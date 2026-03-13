package com.example.myapplicationoh.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplicationoh.model.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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

data class IssueUiState(
    val issueCategories: List<IssueCategory> = emptyList(),
    val issueTypes: List<IssueType> = emptyList(),
    val towers: List<Tower> = emptyList(),
    val floors: List<Floor> = emptyList(),
    val rooms: List<Room> = emptyList(),
    val myIssues: List<Issue> = emptyList()
)

class IssueViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _formState = MutableStateFlow(IssueFormState())
    val formState: StateFlow<IssueFormState> = _formState.asStateFlow()
    private val _uiState = MutableStateFlow(IssueUiState())
    val uiState: StateFlow<IssueUiState> = _uiState.asStateFlow()

    init {
        observeCategories()
        observeIssueTypes()
        observeTowers()
        observeFloors()
        observeRooms()
        observeIssues()
    }

    private fun observeCategories() {
        db.collection("issueCategories")
            .addSnapshotListener { snapshot, _ ->
                val categories = snapshot?.documents?.mapNotNull {
                    it.toObject(IssueCategory::class.java)
                } ?: emptyList()
                _uiState.value = _uiState.value.copy(issueCategories = categories)
            }
    }

    private fun observeIssueTypes() {
        db.collection("issueTypes")
            .addSnapshotListener { snapshot, _ ->
                val types = snapshot?.documents?.mapNotNull {
                    it.toObject(IssueType::class.java)
                } ?: emptyList()
                _uiState.value = _uiState.value.copy(issueTypes = types)
            }
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

    private fun observeIssues() {
        db.collection("issues")
            .addSnapshotListener { snapshot, _ ->
                val issues = snapshot?.documents?.mapNotNull {
                    it.toObject(Issue::class.java)
                } ?: emptyList()
                _uiState.value = _uiState.value.copy(myIssues = issues)
            }
    }

    fun onCategorySelected(category: IssueCategory) {
        val types = _uiState.value.issueTypes.filter {
            it.categoryId == category.id
        }
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
            selectedTower = null,
            selectedFloor = null,
            selectedRoom = null
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

    fun onFloorSelected(floor: Floor) {
        val rooms = _uiState.value.rooms.filter {
            it.floorId == floor.id
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

    fun onDateSelected(date: String) {
        _formState.value = _formState.value.copy(selectedDate = date)
    }

    fun onDescriptionChange(desc: String) {
        _formState.value = _formState.value.copy(description = desc)
    }

    fun submitIssue(onSuccess: (String) -> Unit) {
        val state = _formState.value
        val ref = "ISS-${System.currentTimeMillis()}"
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
            reportedBy = "Alex Johnson",
            reportedByEmail = "alex.johnson@company.com",
            reportedDate = "Mar 11, 2026",
            statusString = IssueStatus.PENDING.label
        )
        viewModelScope.launch {
            db.collection("issues")
                .document(ref)
                .set(issue)
                .addOnSuccessListener {
                    _formState.value = _formState.value.copy(
                        lastSubmittedIssue = issue
                    )
                    onSuccess(ref)
                }
        }
    }

    fun getLastSubmittedIssue() = _formState.value.lastSubmittedIssue

    fun resetForm() {
        _formState.value = IssueFormState()
    }
}