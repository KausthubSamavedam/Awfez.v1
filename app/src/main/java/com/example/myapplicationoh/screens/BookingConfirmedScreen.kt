package com.example.myapplicationoh.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.example.myapplicationoh.viewmodel.BookingViewModel
@Composable
fun BookingConfirmedScreen(
    bookingRef: String,
    viewModel: BookingViewModel,
    onBackToHome: () -> Unit,
    onViewBookings: () -> Unit
) {
    // Observe booking state from ViewModel
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val booking = uiState.lastConfirmedBooking
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(SuccessGreenLight),
            contentAlignment = Alignment.Center
        ) {
            Text("✅", fontSize = 38.sp)
        }
        Spacer(Modifier.height(24.dp))
        Text(
            "Booking Confirmed!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Your space has been reserved. A confirmation has been sent to your email.",
            fontSize = 14.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        DetailCard {
            InfoRow(
                "Room",
                booking?.roomName?.substringBefore(" —") ?: "Room 803, Tower B"
            )
            HorizontalDivider(color = DividerColor)
            InfoRow(
                "Date",
                booking?.date ?: "Thu, Mar 13, 2026"
            )
            HorizontalDivider(color = DividerColor)
            InfoRow(
                "Time",
                booking?.timeSlot ?: "11:00 AM – 12:00 PM"
            )
            HorizontalDivider(color = DividerColor)
            InfoRow(
                "Floor",
                booking?.floor ?: "Floor 8"
            )
            HorizontalDivider(color = DividerColor)
            InfoRow(
                "Category",
                booking?.category?.displayName ?: "Meeting Room"
            )
        }
        Spacer(Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFEEF2FF))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "BOOKING REFERENCE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary,
                    letterSpacing = 0.8.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    bookingRef,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                )
            }
        }
        Spacer(Modifier.height(24.dp))
        PrimaryButton(
            text = "Back to Home",
            onClick = onBackToHome
        )
        Spacer(Modifier.height(12.dp))
        TextButton(
            onClick = onViewBookings,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "View My Bookings",
                color = TextSecondary,
                fontSize = 14.sp
            )
        }
    }
}