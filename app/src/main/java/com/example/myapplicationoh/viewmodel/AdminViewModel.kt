package com.example.myapplicationoh.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplicationoh.model.Issue
import com.example.myapplicationoh.model.IssueStatus
import com.example.myapplicationoh.repository.MockDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AdminUiState(
    val filterStatus: IssueStatus? = null
)

class AdminViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()
    val allIssues get() = MockDataRepository.getAllIssues()
    fun filteredIssues(status: IssueStatus?): List<Issue> =
        if (status == null) allIssues else allIssues.filter { it.status == status }
    fun pendingCount() = allIssues.count { it.status == IssueStatus.PENDING }
    fun assignedCount() = allIssues.count { it.status == IssueStatus.ASSIGNED }
    fun inProgressCount() = allIssues.count { it.status == IssueStatus.IN_PROGRESS }
    fun resolvedCount() = allIssues.count { it.status == IssueStatus.RESOLVED }

    fun setFilter(status: IssueStatus?) {
        _uiState.value = _uiState.value.copy(filterStatus = status)
    }

    fun getIssueById(id: String) =
        allIssues.find { it.id == id || it.issueRef == id }

    fun updateIssueStatus(issueId: String, newStatus: IssueStatus) {
        val idx = MockDataRepository.issues.indexOfFirst { it.id == issueId || it.issueRef == issueId }
        if (idx >= 0) {
            MockDataRepository.issues[idx] = MockDataRepository.issues[idx].copy(status = newStatus)
        }
    }

    fun updateIssueAssignment(issueId: String, department: String) {
        val idx = MockDataRepository.issues.indexOfFirst { it.id == issueId || it.issueRef == issueId }
        if (idx >= 0) {
            MockDataRepository.issues[idx] = MockDataRepository.issues[idx].copy(
                assignedTo = department, status = IssueStatus.ASSIGNED
            )
        }
    }
}