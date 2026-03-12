package com.example.myapplicationoh.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplicationoh.model.Issue
import com.example.myapplicationoh.model.IssueStatus
import com.example.myapplicationoh.ui.components.*
import com.example.myapplicationoh.ui.theme.*
import com.example.myapplicationoh.viewmodel.IssueViewModel

@Composable
fun MyIssuesScreen(
    viewModel: IssueViewModel,
    onHome: () -> Unit,
    onMyBookings: () -> Unit,
    onBack: () -> Unit,
    onAddIssue: () -> Unit
) {
    val issues = viewModel.myIssues
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Pending", "In Progress", "Resolved")
    var selectedTab by remember { mutableStateOf(2) }

    val filtered = when (selectedFilter) {
        "Pending" -> issues.filter { it.status == IssueStatus.PENDING }
        "In Progress" -> issues.filter { it.status == IssueStatus.IN_PROGRESS }
        "Resolved" -> issues.filter { it.status == IssueStatus.RESOLVED }
        else -> issues
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddIssue,
                containerColor = PrimaryBlue,
                contentColor = Color.White,
                shape = CircleShape
            ) { Icon(Icons.Default.Add, "Add Issue") }
        },
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
                                1 -> onMyBookings()
                            }
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
                .background(Color.White)
                .padding(paddingValues)
        ) {
            ScreenTopBar(title = "My Issues", onBack = onBack)
            HorizontalDivider(color = DividerColor)

            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filters.forEach { f ->
                    FilterChip(label = f , isSelected = selectedFilter == f, onClick = { selectedFilter = f })
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
                items(filtered) { issue -> IssueCard(issue) }
            }
        }
    }
}

@Composable
fun IssueCard(issue: Issue) {
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
                    Text(issue.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(Modifier.height(2.dp))
                    Text(issue.location, fontSize = 13.sp, color = TextSecondary)
                    Spacer(Modifier.height(2.dp))
                    Text("Reported ${issue.reportedDate}", fontSize = 12.sp, color = TextHint)
                }
                IssueStatusChip(issue.status)
            }
        }
    }
}
