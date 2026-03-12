package com.example.myapplicationoh.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplicationoh.model.IssueStatus
import com.example.myapplicationoh.ui.components.*
import com.example.myapplicationoh.ui.theme.*
import com.example.myapplicationoh.viewmodel.AdminViewModel

@Composable
fun AdminIssueDetailScreen(
    issueId: String,
    viewModel: AdminViewModel,
    onBack: () -> Unit
) {
    val issue = viewModel.getIssueById(issueId)
    var selectedStatus by remember { mutableStateOf(issue?.status ?: IssueStatus.PENDING) }
    var selectedDept by remember { mutableStateOf(issue?.assignedTo?.ifEmpty { "Electrical" } ?: "Electrical") }
    var showDeptDialog by remember { mutableStateOf(false) }
    val departments = listOf("Electrical", "HVAC", "Plumbing", "IT", "Facilities", "Security")

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AdminDark)
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                }
                Column {
                    Text("Issue Detail", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(issue?.issueRef ?: issueId, fontSize = 12.sp, color = Color.White.copy(alpha = 0.6f))
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(Color.White)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Issue summary
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    issue?.title ?: "Issue",
                                    fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary
                                )
                                Text(issue?.location ?: "", fontSize = 13.sp, color = TextSecondary)
                            }
                            issue?.status?.let { IssueStatusChip(it) }
                        }
                        if (issue?.description?.isNotEmpty() == true) {
                            Spacer(Modifier.height(12.dp))
                            Text(
                                "\"${issue.description}\"",
                                fontSize = 14.sp, color = TextPrimary, fontStyle = FontStyle.Italic
                            )
                        }
                        if ((issue?.photosCount ?: 0) > 0) {
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "+ ${issue?.photosCount} photos attached",
                                fontSize = 12.sp, color = TextSecondary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                // Reported by
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "REPORTED BY", fontSize = 11.sp, fontWeight = FontWeight.SemiBold,
                            color = TextSecondary, letterSpacing = 0.8.sp
                        )
                        Spacer(Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier.size(36.dp).clip(CircleShape).background(PrimaryBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    issue?.reportedBy?.split(" ")?.mapNotNull { it.firstOrNull()?.toString() }?.joinToString("") ?: "AJ",
                                    color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(issue?.reportedBy ?: "Alex Johnson", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                Text(
                                    "${issue?.reportedByEmail ?: ""} · ${issue?.reportedDate ?: ""}",
                                    fontSize = 12.sp, color = TextSecondary
                                )
                            }
                        }
                    }
                }

                // Assign department
                var isAssigned by remember { mutableStateOf(issue?.assignedTo?.isNotEmpty() == true) }
                Column {
                    Text(
                        "ASSIGN DEPARTMENT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(Modifier.height(6.dp))
                    DropdownSelector(
                        label = "",
                        value = selectedDept,
                        options = departments,
                        onOptionSelected = { selectedDept = it }
                    )
                    Spacer(Modifier.height(8.dp))
                    if (!isAssigned) {
                        PrimaryButton(
                            text = "Assign",
                            onClick = {
                                viewModel.updateIssueAssignment(issueId, selectedDept)
                                isAssigned = true
                                selectedStatus = IssueStatus.ASSIGNED
                            }
                        )
                    }
                }

                // Update status
                Column {
                    Text(
                        "UPDATE STATUS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(IssueStatus.IN_PROGRESS, IssueStatus.RESOLVED).forEach { status ->
                            val isSelected = selectedStatus == status
                            OutlinedButton(
                                onClick = {
                                    if (isAssigned) selectedStatus = status
                                },
                                enabled = isAssigned,
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (isSelected) PrimaryBlue else Color.White,
                                    contentColor = if (isSelected) Color.White else PrimaryBlue
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(status.label)
                            }
                        }
                    }
                    if (!isAssigned) {
                        Text(
                            "Assign department first to update status",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }
                }
                PrimaryButton(
                    text = "Save & Notify User",
                    onClick = {
                        viewModel.updateIssueStatus(issueId, selectedStatus)
                        onBack()
                    }
                )
            }
        }
    }

    if (showDeptDialog) {
        AlertDialog(
            onDismissRequest = { showDeptDialog = false },
            title = { Text("Select Department", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    departments.forEachIndexed { idx, dept ->
                        Text(
                            dept,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            fontSize = 15.sp
                        )
                        if (idx < departments.size - 1) HorizontalDivider(color = DividerColor)
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showDeptDialog = false }) { Text("Cancel") }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}