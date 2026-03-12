package com.example.myapplicationoh.viewmodel


import androidx.lifecycle.ViewModel
import com.example.myapplicationoh.model.User
import com.example.myapplicationoh.repository.MockDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AuthUiState(
    val email: String = "alex.johnson@company.com",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentUser: User? = null
)
class AuthViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email, error = null)
    }
    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password, error = null)
    }
    fun signIn(onSuccess: () -> Unit) {
        val state = _uiState.value
        val user = MockDataRepository.authenticateUser(state.email, state.password)
        if (user != null) {
            _uiState.value = _uiState.value.copy(currentUser = user, error = null)
            onSuccess()
        } else {
            _uiState.value = _uiState.value.copy(error = "Invalid email or password")
        }
    }
    fun adminSignIn(onSuccess: () -> Unit) {
        val state = _uiState.value
        val user = MockDataRepository.authenticateAdmin(state.email, state.password)
        if (user != null) {
            _uiState.value = _uiState.value.copy(currentUser = user, error = null)
            onSuccess()
        } else {
            _uiState.value = _uiState.value.copy(error = "Invalid admin credentials")
        }
    }
    fun getCurrentUser(): User = _uiState.value.currentUser ?: MockDataRepository.regularUser
}