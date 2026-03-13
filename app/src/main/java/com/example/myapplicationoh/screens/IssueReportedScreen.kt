package com.example.myapplicationoh.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplicationoh.ui.components.DetailCard
import com.example.myapplicationoh.ui.components.InfoRow
import com.example.myapplicationoh.ui.components.PrimaryButton
import com.example.myapplicationoh.ui.theme.*
import com.example.myapplicationoh.viewmodel.IssueViewModel

@Composable
fun IssueReportedScreen(
    issueRef: String,
    viewModel: IssueViewModel,
    onBackToHome: () -> Unit
) {

    // Observe state from ViewModel
    val formState by viewModel.formState.collectAsState()

    val issue = formState.lastSubmittedIssue

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
                .background(Color(0xFFEEF2FF)),
            contentAlignment = Alignment.Center
        ) {
            Text("📋", fontSize = 38.sp)
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Issue Reported!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Our facilities team has been notified. You can track the status of your issue at any time.",
            fontSize = 14.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        DetailCard {

            InfoRow("Issue ID", issue?.issueRef ?: issueRef)

            HorizontalDivider(color = DividerColor)

            InfoRow(
                "Category",
                issue?.category ?: "Electrical / Lighting"
            )

            HorizontalDivider(color = DividerColor)

            InfoRow(
                "Issue",
                issue?.title ?: "Light Not Working"
            )

            HorizontalDivider(color = DividerColor)

            InfoRow(
                "Location",
                issue?.let { "${it.tower} – ${it.floor}" } ?: "Tower B – Floor 3"
            )

            HorizontalDivider(color = DividerColor)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Status",
                    color = TextSecondary,
                    fontSize = 14.sp
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFFFF3E0))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {

                    Text(
                        text = "Pending",
                        color = StatusPending,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                }

            }

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
                    text = "ESTIMATED RESPONSE TIME",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary,
                    letterSpacing = 0.8.sp
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Within 24 hours",
                    fontSize = 20.sp,
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

    }
}