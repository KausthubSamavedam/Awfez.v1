package com.example.myapplicationoh.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplicationoh.model.Booking
import com.example.myapplicationoh.model.BookingStatus
import com.example.myapplicationoh.ui.components.BookingStatusChip
import com.example.myapplicationoh.ui.components.FilterChip
import com.example.myapplicationoh.ui.components.ScreenTopBar
import com.example.myapplicationoh.ui.theme.*
import com.example.myapplicationoh.viewmodel.BookingViewModel

@Composable
fun MyBookingsScreen(
    viewModel: BookingViewModel,
    onHome: () -> Unit,
    onMyIssues: () -> Unit,
    onBack: () -> Unit
) {
    // observe bookings state from ViewModel (Firestore reactive)
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val bookings = state.myBookings
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Upcoming", "Today", "Past")
    var selectedTab by remember { mutableStateOf(1) }
    val filtered = when (selectedFilter) {
        "Today" -> bookings.filter { it.status == BookingStatus.TODAY }
        "Upcoming" -> bookings.filter {
            it.status == BookingStatus.TOMORROW ||
                    it.status == BookingStatus.UPCOMING
        }
        "Past" -> bookings.filter { it.status == BookingStatus.COMPLETED }
        else -> bookings
    }
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
                            when (idx) {
                                0 -> onHome()
                                2 -> onMyIssues()
                            }
                        },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryBlue,
                            selectedTextColor = PrimaryBlue,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary,
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
                .background(Color.White)
                .padding(paddingValues)
        ) {
            ScreenTopBar(title = "My Bookings", onBack = onBack)
            HorizontalDivider(color = DividerColor)
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filters.forEach { f ->
                    FilterChip(
                        label = f,
                        isSelected = selectedFilter == f,
                        onClick = { selectedFilter = f }
                    )
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundLight)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(filtered) { booking ->
                    BookingCard(booking)
                }
            }
        }
    }
}
@Composable
private fun BookingCard(booking: Booking) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        booking.roomName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "${booking.tower}, ${booking.floor}",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }
                BookingStatusChip(booking.status)
            }
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("📅", fontSize = 12.sp)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        booking.date,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🕐", fontSize = 12.sp)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        booking.timeSlot,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}