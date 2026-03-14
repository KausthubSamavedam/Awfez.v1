package com.example.myapplicationoh.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplicationoh.data.FirestoreRepository
import com.example.myapplicationoh.model.Issue
import com.example.myapplicationoh.model.IssueStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AdminUiState(
    val issues: List<Issue> = emptyList(),
    val filterStatus: IssueStatus? = null
)

class AdminViewModel : ViewModel() {

    private val repo = FirestoreRepository()

    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    init {
        observeIssues()
    }

    private fun observeIssues() {
        repo.observeAllIssues { issues ->
            _uiState.value = _uiState.value.copy(issues = issues)
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

    fun updateIssueStatus(issueId: String, newStatus: IssueStatus) {
        viewModelScope.launch {
            repo.updateIssueStatus(issueId, newStatus.label)
        }
    }

    fun updateIssueAssignment(issueId: String, department: String) {
        viewModelScope.launch {
            repo.updateIssueAssignment(issueId, department)
        }
    }
}