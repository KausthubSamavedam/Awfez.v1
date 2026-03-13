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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplicationoh.model.SpaceType
import com.example.myapplicationoh.model.TimeSlot
import com.example.myapplicationoh.ui.components.*
import com.example.myapplicationoh.ui.theme.*
import com.example.myapplicationoh.viewmodel.BookingViewModel
import com.example.myapplicationoh.ui.components.SpaceTypeSelector
@Composable
fun BookSpaceScreen(
    viewModel: BookingViewModel,
    onBack: () -> Unit,
    onBookingConfirmed: (String) -> Unit,
    onBookingFailed: () -> Unit
) {
    // Observe state safely from ViewModel (Firestore reactive)
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val towers = state.towers
    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetBookingForm()
        }
    }
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

                SpaceTypeSelector(
                    selectedType = state.selectedType,
                    onTypeSelected = viewModel::onTypeSelected
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DropdownSelector(
                        label = "TOWER",
                        value = state.selectedTower?.name ?: "",
                        options = towers.map { it.name },
                        onOptionSelected = { name ->
                            val tower = towers.first { it.name == name }
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
                    label = "ROOM NAME",
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
                            onSuccess = {
                                viewModel.resetBookingForm()
                                onBookingConfirmed(it)
                            },
                            onFailure = onBookingFailed
                        )
                    },
                    enabled = state.selectedRoom != null &&
                            state.selectedTimeSlot != null
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
    val background =
        if (slot.isBooked) TimeSlotDisabled
        else if (isSelected) TimeSlotSelected
        else Color.White
    val textColor =
        if (slot.isBooked) TextHint
        else if (isSelected) Color.White
        else TextPrimary
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(background)
            .border(1.dp, DividerColor, RoundedCornerShape(10.dp))
            .clickable(enabled = !slot.isBooked) { onSlotSelected(slot) }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = slot.label,
            color = textColor,
            fontSize = 12.sp
        )
    }
}
