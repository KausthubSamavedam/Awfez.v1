package com.example.myapplicationoh.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.myapplicationoh.ui.components.SpaceTypeSelector
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
    val isPlumbing = formState.selectedCategory?.id == "plumbing"
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
                if (formState.selectedCategory?.id != "plumbing") {

                    SectionHeader("SELECT TYPE")
                    SpaceTypeSelector(
                        types = SpaceType.entries,
                        selectedType = formState.selectedSpaceType,
                        onTypeSelected = viewModel::onSpaceTypeSelected
                    )
                } else {
                    SectionHeader("LOCATION TYPE")
                    Text(
                        text = "🚻 Washroom / Wash Area",
                        color = TextSecondary
                    )
                }
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
                if (!isPlumbing) {
                    DropdownSelector(
                        label = "ROOM NAME",
                        value = formState.selectedRoom?.name ?: "",
                        options = formState.availableRooms.map { it.name },
                        onOptionSelected = { name ->
                            val room = formState.availableRooms.first { it.name == name }
                            viewModel.onRoomSelected(room)
                        },
                        placeholder = "Select room"
                    )
                }
                SectionHeader("DATE")
                Text(
                    text = formState.selectedDate,
                    fontSize = 14.sp,
                    color = TextPrimary
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