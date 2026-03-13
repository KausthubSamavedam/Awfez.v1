package com.example.myapplicationoh.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplicationoh.model.Issue
import com.example.myapplicationoh.model.IssueStatus
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AdminUiState(
    val issues: List<Issue> = emptyList(),
    val filterStatus: IssueStatus? = null
)

class AdminViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    init {
        observeIssues()
    }

    private fun observeIssues() {
        db.collection("issues")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val issues = snapshot.documents.mapNotNull {
                        it.toObject(Issue::class.java)
                    }
                    _uiState.value = _uiState.value.copy(issues = issues)
                }
            }
    }

    fun filteredIssues(status: IssueStatus?): List<Issue> {
        val issues = _uiState.value.issues
        return if (status == null) issues
        else issues.filter { it.status == status }
    }

    fun pendingCount() =
        _uiState.value.issues.count { it.status == IssueStatus.PENDING }

    fun assignedCount() =
        _uiState.value.issues.count { it.status == IssueStatus.ASSIGNED }

    fun inProgressCount() =
        _uiState.value.issues.count { it.status == IssueStatus.IN_PROGRESS }

    fun resolvedCount() =
        _uiState.value.issues.count { it.status == IssueStatus.RESOLVED }

    fun setFilter(status: IssueStatus?) {
        _uiState.value = _uiState.value.copy(filterStatus = status)
    }

    fun getIssueById(id: String): Issue? {
        return _uiState.value.issues.find {
            it.id == id || it.issueRef == id
        }
    }

    fun updateIssueStatus(issueId: String, newStatus: IssueStatus) {
        db.collection("issues")
            .document(issueId)
            .update("status", newStatus.label)
    }

    fun updateIssueAssignment(issueId: String, department: String) {
        db.collection("issues")
            .document(issueId)
            .update(
                mapOf(
                    "assignedTo" to department,
                    "status" to IssueStatus.ASSIGNED.label
                )
            )
    }
}