package com.example.myapplicationoh.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplicationoh.model.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BookingUiState(
    val towers: List<Tower> = emptyList(),
    val floors: List<Floor> = emptyList(),
    val rooms: List<Room> = emptyList(),
    val bookings: List<Booking> = emptyList(),
    val selectedType: SpaceType = SpaceType.MEETING_ROOM,
    val selectedTower: Tower? = null,
    val selectedFloor: Floor? = null,
    val selectedRoom: Room? = null,
    val selectedDate: String = "Thursday, Mar 13, 2026",
    val selectedTimeSlot: TimeSlot? = null,
    val availableFloors: List<Floor> = emptyList(),
    val availableRooms: List<Room> = emptyList(),
    val availableTimeSlots: List<TimeSlot> = emptyList(),
    val lastConfirmedBooking: Booking? = null
)

class BookingViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _uiState = MutableStateFlow(BookingUiState())
    val uiState: StateFlow<BookingUiState> = _uiState.asStateFlow()

    init {
        observeTowers()
        observeFloors()
        observeRooms()
        observeBookings()
    }

    private fun observeTowers() {
        db.collection("towers")
            .addSnapshotListener { snapshot, _ ->
                val towers = snapshot?.documents?.mapNotNull {
                    it.toObject(Tower::class.java)
                } ?: emptyList()
                _uiState.value = _uiState.value.copy(towers = towers)
            }
    }

    private fun observeFloors() {
        db.collection("floors")
            .addSnapshotListener { snapshot, _ ->
                val floors = snapshot?.documents?.mapNotNull {
                    it.toObject(Floor::class.java)
                } ?: emptyList()
                _uiState.value = _uiState.value.copy(floors = floors)
            }
    }

    private fun observeRooms() {
        db.collection("rooms")
            .addSnapshotListener { snapshot, _ ->
                val rooms = snapshot?.documents?.mapNotNull {
                    it.toObject(Room::class.java)
                } ?: emptyList()
                _uiState.value = _uiState.value.copy(rooms = rooms)
            }
    }

    private fun observeBookings() {
        db.collection("bookings")
            .addSnapshotListener { snapshot, _ ->
                val bookings = snapshot?.documents?.mapNotNull {
                    it.toObject(Booking::class.java)
                } ?: emptyList()
                _uiState.value = _uiState.value.copy(bookings = bookings)
            }
    }

    fun onTypeSelected(type: SpaceType) {
        _uiState.value = _uiState.value.copy(
            selectedType = type,
            selectedTower = null,
            selectedFloor = null,
            selectedRoom = null,
            selectedTimeSlot = null,
            availableFloors = emptyList(),
            availableRooms = emptyList(),
            availableTimeSlots = emptyList()
        )
    }

    fun onTowerSelected(tower: Tower) {
        val floors = _uiState.value.floors.filter {
            it.towerId == tower.id
        }
        _uiState.value = _uiState.value.copy(
            selectedTower = tower,
            selectedFloor = null,
            selectedRoom = null,
            selectedTimeSlot = null,
            availableFloors = floors,
            availableRooms = emptyList(),
            availableTimeSlots = emptyList()
        )
    }

    fun onFloorSelected(floor: Floor) {
        val rooms = _uiState.value.rooms.filter {
            it.floorId == floor.id && it.type == _uiState.value.selectedType
        }
        _uiState.value = _uiState.value.copy(
            selectedFloor = floor,
            selectedRoom = null,
            selectedTimeSlot = null,
            availableRooms = rooms,
            availableTimeSlots = generateTimeSlots()
        )
    }

    fun onRoomSelected(room: Room) {
        _uiState.value = _uiState.value.copy(
            selectedRoom = room,
            selectedTimeSlot = null,
            availableTimeSlots = generateTimeSlots()
        )
    }

    fun onDateSelected(date: String) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
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
        if (state.selectedRoom == null || state.selectedTimeSlot == null) {
            onFailure()
            return
        }
        val ref = "BK-${System.currentTimeMillis()}"
        val booking = Booking(
            id = ref,
            bookingRef = ref,
            roomName = state.selectedRoom.name,
            tower = state.selectedTower?.name ?: "",
            floor = state.selectedFloor?.name ?: "",
            date = state.selectedDate,
            timeSlot = state.selectedTimeSlot.label,
            categoryString = state.selectedType.firestoreValue,
            statusString = BookingStatus.TODAY.label
        )
        viewModelScope.launch {
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

    private fun generateTimeSlots(): List<TimeSlot> {
        return listOf(
            TimeSlot("1", "09:00 - 10:00"),
            TimeSlot("2", "10:00 - 11:00"),
            TimeSlot("3", "11:00 - 12:00"),
            TimeSlot("4", "13:00 - 14:00"),
            TimeSlot("5", "14:00 - 15:00"),
            TimeSlot("6", "15:00 - 16:00")
        )
    }

    fun resetBookingForm() {
        _uiState.value = BookingUiState(
            towers = _uiState.value.towers,
            floors = _uiState.value.floors,
            rooms = _uiState.value.rooms,
            bookings = _uiState.value.bookings
        )
    }
}