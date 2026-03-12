package com.example.myapplicationoh.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplicationoh.model.SpaceType
import com.example.myapplicationoh.ui.components.*
import com.example.myapplicationoh.ui.theme.*
import com.example.myapplicationoh.viewmodel.IssueViewModel


@Composable
fun ReportIssueScreen(
    viewModel: IssueViewModel,
    onBack: () -> Unit,
    onIssueSubmitted: (String) -> Unit
) {
    val state by viewModel.formState.collectAsState()
    var showCategoryDialog by remember { mutableStateOf(false) }
    var showTypeDialog by remember { mutableStateOf(false) }
    var showTowerDialog by remember { mutableStateOf(false) }
    var showFloorDialog by remember { mutableStateOf(false) }
    var showRoomDialog by remember { mutableStateOf(false) }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            ScreenTopBar(title = "Report an Issue", subtitle = "Tell us what's wrong", onBack = onBack)
            HorizontalDivider(color = DividerColor)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // CATEGORY dropdown
                DropdownSelector(
                    label = "CATEGORY",
                    value = state.selectedCategory?.name ?: "",
                    options = viewModel.issueCategories.map { it.name },
                    onOptionSelected = { name ->
                        val cat = viewModel.issueCategories.first { it.name == name }
                        viewModel.onCategorySelected(cat)
                    },
                    placeholder = "Select category"
                )

                // ISSUE TYPE dropdown
                DropdownSelector(
                    label = "ISSUE",
                    value = state.selectedIssueType?.name ?: "",
                    options = state.availableIssueTypes.map { it.name },
                    onOptionSelected = { name ->
                        val type = state.availableIssueTypes.first { it.name == name }
                        viewModel.onIssueTypeSelected(type)
                    },
                    placeholder = "Select issue type"
                )

                SectionHeader("SELECT TYPE")
                TypeToggle(
                    leftLabel = SpaceType.MEETING_ROOM.displayName,
                    leftEmoji = SpaceType.MEETING_ROOM.emoji,
                    rightLabel = SpaceType.WORKSPACE.displayName,
                    rightEmoji = SpaceType.WORKSPACE.emoji,
                    isLeftSelected = state.selectedSpaceType == SpaceType.MEETING_ROOM,
                    onLeftClick = { viewModel.onSpaceTypeSelected(SpaceType.MEETING_ROOM) },
                    onRightClick = { viewModel.onSpaceTypeSelected(SpaceType.WORKSPACE) }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DropdownSelector(
                        label = "TOWER",
                        value = state.selectedTower?.name ?: "",
                        options = viewModel.towers.map { it.name },
                        onOptionSelected = { name ->
                            val tower = viewModel.towers.first { it.name == name }
                            viewModel.onTowerSelected(tower)
                        },
                        modifier = Modifier.weight(1f),
                        placeholder = "Select"
                    )
                    DropdownSelector(
                        label = "FLOOR",
                        value = state.selectedFloor?.name ?: "",
                        options = state.availableFloors.map { it.name },
                        onOptionSelected = { name ->
                            val floor = state.availableFloors.first { it.name == name }
                            viewModel.onFloorSelected(floor)
                        },
                        modifier = Modifier.weight(1f),
                        placeholder = "Select"
                    )
                }

                DropdownSelector(
                    label = "ROOM NUMBER",
                    value = state.selectedRoom?.name ?: "",
                    options = state.availableRooms.map { it.name },
                    onOptionSelected = { name ->
                        val room = state.availableRooms.first { it.name == name }
                        viewModel.onRoomSelected(room)
                    },
                    placeholder = "Select room"
                )

                DropdownSelector(
                    label = "DATE",
                    value = state.selectedDate,
                    options = listOf(
                        "Thursday, Mar 13, 2026",
                        "Friday, Mar 14, 2026",
                        "Monday, Mar 17, 2026",
                        "Tuesday, Mar 18, 2026"
                    ),
                    onOptionSelected = { viewModel.onDateSelected(it) }
                )

                Column {
                    SectionHeader("DESCRIPTION")
                    OutlinedTextField(
                        value = state.description,
                        onValueChange = viewModel::onDescriptionChange,
                        placeholder = {
                            Text(
                                "Describe the issue in detail...",
                                color = TextHint, fontSize = 13.sp
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = DividerColor,
                            focusedBorderColor = PrimaryBlue
                        ),
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        maxLines = 4
                    )
                }

                Column {
                    SectionHeader("UPLOAD A PHOTO")
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, DividerColor, RoundedCornerShape(12.dp))
                            .background(Color(0xFFF8F9FA))
                            .clickable { }
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("📷", fontSize = 28.sp)
                            Spacer(Modifier.height(8.dp))
                            Text("Tap to upload a photo", fontSize = 14.sp, color = TextSecondary)
                            Text("JPG, PNG up to 10MB", fontSize = 12.sp, color = TextHint)
                        }
                    }
                }

                PrimaryButton(
                    text = "Submit Issue",
                    onClick = { viewModel.submitIssue(onSuccess = onIssueSubmitted) },
                    enabled = state.selectedCategory != null && state.selectedIssueType != null
                )
            }
        }
    }


    if (showCategoryDialog) {
        SelectionDialog(
            title = "Select Category",
            items = viewModel.issueCategories.map { it.name },
            onSelect = { idx -> viewModel.onCategorySelected(viewModel.issueCategories[idx]); showCategoryDialog = false },
            onDismiss = { showCategoryDialog = false }
        )
    }
    if (showTypeDialog && state.availableIssueTypes.isNotEmpty()) {
        SelectionDialog(
            title = "Select Issue Type",
            items = state.availableIssueTypes.map { it.name },
            onSelect = { idx -> viewModel.onIssueTypeSelected(state.availableIssueTypes[idx]); showTypeDialog = false },
            onDismiss = { showTypeDialog = false }
        )
    }
    if (showTowerDialog) {
        SelectionDialog(
            title = "Select Tower",
            items = viewModel.towers.map { it.name },
            onSelect = { idx -> viewModel.onTowerSelected(viewModel.towers[idx]); showTowerDialog = false },
            onDismiss = { showTowerDialog = false }
        )
    }
    if (showFloorDialog) {
        SelectionDialog(
            title = "Select Floor",
            items = state.availableFloors.map { it.name },
            onSelect = { idx -> viewModel.onFloorSelected(state.availableFloors[idx]); showFloorDialog = false },
            onDismiss = { showFloorDialog = false }
        )
    }
    if (showRoomDialog) {
        SelectionDialog(
            title = "Select Room",
            items = state.availableRooms.map { it.name },
            onSelect = { idx -> viewModel.onRoomSelected(state.availableRooms[idx]); showRoomDialog = false },
            onDismiss = { showRoomDialog = false }
        )
    }
}