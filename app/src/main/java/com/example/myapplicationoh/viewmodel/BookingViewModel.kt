package com.example.myapplicationoh.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplicationoh.model.Booking
import com.example.myapplicationoh.model.BookingStatus
import com.example.myapplicationoh.model.Floor
import com.example.myapplicationoh.model.Room
import com.example.myapplicationoh.model.SpaceType
import com.example.myapplicationoh.model.TimeSlot
import com.example.myapplicationoh.model.Tower
import com.example.myapplicationoh.repository.MockDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class BookingUiState(
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
    private val _uiState = MutableStateFlow(BookingUiState())
    val uiState: StateFlow<BookingUiState> = _uiState.asStateFlow()

    val towers = MockDataRepository.towers
    val bookings get() = MockDataRepository.bookings.toList()

    fun onTypeSelected(type: SpaceType) {
        _uiState.value = _uiState.value.copy(
            selectedType = type,
            selectedTower = null, selectedFloor = null,
            selectedRoom = null, selectedTimeSlot = null,
            availableFloors = emptyList(), availableRooms = emptyList(),
            availableTimeSlots = emptyList()
        )
    }

    fun onTowerSelected(tower: Tower) {
        val floors = MockDataRepository.getFloorsForTower(tower.id)
        _uiState.value = _uiState.value.copy(
            selectedTower = tower, selectedFloor = null,
            selectedRoom = null, selectedTimeSlot = null,
            availableFloors = floors, availableRooms = emptyList(),
            availableTimeSlots = emptyList()
        )
    }

    fun onFloorSelected(floor: Floor) {
        val rooms = MockDataRepository.getRoomsForFloor(floor.id)
            .filter { it.type == _uiState.value.selectedType }
        _uiState.value = _uiState.value.copy(
            selectedFloor = floor, selectedRoom = null,
            selectedTimeSlot = null, availableRooms = rooms,
            availableTimeSlots = emptyList()
        )
    }

    fun onRoomSelected(room: Room) {
        val slots = MockDataRepository.getTimeSlotsForRoom(room.id)
        _uiState.value = _uiState.value.copy(
            selectedRoom = room, selectedTimeSlot = null,
            availableTimeSlots = slots
        )
    }

    fun onDateSelected(date: String) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
    }

    fun onTimeSlotSelected(slot: TimeSlot) {
        if (!slot.isBooked) _uiState.value = _uiState.value.copy(selectedTimeSlot = slot)
    }

    fun bookSpace(onSuccess: (String) -> Unit, onFailure: () -> Unit) {
        val state = _uiState.value
        if (state.selectedRoom == null || state.selectedTimeSlot == null) return
        val ref = "BK-2026-${(4000..9999).random()}"
        val booking = Booking(
            id = ref, bookingRef = ref,
            roomName = state.selectedRoom.name,
            tower = state.selectedTower?.name ?: "",
            floor = state.selectedFloor?.name ?: "",
            date = state.selectedDate,
            timeSlot = state.selectedTimeSlot.label,
            category = state.selectedType,
            status = BookingStatus.TODAY
        )
        MockDataRepository.bookings.add(0, booking)
        _uiState.value = _uiState.value.copy(lastConfirmedBooking = booking)
        onSuccess(ref)
    }

    fun getLastConfirmedBooking() = _uiState.value.lastConfirmedBooking

    fun resetBookingForm() {
        _uiState.value = BookingUiState()
    }
}