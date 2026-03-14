package com.example.myapplicationoh.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplicationoh.data.FirestoreRepository
import com.example.myapplicationoh.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class BookingUiState(
    val towers: List<Tower> = emptyList(),
    val floors: List<Floor> = emptyList(),
    val rooms: List<Room> = emptyList(),
    val bookings: List<Booking> = emptyList(),
    val myBookings: List<Booking> = emptyList(),
    val selectedType: SpaceType = SpaceType.TRAINING_ROOM,
    val selectedTower: Tower? = null,
    val selectedFloor: Floor? = null,
    val selectedRoom: Room? = null,
    val selectedDate: String = "Select Date",
    val selectedTimeSlot: TimeSlot? = null,
    val availableFloors: List<Floor> = emptyList(),
    val availableRooms: List<Room> = emptyList(),
    val availableTimeSlots: List<TimeSlot> = emptyList(),
    val lastConfirmedBooking: Booking? = null
)

class BookingViewModel : ViewModel() {

    private val repo = FirestoreRepository()
    private val db = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow(BookingUiState())
    val uiState: StateFlow<BookingUiState> = _uiState.asStateFlow()

    init {

        repo.observeTowers {
            _uiState.value = _uiState.value.copy(towers = it)
        }

        repo.observeFloors {
            _uiState.value = _uiState.value.copy(floors = it)
        }

        repo.observeRooms {
            _uiState.value = _uiState.value.copy(rooms = it)
        }

        // Logged-in user's bookings
        repo.observeUserBookings {
            _uiState.value = _uiState.value.copy(myBookings = it)
            Log.d("SNAPS" , "${it.size}")
        }

        // All bookings (for slot disabling)
        repo.observeAllBookings { bookings ->

            val state = _uiState.value
            val room = state.selectedRoom

            val updatedSlots =
                if (room != null) {

                    val roomBookings = bookings.filter {
                        it.roomName == room.name &&
                                it.date.trim() == state.selectedDate.trim()
                    }

                    generateTimeSlots(roomBookings)

                } else emptyList()

            _uiState.value = state.copy(
                bookings = bookings,
                availableTimeSlots = updatedSlots
            )
        }
    }

    fun onTypeSelected(type: SpaceType) {

        val tower = _uiState.value.selectedTower

        val floors =
            if (tower != null) {

                val roomsForTowerAndType = _uiState.value.rooms.filter {
                    it.towerId == tower.id && it.type == type
                }

                val floorIds = roomsForTowerAndType.map { it.floorId }.toSet()

                _uiState.value.floors.filter {
                    it.id in floorIds
                }

            } else emptyList()

        _uiState.value = _uiState.value.copy(
            selectedType = type,
            selectedFloor = null,
            selectedRoom = null,
            availableFloors = floors,
            availableRooms = emptyList(),
            availableTimeSlots = emptyList()
        )
    }

    fun onTowerSelected(tower: Tower) {

        val selectedType = _uiState.value.selectedType

        val roomsForTowerAndType = _uiState.value.rooms.filter {
            it.towerId == tower.id && it.type == selectedType
        }

        val floorIds = roomsForTowerAndType.map { it.floorId }.toSet()

        val floors = _uiState.value.floors.filter {
            it.id in floorIds
        }

        _uiState.value = _uiState.value.copy(
            selectedTower = tower,
            selectedFloor = null,
            selectedRoom = null,
            availableFloors = floors,
            availableRooms = emptyList(),
            availableTimeSlots = emptyList()
        )
    }

    fun onFloorSelected(floor: Floor) {

        val rooms = _uiState.value.rooms.filter {
            it.floorId == floor.id &&
                    it.type == _uiState.value.selectedType
        }

        _uiState.value = _uiState.value.copy(
            selectedFloor = floor,
            selectedRoom = null,
            selectedTimeSlot = null,
            availableRooms = rooms,
            availableTimeSlots = emptyList()
        )
    }

    fun onRoomSelected(room: Room) {

        val state = _uiState.value

        val roomBookings = state.bookings.filter {
            it.roomName == room.name &&
                    it.date.trim() == state.selectedDate.trim()
        }

        _uiState.value = state.copy(
            selectedRoom = room,
            selectedTimeSlot = null,
            availableTimeSlots = generateTimeSlots(roomBookings)
        )
    }

