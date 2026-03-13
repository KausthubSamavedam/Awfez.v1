package com.example.myapplicationoh.viewmodel


import androidx.lifecycle.ViewModel
import com.example.myapplicationoh.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentUser: User? = null
)
class AuthViewModel : ViewModel() {
    val auth = FirebaseAuth.getInstance()
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
        auth.signInWithEmailAndPassword(
            state.email,
            state.password
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val firebaseUser = auth.currentUser
                _uiState.value = _uiState.value.copy(
                    error = null
                )
                onSuccess()
            } else {
                _uiState.value = _uiState.value.copy(
                    error = task.exception?.message ?: "Login failed"
                )
            }
        }
    }

    fun adminSignIn(onSuccess: () -> Unit) {
        val state = _uiState.value
        auth.signInWithEmailAndPassword(
            state.email,
            state.password
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val firebaseUser = auth.currentUser
                if (firebaseUser?.email == "admin@ltm.com") {
                    _uiState.value = _uiState.value.copy(error = null)
                    onSuccess()
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = "Not authorized as admin"
                    )
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    error = task.exception?.message ?: "Admin login failed"
                )
            }
        }
    }

    fun getCurrentUserEmail(): String? {
        return auth.currentUser?.email
    }
    fun logout() {
        auth.signOut()
    }
}