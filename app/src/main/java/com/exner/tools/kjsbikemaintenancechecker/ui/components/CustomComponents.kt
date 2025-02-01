package com.exner.tools.kjsbikemaintenancechecker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Component
import com.exner.tools.kjsbikemaintenancechecker.ui.destinations.convertMillisToDate
import com.exner.tools.kjsbikemaintenancechecker.ui.theme.Theme

@Composable
fun DefaultTextFieldWithSpacer(
    value: String = "",
    label: String = "",
    placeholder: String? = null,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val usablePlaceholder = placeholder ?: label

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = usablePlaceholder) },
        label = { Text(text = label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.Sentences
        )
    )
    DefaultSpacer()
}

@Composable
fun DefaultNumberFieldWithSpacer(
    value: String = "",
    label: String = "",
    placeholder: String? = null,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val usablePlaceholder = placeholder ?: label

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = usablePlaceholder) },
        label = { Text(text = label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        )
    )
    DefaultSpacer()
}

@Composable
fun TextAndSwitch(
    text: String,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?
) {
    ListItem(
        modifier = Modifier.fillMaxWidth(),
        headlineContent = {
            Text(
                text = text,
                maxLines = 3,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = Modifier
            )
        },
    )
}

@Composable
fun TextAndTriStateToggle(
    text: String,
    currentTheme: Theme,
    updateTheme: (Theme) -> Unit
) {
    val states = listOf(
        Theme.Auto,
        Theme.Dark,
        Theme.Light,
    )

    ListItem(
        headlineContent = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        trailingContent = {
            Surface(
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .wrapContentSize()
            ) {
                Row(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    states.forEach { thisTheme ->
                        Text(
                            text = thisTheme.name,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(50))
                                .clickable {
                                    updateTheme(thisTheme)
                                }
                                .background(
                                    if (thisTheme == currentTheme) {
                                        MaterialTheme.colorScheme.surfaceVariant
                                    } else {
                                        MaterialTheme.colorScheme.surface
                                    }
                                )
                                .padding(
                                    vertical = 8.dp,
                                    horizontal = 16.dp,
                                ),
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun DefaultSpacer() {
    Spacer(modifier = Modifier.size(16.dp))
}

@Composable
fun IconSpacer() {
    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
}

//@Composable fun DefaultDateField(
//
//) {
//    OutlinedTextField(
//        value = selectedBuildDate?.let { convertMillisToDate(it) } ?: "",
//        onValueChange = { },
//        label = { Text("Acquisition date") },
//        placeholder = { Text("YYYY-MM-DD") },
//        trailingIcon = {
//            Icon(Icons.Default.DateRange, contentDescription = "Select date")
//        },
//        modifier = Modifier
//            .fillMaxWidth()
//            .pointerInput(selectedBuildDate) {
//                awaitEachGesture {
//                    // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
//                    // in the Initial pass to observe events before the text field consumes them
//                    // in the Main pass.
//                    awaitFirstDown(pass = PointerEventPass.Initial)
//                    val upEvent =
//                        waitForUpOrCancellation(pass = PointerEventPass.Initial)
//                    if (upEvent != null) {
//                        showBuildDateModal = true
//                    }
//                }
//            }
//    )
//
//    if (showBuildDateModal) {
//        DatePickerModal(
//            onDateSelected = {
//                selectedBuildDate = it
//                if (it != null) {
//                    componentEditViewModel.updateAcquisitionDate(it)
//                }
//            },
//            onDismiss = { showBuildDateModal = false }
//        )
//    }
//    DefaultSpacer()
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            modifier = Modifier.verticalScroll(rememberScrollState()),
            dateFormatter = remember { DatePickerDefaults.dateFormatter() },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultBikeSelectorWithSpacer(
    value: String,
    label: String,
    onMenuItemClick: (Long?) -> Unit,
    bikes: List<Bike>
) {
    var bikeSelectorExpanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = bikeSelectorExpanded,
        onExpandedChange = { bikeSelectorExpanded = !bikeSelectorExpanded }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                // The `menuAnchor` modifier must be passed to the text field for correctness.
                modifier = Modifier
                    .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                    .fillMaxWidth()
                    .padding(8.dp),
                readOnly = true,
                value = value,
                onValueChange = {},
                label = { Text(text = label) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = bikeSelectorExpanded) },
            )
        }
        ExposedDropdownMenu(
            expanded = bikeSelectorExpanded,
            onDismissRequest = { bikeSelectorExpanded = false }) {
            DropdownMenuItem(
                text = { Text(text = "None") },
                onClick = {
                    onMenuItemClick(null)
                    bikeSelectorExpanded = false
                },
                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
            )
            bikes.forEach { bike ->
                DropdownMenuItem(
                    text = { Text(text = bike.name) },
                    onClick = {
                        onMenuItemClick(bike.uid)
                        bikeSelectorExpanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
    DefaultSpacer()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultParentComponentSelectorWithSpacer(
    value: String,
    label: String,
    onMenuItemClick: (Long?) -> Unit,
    components: List<Component>,
) {
    var parentComponentExpanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = parentComponentExpanded,
        onExpandedChange = { parentComponentExpanded = !parentComponentExpanded }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                // The `menuAnchor` modifier must be passed to the text field for correctness.
                modifier = Modifier
                    .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                    .fillMaxWidth()
                    .padding(8.dp),
                readOnly = true,
                value = value,
                onValueChange = {},
                label = { Text(text = label) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = parentComponentExpanded) },
            )
        }
        ExposedDropdownMenu(
            expanded = parentComponentExpanded,
            onDismissRequest = { parentComponentExpanded = false }) {
            DropdownMenuItem(
                text = { Text(text = "None") },
                onClick = {
                    onMenuItemClick(null)
                    parentComponentExpanded = false
                },
                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
            )
            components.forEach { component ->
                DropdownMenuItem(
                    text = { Text(text = component.name) },
                    onClick = {
                        onMenuItemClick(component.uid)
                        parentComponentExpanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
    DefaultSpacer()
}

@Composable
fun DefaultDateSelectorWithSpacer(
    selectedDate: Long?,
    label: String,
    placeholder: String?,
    onDateSelected: (Long?) -> Unit
) {
    val realPlaceholder = placeholder ?: label
    var showCreatedDateModal by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = selectedDate?.let { convertMillisToDate(it) } ?: "",
        onValueChange = { },
        label = { Text(text = label) },
        placeholder = { Text(text = realPlaceholder) },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select date")
        },
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                    // in the Initial pass to observe events before the text field consumes them
                    // in the Main pass.
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent =
                        waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showCreatedDateModal = true
                    }
                }
            }
    )

    if (showCreatedDateModal) {
        DatePickerModal(
            onDateSelected = {
                if (it != null) {
                    onDateSelected(it)
                }
            },
            onDismiss = { showCreatedDateModal = false }
        )
    }
    DefaultSpacer()
}

@Composable
fun DefaultDateSelectorNullableWithSpacer(
    selectedDate: Long?,
    label: String,
    placeholder: String?,
    onDateSelected: (Long?) -> Unit
) {
    val realPlaceholder = placeholder ?: label
    var showCreatedDateModal by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = selectedDate?.let { convertMillisToDate(it) } ?: "",
        onValueChange = { },
        label = { Text(text = label) },
        placeholder = { Text(text = realPlaceholder) },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select date")
        },
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                    // in the Initial pass to observe events before the text field consumes them
                    // in the Main pass.
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent =
                        waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showCreatedDateModal = true
                    }
                }
            }
    )

    if (showCreatedDateModal) {
        DatePickerModal(
            onDateSelected = {
                if (it != null) {
                    onDateSelected(it)
                }
            },
            onDismiss = { showCreatedDateModal = false }
        )
    }
    DefaultSpacer()
}
