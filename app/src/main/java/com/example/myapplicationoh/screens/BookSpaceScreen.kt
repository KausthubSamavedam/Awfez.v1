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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplicationoh.model.SpaceType
import com.example.myapplicationoh.model.TimeSlot
import com.example.myapplicationoh.ui.components.*
import com.example.myapplicationoh.ui.theme.*
import com.example.myapplicationoh.viewmodel.BookingViewModel

@Composable
fun BookSpaceScreen(
    viewModel: BookingViewModel,
    onBack: () -> Unit,
    onBookingConfirmed: (String) -> Unit,
    onBookingFailed: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            ScreenTopBar(
                title = "Book a Space",
                subtitle = "Reserve your workspace",
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
                SectionHeader("SELECT TYPE")
                TypeToggle(
                    leftLabel = SpaceType.MEETING_ROOM.displayName,
                    leftEmoji = SpaceType.MEETING_ROOM.emoji,
                    rightLabel = SpaceType.WORKSPACE.displayName,
                    rightEmoji = SpaceType.WORKSPACE.emoji,
                    isLeftSelected = state.selectedType == SpaceType.MEETING_ROOM,
                    onLeftClick = { viewModel.onTypeSelected(SpaceType.MEETING_ROOM) },
                    onRightClick = { viewModel.onTypeSelected(SpaceType.WORKSPACE) }
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
                        placeholder = "Select tower"
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
                        placeholder = "Select floor"
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

                if (state.availableTimeSlots.isNotEmpty()) {
                    SectionHeader("TIME SLOT")
                    TimeSlotGrid(
                        slots = state.availableTimeSlots,
                        selectedSlot = state.selectedTimeSlot,
                        onSlotSelected = viewModel::onTimeSlotSelected
                    )
                }

                Spacer(Modifier.height(8.dp))
                PrimaryButton(
                    text = "Book Space",
                    onClick = {
                        viewModel.bookSpace(
                            onSuccess = onBookingConfirmed,
                            onFailure = onBookingFailed
                        )
                    },
                    enabled = state.selectedRoom != null && state.selectedTimeSlot != null
                )
            }
        }
    }
}

@Composable
fun TimeSlotGrid(
    slots: List<TimeSlot>,
    selectedSlot: TimeSlot?,
    onSlotSelected: (TimeSlot) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        slots.chunked(3).forEach { rowSlots ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowSlots.forEach { slot ->
                    TimeSlotChip(
                        slot = slot,
                        isSelected = selectedSlot?.id == slot.id,
                        onSlotSelected = onSlotSelected,
                        modifier = Modifier.weight(1f)
                    )
                }
                // Fill remaining columns if row has less than 3
                repeat(3 - rowSlots.size) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun TimeSlotChip(
    slot: TimeSlot,
    isSelected: Boolean,
    onSlotSelected: (TimeSlot) -> Unit,
    modifier: Modifier = Modifier
) {
    val bg: Color
    val textColor: Color
    val borderColor: Color

    when {
        slot.isBooked -> {
            bg = TimeSlotDisabled
            textColor = TextHint
            borderColor = TimeSlotDisabled
        }
        isSelected -> {
            bg = TimeSlotSelected
            textColor = Color.White
            borderColor = TimeSlotSelected
        }
        else -> {
            bg = Color.White
            textColor = TextPrimary
            borderColor = DividerColor
        }
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bg)
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
            .clickable(enabled = !slot.isBooked) { onSlotSelected(slot) }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            slot.label,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
@Composable
fun SelectionDialog(
    title: String,
    items: List<String>,
    onSelect: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                items.forEachIndexed { idx, item ->
                    Text(
                        item,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(idx) }
                            .padding(vertical = 12.dp, horizontal = 4.dp),
                        fontSize = 15.sp
                    )
                    if (idx < items.size - 1) HorizontalDivider(color = DividerColor)
                }
            }
        },
        confirmButton = {},
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
        shape = RoundedCornerShape(16.dp)
    )
}