package com.example.myapplicationoh.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplicationoh.data.FirestoreRepository
import com.example.myapplicationoh.model.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class IssueFormState(
    val selectedCategory: IssueCategory? = null,
    val selectedIssueType: IssueType? = null,
    val selectedSpaceType: SpaceType = SpaceType.MEETING_ROOM,
    val selectedTower: Tower? = null,
    val selectedFloor: Floor? = null,
    val selectedRoom: Room? = null,
    val selectedDate: String =
        java.text.SimpleDateFormat(
            "EEE, MMM dd, yyyy",
            java.util.Locale.getDefault()).format(java.util.Date())    ,
    val description: String = "",
    val availableIssueTypes: List<IssueType> = emptyList(),
    val availableFloors: List<Floor> = emptyList(),
    val availableRooms: List<Room> = emptyList(),
    val lastSubmittedIssue: Issue? = null
)

data class IssueUiState(
    val issueCategories: List<IssueCategory> = emptyList(),
    val issueTypes: List<IssueType> = emptyList(),
    val towers: List<Tower> = emptyList(),
    val floors: List<Floor> = emptyList(),
    val rooms: List<Room> = emptyList(),
    val myIssues: List<Issue> = emptyList()
)

class IssueViewModel : ViewModel() {

    private val repo = FirestoreRepository()

    private val _formState = MutableStateFlow(IssueFormState())
    val formState: StateFlow<IssueFormState> = _formState.asStateFlow()

    private val _uiState = MutableStateFlow(IssueUiState())
    val uiState: StateFlow<IssueUiState> = _uiState.asStateFlow()

    init {

        repo.observeIssueCategories {
            _uiState.value = _uiState.value.copy(issueCategories = it)
        }

        repo.observeIssueTypes {
            _uiState.value = _uiState.value.copy(issueTypes = it)
        }

        repo.observeTowers {
            _uiState.value = _uiState.value.copy(towers = it)
        }

        repo.observeFloors {
            _uiState.value = _uiState.value.copy(floors = it)
        }

        repo.observeRooms {
            _uiState.value = _uiState.value.copy(rooms = it)
        }

        repo.observeIssues {
            _uiState.value = _uiState.value.copy(myIssues = it)
        }
    }

    fun onCategorySelected(category: IssueCategory) {

        val types = _uiState.value.issueTypes.filter {
            it.categoryId == category.id
        }

        // plumbing special rule
        val spaceType =
            if (category.id == "plumbing") SpaceType.WASHROOM
            else _formState.value.selectedSpaceType

        _formState.value = _formState.value.copy(
            selectedCategory = category,
            selectedIssueType = null,
            selectedSpaceType = spaceType,
            availableIssueTypes = types
        )
    }

    fun onIssueTypeSelected(type: IssueType) {
        _formState.value = _formState.value.copy(
            selectedIssueType = type
        )
    }

    fun onTowerSelected(tower: Tower) {

        val floors = _uiState.value.floors.filter {
            it.towerId == tower.id
        }

        _formState.value = _formState.value.copy(
            selectedTower = tower,
            selectedFloor = null,
            selectedRoom = null,
            availableFloors = floors,
            availableRooms = emptyList()
        )
    }
    fun onSpaceTypeSelected(type: SpaceType) {

        _formState.value = _formState.value.copy(
            selectedSpaceType = type,
            selectedTower = null,
            selectedFloor = null,
            selectedRoom = null
        )

    }

    fun onDateSelected(date: String) {

        _formState.value = _formState.value.copy(
            selectedDate = date
        )

    }

    fun onFloorSelected(floor: Floor) {

        val rooms = _uiState.value.rooms.filter {
            it.floorId == floor.id &&
                    it.type == _formState.value.selectedSpaceType
        }

        _formState.value = _formState.value.copy(
            selectedFloor = floor,
            selectedRoom = null,
            availableRooms = rooms
        )
    }

    fun onRoomSelected(room: Room) {
        _formState.value = _formState.value.copy(selectedRoom = room)
    }

    fun onDescriptionChange(desc: String) {
        _formState.value = _formState.value.copy(description = desc)
    }

    fun submitIssue(onSuccess: (String) -> Unit) {

        val state = _formState.value
        val ref = "ISS-${System.currentTimeMillis()}"
        val user = FirebaseAuth.getInstance().currentUser
        val today = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())

        val issue = Issue(
            id = ref,
            issueRef = ref,
            title = state.selectedIssueType?.name ?: "Issue",
            description = state.description,
            category = state.selectedCategory?.name ?: "",
            location = "${state.selectedTower?.name ?: ""}, ${state.selectedFloor?.name ?: ""}, ${state.selectedRoom?.name ?: ""}",
            tower = state.selectedTower?.name ?: "",
            floor = state.selectedFloor?.name ?: "",
            room = state.selectedRoom?.name ?: "",
            reportedBy = user?.email ?: "User",
            reportedByEmail = user?.email ?: "",
            userId = user?.uid ?: "",
            userEmail = user?.email ?: "",
            reportedDate = today,
            statusString = IssueStatus.PENDING.label
        )

        viewModelScope.launch {

            repo.createIssue(issue)

            _formState.value = _formState.value.copy(
                lastSubmittedIssue = issue
            )

            onSuccess(ref)
        }
    }

    fun getLastSubmittedIssue(): Issue? {
        return _formState.value.lastSubmittedIssue
    }

    fun resetForm() {
        _formState.value = IssueFormState()
    }
}