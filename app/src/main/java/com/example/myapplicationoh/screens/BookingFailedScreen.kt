package com.example.myapplicationoh.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplicationoh.ui.components.*
import com.example.myapplicationoh.ui.theme.*

@Composable
fun BookingFailedScreen(onTryAnotherSlot: () -> Unit, onBackToHome: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(80.dp).clip(CircleShape).background(ErrorRedLight),
            contentAlignment = Alignment.Center
        ) { Text("❌", fontSize = 38.sp) }

        Spacer(Modifier.height(24.dp))
        Text("Booking Unavailable", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(Modifier.height(8.dp))
        Text(
            "This space was just booked by someone else. Please select a different time slot or room.",
            fontSize = 14.sp, color = TextSecondary, textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(SuccessGreenLight)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("💡", fontSize = 16.sp)
                Spacer(Modifier.width(8.dp))
                Text(
                    "Available at 11:00 – 12:00 PM",
                    color = Color(0xFF2D6A4F), fontSize = 14.sp, fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(Modifier.height(24.dp))
        PrimaryButton(text = "← Try Another Slot", onClick = onTryAnotherSlot, color = ErrorRed)
        Spacer(Modifier.height(12.dp))
        TextButton(onClick = onBackToHome, modifier = Modifier.fillMaxWidth()) {
            Text("Back to Home", color = TextSecondary, fontSize = 14.sp)
        }
    }
}