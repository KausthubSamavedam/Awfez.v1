package com.example.myapplicationoh.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplicationoh.model.Floor
import com.example.myapplicationoh.model.Issue
import com.example.myapplicationoh.model.IssueCategory
import com.example.myapplicationoh.model.IssueStatus
import com.example.myapplicationoh.model.IssueType
import com.example.myapplicationoh.model.Room
import com.example.myapplicationoh.model.SpaceType
import com.example.myapplicationoh.model.Tower
import com.example.myapplicationoh.repository.MockDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class IssueFormState(
    val selectedCategory: IssueCategory? = null,
    val selectedIssueType: IssueType? = null,
    val selectedSpaceType: SpaceType = SpaceType.MEETING_ROOM,
    val selectedTower: Tower? = null,
    val selectedFloor: Floor? = null,
    val selectedRoom: Room? = null,
    val selectedDate: String = "Thursday, Mar 13, 2026",
    val description: String = "",
    val availableIssueTypes: List<IssueType> = emptyList(),
    val availableFloors: List<Floor> = emptyList(),
    val availableRooms: List<Room> = emptyList(),
    val lastSubmittedIssue: Issue? = null
)

class IssueViewModel : ViewModel() {
    private val _formState = MutableStateFlow(IssueFormState())
    val formState: StateFlow<IssueFormState> = _formState.asStateFlow()

    val issueCategories = MockDataRepository.issueCategories
    val towers = MockDataRepository.towers
    val myIssues get() = MockDataRepository.getMyIssues("alex.johnson@company.com")

    fun onCategorySelected(category: IssueCategory) {
        val types = MockDataRepository.getIssueTypesForCategory(category.id)
        _formState.value = _formState.value.copy(
            selectedCategory = category,
            selectedIssueType = null,
            availableIssueTypes = types
        )
    }

    fun onIssueTypeSelected(type: IssueType) {
        _formState.value = _formState.value.copy(selectedIssueType = type)
    }

    fun onSpaceTypeSelected(type: SpaceType) {
        _formState.value = _formState.value.copy(
            selectedSpaceType = type,
            selectedTower = null, selectedFloor = null, selectedRoom = null
        )
    }

    fun onTowerSelected(tower: Tower) {
        val floors = MockDataRepository.getFloorsForTower(tower.id)
        _formState.value = _formState.value.copy(
            selectedTower = tower, selectedFloor = null, selectedRoom = null,
            availableFloors = floors, availableRooms = emptyList()
        )
    }

    fun onFloorSelected(floor: Floor) {
        val rooms = MockDataRepository.getRoomsForFloor(floor.id)
        _formState.value = _formState.value.copy(
            selectedFloor = floor, selectedRoom = null, availableRooms = rooms
        )
    }

    fun onRoomSelected(room: Room) {
        _formState.value = _formState.value.copy(selectedRoom = room)
    }

    fun onDateSelected(date: String) {
        _formState.value = _formState.value.copy(selectedDate = date)
    }

    fun onDescriptionChange(desc: String) {
        _formState.value = _formState.value.copy(description = desc)
    }

    fun submitIssue(onSuccess: (String) -> Unit) {
        val state = _formState.value
        val ref = "#ISS-2026-${(1000..9999).random()}"
        val issue = Issue(
            id = ref, issueRef = ref,
            title = state.selectedIssueType?.name ?: "Issue",
            description = state.description,
            category = state.selectedCategory?.name ?: "",
            location = "${state.selectedTower?.name ?: ""}, ${state.selectedFloor?.name ?: ""}, ${state.selectedRoom?.name ?: ""}",
            tower = state.selectedTower?.name ?: "Tower B",
            floor = state.selectedFloor?.name ?: "Floor 3",
            room = state.selectedRoom?.name ?: "",
            reportedBy = "Alex Johnson",
            reportedByEmail = "alex.johnson@company.com",
            reportedDate = "Mar 11, 2026",
            status = IssueStatus.PENDING
        )
        MockDataRepository.issues.add(0, issue)
        _formState.value = _formState.value.copy(lastSubmittedIssue = issue)
        onSuccess(ref)
    }

    fun getLastSubmittedIssue() = _formState.value.lastSubmittedIssue

    fun resetForm() {
        _formState.value = IssueFormState()
    }
}