package com.example.myapplicationoh.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplicationoh.model.Booking
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

    var booking by remember { mutableStateOf<Booking?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getBookingByRef(bookingRef) {
            booking = it
        }
    }

    if (booking == null) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        return
    }

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

        Spacer(Modifier.height(24.dp))

        DetailCard {

            InfoRow("Room", booking!!.roomName)

            HorizontalDivider(color = DividerColor)

            InfoRow("Date", booking!!.date)

            HorizontalDivider(color = DividerColor)

            InfoRow("Time", booking!!.timeSlot)

            HorizontalDivider(color = DividerColor)

            InfoRow("Floor", booking!!.floor)

            HorizontalDivider(color = DividerColor)

            InfoRow("Category", booking!!.categoryString)
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
                    color = TextSecondary
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    booking!!.bookingRef,
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
                color = TextSecondary
            )
        }
    }
}