package com.example.myapplicationoh.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplicationoh.ui.components.*
import com.example.myapplicationoh.ui.theme.*
import com.example.myapplicationoh.viewmodel.AuthViewModel


@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onAdminPortal: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(BlueGradientStart, BlueGradientEnd)))
                .padding(top = 80.dp, bottom = 48.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(18.dp)),
                    contentAlignment = Alignment.Center
                ) { Text("🏢", fontSize = 36.sp) }
                Spacer(Modifier.height(16.dp))
                Text("AwfEz", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(Modifier.height(4.dp))
                Text(
                    "Manage your workplace, effortlessly",
                    fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LabeledTextField(
                label = "EMAIL ADDRESS",
                value = state.email,
                onValueChange = viewModel::onEmailChange,
                placeholder = "your.email@company.com"
            )
            LabeledTextField(
                label = "PASSWORD",
                value = state.password,
                onValueChange = viewModel::onPasswordChange,
                placeholder = "••••••••••••",
                isPassword = true
            )
            if (state.error != null) {
                Text(
                    state.error!!, color = ErrorRed, fontSize = 13.sp,
                    textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(Modifier.height(8.dp))
            PrimaryButton(text = "Sign In →", onClick = { viewModel.signIn(onLoginSuccess) })
            TextButton(onClick = onAdminPortal, modifier = Modifier.fillMaxWidth()) {
                Text("Admin Portal", color = PrimaryBlue, fontSize = 14.sp)
            }
        }
    }
}