    fun onDateSelected(date: String) {

        val state = _uiState.value
        val room = state.selectedRoom

        if (room != null) {

            val roomBookings = state.bookings.filter {
                it.roomName == room.name &&
                        it.date.trim() == date.trim()
            }

            _uiState.value = state.copy(
                selectedDate = date,
                selectedTimeSlot = null,
                availableTimeSlots = generateTimeSlots(roomBookings)
            )

        } else {

            _uiState.value = state.copy(selectedDate = date)
        }
    }

    fun onTimeSlotSelected(slot: TimeSlot) {

        if (!slot.isBooked) {
            _uiState.value = _uiState.value.copy(selectedTimeSlot = slot)
        }
    }

    fun bookSpace(
        onSuccess: (String) -> Unit,
        onFailure: () -> Unit
    ) {

        val state = _uiState.value
        val user = FirebaseAuth.getInstance().currentUser

        if (state.selectedRoom == null || state.selectedTimeSlot == null) {
            onFailure()
            return
        }

        val ref = "BK-${System.currentTimeMillis()}"
        val formatter = SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault())

        val today = formatter.format(Date())

        val status =
            if (state.selectedDate == today)
                BookingStatus.TODAY.label
            else
                BookingStatus.UPCOMING.label

        val booking = Booking(
            id = ref,
            bookingRef = ref,
            roomName = state.selectedRoom.name,
            userId = user?.uid ?: "",
            userEmail = user?.email ?: "",
            tower = state.selectedTower?.name ?: "",
            floor = state.selectedFloor?.name ?: "",
            date = state.selectedDate,
            timeSlot = state.selectedTimeSlot.label,
            categoryString = state.selectedType.firestoreValue,
            statusString = status
        )

        viewModelScope.launch {

            val conflict = db.collection("bookings")
                .whereEqualTo("roomName", state.selectedRoom.name)
                .whereEqualTo("date", state.selectedDate)
                .whereEqualTo("timeSlot", state.selectedTimeSlot.label)
                .get()
                .await()

            if (!conflict.isEmpty) {
                onFailure()
                return@launch
            }

            db.collection("bookings")
                .document(ref)
                .set(booking)
                .addOnSuccessListener {

                    _uiState.value = _uiState.value.copy(
                        lastConfirmedBooking = booking
                    )

                    onSuccess(ref)
                }
                .addOnFailureListener {
                    onFailure()
                }
        }
    }

    fun generateTimeSlots(bookings: List<Booking>): List<TimeSlot> {

        val bookedSlots = bookings.map { it.timeSlot }

        return listOf(
            TimeSlot("1","09:00 - 10:00", bookedSlots.contains("09:00 - 10:00")),
            TimeSlot("2","10:00 - 11:00", bookedSlots.contains("10:00 - 11:00")),
            TimeSlot("3","11:00 - 12:00", bookedSlots.contains("11:00 - 12:00")),
            TimeSlot("4","13:00 - 14:00", bookedSlots.contains("13:00 - 14:00")),
            TimeSlot("5","14:00 - 15:00", bookedSlots.contains("14:00 - 15:00")),
            TimeSlot("6","15:00 - 16:00", bookedSlots.contains("15:00 - 16:00"))
        )
    }

    fun resetBookingForm() {

        val state = _uiState.value

        _uiState.value = BookingUiState(
            towers = state.towers,
            floors = state.floors,
            rooms = state.rooms,
            bookings = state.bookings,
            myBookings = state.myBookings
        )
    }
    fun getBookingByRef(ref: String, onResult: (Booking?) -> Unit) {

        FirebaseFirestore.getInstance()
            .collection("bookings")
            .document(ref)
            .get()
            .addOnSuccessListener {
                onResult(it.toObject(Booking::class.java))
            }
    }
    //------------cancel booking -----------
    fun cancelBooking(
        bookingId: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        viewModelScope.launch {

            db.collection("bookings")
                .document(bookingId)
                .delete()
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener {
                    onFailure()
                }
        }
    }
}