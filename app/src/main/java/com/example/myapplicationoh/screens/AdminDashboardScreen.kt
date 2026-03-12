package com.example.myapplicationoh.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplicationoh.model.Issue
import com.example.myapplicationoh.ui.components.*
import com.example.myapplicationoh.ui.theme.*
import com.example.myapplicationoh.viewmodel.AdminViewModel

@Composable
fun AdminDashboardScreen(
    viewModel: AdminViewModel,
    onIssueClick: (String) -> Unit,
    onLogout: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AdminDark)
                .padding(paddingValues)
        ) {

            // HEADER
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(PrimaryBlue)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            "ADMIN",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(
                        "Issues Dashboard",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        "All reported facility issues",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }

                IconButton(onClick = onLogout) {
                    Icon(
                        Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        tint = Color.White
                    )
                }
            }

            // STATS
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                StatBox(viewModel.pendingCount(), "PENDING", StatusPending, Modifier.weight(1f))
                StatBox(viewModel.assignedCount(), "ASSIGNED", Color.White, Modifier.weight(1f))
                StatBox(viewModel.inProgressCount(), "IN PROGRESS", StatusInProgress, Modifier.weight(1f))
                StatBox(viewModel.resolvedCount(), "RESOLVED", StatusResolved, Modifier.weight(1f))
            }

            Spacer(Modifier.height(12.dp))

            // WHITE PANEL
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(Color.White)
            ) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BackgroundLight)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {

                    items(viewModel.filteredIssues(uiState.filterStatus)) { issue ->

                        AdminIssueCard(
                            issue = issue,
                            onClick = { onIssueClick(issue.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatBox(
    count: Int,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(AdminCard)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                count.toString(),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )

            Spacer(Modifier.height(2.dp))

            Text(
                label,
                fontSize = 9.sp,
                color = Color.White.copy(alpha = 0.5f),
                letterSpacing = 0.4.sp
            )
        }
    }
}

@Composable
private fun AdminIssueCard(
    issue: Issue,
    onClick: () -> Unit
) {

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {

                Column(modifier = Modifier.weight(1f)) {

                    Text(
                        issue.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Text(
                        issue.location,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )

                    Text(
                        "${issue.reportedBy} · ${issue.reportedDate}",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )

                    Text(
                        issue.issueRef,
                        fontSize = 11.sp,
                        color = TextHint
                    )
                }

                IssueStatusChip(issue.status)
            }
        }
    }
}