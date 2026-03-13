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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val categories = uiState.issueCategories
    val towers = uiState.towers
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            ScreenTopBar(
                title = "Report an Issue",
                subtitle = "Tell us what's wrong",
                onBack = onBack
            )
            HorizontalDivider(color = DividerColor)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DropdownSelector(
                    label = "CATEGORY",
                    value = formState.selectedCategory?.name ?: "",
                    options = categories.map { it.name },
                    onOptionSelected = { name ->
                        val cat = categories.first { it.name == name }
                        viewModel.onCategorySelected(cat)
                    },
                    placeholder = "Select category"
                )
                DropdownSelector(
                    label = "ISSUE",
                    value = formState.selectedIssueType?.name ?: "",
                    options = formState.availableIssueTypes.map { it.name },
                    onOptionSelected = { name ->
                        val type = formState.availableIssueTypes.first { it.name == name }
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
                    isLeftSelected = formState.selectedSpaceType == SpaceType.MEETING_ROOM,
                    onLeftClick = { viewModel.onSpaceTypeSelected(SpaceType.MEETING_ROOM) },
                    onRightClick = { viewModel.onSpaceTypeSelected(SpaceType.WORKSPACE) }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DropdownSelector(
                        label = "TOWER",
                        value = formState.selectedTower?.name ?: "",
                        options = towers.map { it.name },
                        onOptionSelected = { name ->
                            val tower = towers.first { it.name == name }
                            viewModel.onTowerSelected(tower)
                        },
                        modifier = Modifier.weight(1f),
                        placeholder = "Select"
                    )
                    DropdownSelector(
                        label = "FLOOR",
                        value = formState.selectedFloor?.name ?: "",
                        options = formState.availableFloors.map { it.name },
                        onOptionSelected = { name ->
                            val floor = formState.availableFloors.first { it.name == name }
                            viewModel.onFloorSelected(floor)
                        },
                        modifier = Modifier.weight(1f),
                        placeholder = "Select"
                    )
                }
                DropdownSelector(
                    label = "ROOM NUMBER",
                    value = formState.selectedRoom?.name ?: "",
                    options = formState.availableRooms.map { it.name },
                    onOptionSelected = { name ->
                        val room = formState.availableRooms.first { it.name == name }
                        viewModel.onRoomSelected(room)
                    },
                    placeholder = "Select room"
                )
                DropdownSelector(
                    label = "DATE",
                    value = formState.selectedDate,
                    options = listOf(
                        "Thursday, Mar 13, 2026",
                        "Friday, Mar 14, 2026",
                        "Monday, Mar 17, 2026",
                        "Tuesday, Mar 18, 2026"
                    ),
                    onOptionSelected = { viewModel.onDateSelected(it) }
                )
                SectionHeader("DESCRIPTION")
                OutlinedTextField(
                    value = formState.description,
                    onValueChange = viewModel::onDescriptionChange,
                    placeholder = {
                        Text(
                            "Describe the issue in detail...",
                            color = TextHint,
                            fontSize = 13.sp
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = DividerColor,
                        focusedBorderColor = PrimaryBlue
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    maxLines = 4
                )
                PrimaryButton(
                    text = "Submit Issue",
                    onClick = {
                        viewModel.submitIssue(onSuccess = onIssueSubmitted)
                    },
                    enabled = formState.selectedCategory != null &&
                            formState.selectedIssueType != null
                )
            }
        }
    }
}