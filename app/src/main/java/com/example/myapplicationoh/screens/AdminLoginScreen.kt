package com.example.myapplicationoh.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplicationoh.ui.components.*
import com.example.myapplicationoh.ui.theme.*
import com.example.myapplicationoh.viewmodel.AuthViewModel

@Composable
fun AdminLoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onLoginAsUser: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(AdminDark)) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(top = 72.dp, bottom = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Admin Portal", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(Modifier.height(4.dp))
                Text(
                    "AwfEz — Maintanence Team",
                    fontSize = 14.sp, color = Color.White.copy(alpha = 0.6f)
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Sign in to Admin", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                LabeledTextField(
                    label = "ADMIN EMAIL",
                    value = state.email,
                    onValueChange = viewModel::onEmailChange,
                    placeholder = "admin@company.com"
                )
                LabeledTextField(
                    label = "PASSWORD",
                    value = state.password,
                    onValueChange = viewModel::onPasswordChange,
                    isPassword = true
                )
                if (state.error != null) {
                    Text(
                        state.error!!, color = ErrorRed, fontSize = 13.sp,
                        textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()
                    )
                }
                PrimaryButton(
                    text = "Admin Sign In",
                    onClick = { viewModel.adminSignIn(onLoginSuccess) }
                )
                
                TextButton(
                    onClick = onLoginAsUser,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Login as User", color = PrimaryBlue, fontSize = 14.sp)
                }
            }
        }
    }
}
