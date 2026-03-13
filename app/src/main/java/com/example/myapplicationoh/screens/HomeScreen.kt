package com.example.myapplicationoh.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplicationoh.model.BookingStatus
import com.example.myapplicationoh.ui.components.*
import com.example.myapplicationoh.ui.theme.*
import com.example.myapplicationoh.viewmodel.AuthViewModel
import com.example.myapplicationoh.viewmodel.BookingViewModel

@Composable
fun HomeScreen(
    viewModel: AuthViewModel,
    bookingViewModel: BookingViewModel,
    onBookSpace: () -> Unit,
    onReportIssue: () -> Unit,
    onMyBookings: () -> Unit,
    onMyIssues: () -> Unit,
    onLogout: () -> Unit
) {
    val userEmail = viewModel.getCurrentUserEmail()?:"User"
    val user = userEmail.split("@")[0].replaceFirstChar { it.uppercase() }

    val bookingState by bookingViewModel.uiState.collectAsStateWithLifecycle()
    val bookings = bookingState.bookings
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomAppBar(containerColor = Color.White, tonalElevation = 4.dp) {
                listOf(
                    Triple("Home", Icons.Default.Home, 0),
                    Triple("Bookings", Icons.Default.DateRange, 1),
                    Triple("Issues", Icons.Default.Build, 2)
                ).forEach { (label, icon, idx) ->
                    NavigationBarItem(
                        selected = selectedTab == idx,
                        onClick = {
                            selectedTab = idx
                            when (idx) { 1 -> onMyBookings(); 2 -> onMyIssues() }
                        },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryBlue, selectedTextColor = PrimaryBlue,
                            unselectedIconColor = TextSecondary, unselectedTextColor = TextSecondary,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(BlueGradientStart, BlueGradientEnd)))
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Good morning",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Text(
                            user,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(Modifier.width(8.dp))
                        IconButton(onClick = onLogout) {
                            Icon(
                                Icons.Default.ExitToApp,
                                contentDescription = "Logout",
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                SectionHeader("QUICK ACTIONS")
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    QuickActionCard(
                        title = "Book a Shared Space", subtitle = "Rooms & desks",
                        emoji = "📅", onClick = onBookSpace,
                        modifier = Modifier.weight(1f), bgColor = Color(0xFFEEF2FF)
                    )
                    QuickActionCard(
                        title = "Report an Issue", subtitle = "Facilities & IT",
                        emoji = "🔧", onClick = onReportIssue,
                        modifier = Modifier.weight(1f), bgColor = Color(0xFFF0FFF4)
                    )
                }
                Spacer(Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    QuickActionCard(
                        title = "My Bookings", subtitle = "View & manage",
                        emoji = "📋", onClick = onMyBookings,
                        modifier = Modifier.weight(1f), bgColor = Color(0xFFFFF3E0)
                    )
                    QuickActionCard(
                        title = "My Issues", subtitle = "Track status",
                        emoji = "📊", onClick = onMyIssues,
                        modifier = Modifier.weight(1f), bgColor = Color(0xFFFFF5F5)
                    )
                }

                Spacer(Modifier.height(24.dp))
                SectionHeader("UPCOMING BOOKINGS")

                val upcomingBookings = bookings.filter { booking -> booking.status != BookingStatus.COMPLETED }
                if (upcomingBookings.isEmpty()) {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "No upcoming bookings",
                            modifier = Modifier.padding(16.dp),
                            color = TextSecondary, fontSize = 14.sp
                        )
                    }
                } else {
                    upcomingBookings.take(3).forEach { booking ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(1.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                                .clickable(onClick = onMyBookings)
                        ) {
                            Text(
                                text = "${booking.tower}, ${booking.floor}: ${booking.date} ${booking.timeSlot}",
                                modifier = Modifier.padding(16.dp),
                                color = PrimaryBlue, fontSize = 14.sp, fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}