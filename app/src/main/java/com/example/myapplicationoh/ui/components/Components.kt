package com.example.myapplicationoh.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplicationoh.model.BookingStatus
import com.example.myapplicationoh.model.IssueStatus
import com.example.myapplicationoh.model.SpaceType
import com.example.myapplicationoh.ui.theme.DividerColor
import com.example.myapplicationoh.ui.theme.PrimaryBlue
import com.example.myapplicationoh.ui.theme.StatusAssigned
import com.example.myapplicationoh.ui.theme.StatusInProgress
import com.example.myapplicationoh.ui.theme.StatusPending
import com.example.myapplicationoh.ui.theme.StatusResolved
import com.example.myapplicationoh.ui.theme.StatusToday
import com.example.myapplicationoh.ui.theme.StatusTomorrow
import com.example.myapplicationoh.ui.theme.TextHint
import com.example.myapplicationoh.ui.theme.TextPrimary
import com.example.myapplicationoh.ui.theme.TextSecondary


@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = PrimaryBlue
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            disabledContainerColor = color.copy(alpha = 0.5f)
        ),
        modifier = modifier.fillMaxWidth().height(56.dp)
    ) {
        Text(text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
    }
}

@Composable
fun LabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    isPassword: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold,
            color = TextSecondary, letterSpacing = 0.8.sp
        )
        Spacer(Modifier.height(6.dp))
        if (isPassword) {
            OutlinedTextField(
                value = value, onValueChange = onValueChange,
                placeholder = { Text(placeholder, color = TextHint) },
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = DividerColor, focusedBorderColor = PrimaryBlue,
                    unfocusedContainerColor = Color(0xFFF8F9FA), focusedContainerColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth(), singleLine = true
            )
        } else {
            OutlinedTextField(
                value = value, onValueChange = onValueChange,
                placeholder = { Text(placeholder, color = TextHint) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = DividerColor, focusedBorderColor = PrimaryBlue,
                    unfocusedContainerColor = Color(0xFFF8F9FA), focusedContainerColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth(), singleLine = true
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(
    label: String,
    value: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Select..."
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        if (label.isNotEmpty()) {
            Text(
                label,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary,
                letterSpacing = 0.8.sp
            )
            Spacer(Modifier.height(6.dp))
        }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = value.ifEmpty { placeholder },
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = DividerColor,
                    focusedBorderColor = PrimaryBlue,
                    unfocusedContainerColor = Color(0xFFF8F9FA),
                    focusedContainerColor = Color.White,
                    unfocusedTextColor = if (value.isEmpty()) TextHint else TextPrimary,
                    focusedTextColor = TextPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                if (options.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("No options available", color = TextHint, fontSize = 14.sp) },
                        onClick = { expanded = false }
                    )
                } else {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    option,
                                    fontSize = 15.sp,
                                    color = if (option == value) PrimaryBlue else TextPrimary,
                                    fontWeight = if (option == value) FontWeight.SemiBold else FontWeight.Normal
                                )
                            },
                            onClick = {
                                onOptionSelected(option)
                                expanded = false
                            },
                            trailingIcon = {
                                if (option == value) {
                                    Icon(
                                        Icons.Default.Info,
                                        contentDescription = null,
                                        tint = PrimaryBlue,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun SpaceTypeSelector(
    selectedType: SpaceType,
    onTypeSelected: (SpaceType) -> Unit
) {

    val types = SpaceType.entries

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        items(types) { type ->

            val selected = type == selectedType

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (selected) PrimaryBlue else Color.White)
                    .clickable { onTypeSelected(type) }
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {

                Text(
                    "${type.emoji} ${type.displayName}",
                    color = if (selected) Color.White else TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )

            }

        }

    }
}
@Composable
fun SectionHeader(title: String, modifier: Modifier = Modifier) {
    Text(
        title, fontSize = 11.sp, fontWeight = FontWeight.Bold,
        letterSpacing = 1.2.sp, color = TextSecondary,
        modifier = modifier.padding(bottom = 12.dp)
    )
}

@Composable
fun IssueStatusChip(status: IssueStatus) {
    val bg: Color
    val textColor: Color
    when (status) {
        IssueStatus.PENDING -> {
            bg = Color(0xFFFFF3E0)
            textColor = StatusPending
        }
        IssueStatus.ASSIGNED -> {
            bg = Color(0xFFE3F2FD)
            textColor = StatusAssigned
        }
        IssueStatus.IN_PROGRESS -> {
            bg = Color(0xFFEEF2FF)
            textColor = StatusInProgress
        }
        IssueStatus.RESOLVED -> {
            bg = Color(0xFFE8F5E9)
            textColor = StatusResolved
        }
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(status.label, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun BookingStatusChip(status: BookingStatus) {
    val bg: Color
    val textColor: Color
    when (status) {
        BookingStatus.TODAY -> {
            bg = Color(0xFFEEF2FF)
            textColor = StatusToday
        }
        BookingStatus.TOMORROW -> {
            bg = Color(0xFFFFF3E0)
            textColor = StatusTomorrow
        }
        BookingStatus.UPCOMING -> {
            bg = Color(0xFFE8F5E9)
            textColor = StatusResolved
        }
        BookingStatus.COMPLETED -> {
            bg = Color(0xFFE8F5E9)
            textColor = StatusResolved
        }
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(status.label, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}
@Composable
fun InfoRow(label: String, value: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = TextSecondary, fontSize = 14.sp)
        Text(value, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun DetailCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) { content() }
    }
}

@Composable
fun TypeToggle(
    leftLabel: String, leftEmoji: String,
    rightLabel: String, rightEmoji: String,
    isLeftSelected: Boolean,
    onLeftClick: () -> Unit, onRightClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, DividerColor, RoundedCornerShape(12.dp))
    ) {
        TypeToggleButton(leftLabel, leftEmoji, isLeftSelected, onLeftClick, Modifier.weight(1f))
        TypeToggleButton(rightLabel, rightEmoji, !isLeftSelected, onRightClick, Modifier.weight(1f))
    }
}

@Composable
private fun TypeToggleButton(
    label: String, emoji: String,
    isSelected: Boolean, onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) Color.White else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(emoji, fontSize = 16.sp)
            Spacer(Modifier.width(6.dp))
            Text(
                label,
                color = if (isSelected) PrimaryBlue else TextSecondary,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}

@Composable
fun QuickActionCard(
    title: String, subtitle: String, emoji: String,
    onClick: () -> Unit, modifier: Modifier = Modifier,
    bgColor: Color = Color(0xFFF0F4FF)
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(bgColor),
                contentAlignment = Alignment.Center
            ) { Text(emoji, fontSize = 20.sp) }
            Spacer(Modifier.height(10.dp))
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(Modifier.height(2.dp))
            Text(subtitle, fontSize = 12.sp, color = TextSecondary)
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Icon(Icons.Default.Add, contentDescription = null, tint = TextHint, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun FilterChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) PrimaryBlue else Color.Transparent)
            .border(1.dp, if (isSelected) PrimaryBlue else DividerColor, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            label,
            color = if (isSelected) Color.White else TextSecondary,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
fun ScreenTopBar(title: String, subtitle: String = "", onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, "Back", tint = TextPrimary)
        }
        Spacer(Modifier.width(8.dp))
        Column {
            Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            if (subtitle.isNotEmpty()) Text(subtitle, fontSize = 13.sp, color = TextSecondary)
        }
    }
}