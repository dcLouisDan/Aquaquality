package com.example.aquaquality.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aquaquality.R
import com.example.aquaquality.data.SettingsUiState
import com.example.aquaquality.ui.theme.AquaqualityTheme
import com.example.aquaquality.ui.viewmodels.SettingsViewModel
import androidx.compose.runtime.getValue

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = viewModel(),
    onSaveButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val settingsUiState: SettingsUiState by settingsViewModel.uiState.collectAsState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(dimensionResource(id = R.dimen.padding_small))
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))) {
                Text(
                    text = stringResource(R.string.title_app_settings),
                    style = MaterialTheme.typography.titleLarge
                )

                Divider(Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_large)))

                //Temperature
                Text(
                    text = stringResource(R.string.title_temperature_threshold),
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))
                ParameterSettingTextField(
                    value = settingsUiState.minTemp,
                    recommendedValue = stringResource(R.string.value_rec_min_temp),
                    onValueChange = {settingsViewModel.setMinTempInput(it)},
                    label = stringResource(R.string.label_min_temp)
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))
                ParameterSettingTextField(
                    value = settingsUiState.maxTemp,
                    recommendedValue = stringResource(R.string.value_rec_max_temp),
                    onValueChange = { settingsViewModel.setMaxTempInput(it) },
                    label = stringResource(R.string.label_max_temp)
                )

                Divider(Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_large)))

                //pH
                Text(
                    text = stringResource(R.string.title_ph_level_threshold),
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))
                ParameterSettingTextField(
                    value = settingsUiState.minPh,
                    recommendedValue = stringResource(R.string.value_rec_min_ph),
                    onValueChange = {settingsViewModel.setMinPhInput(it)},
                    label = stringResource(R.string.label_min_ph)
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))
                ParameterSettingTextField(
                    value = settingsUiState.maxPh,
                    recommendedValue = stringResource(R.string.value_rec_max_ph),
                    onValueChange = {settingsViewModel.setMaxPhInput(it)},
                    label = stringResource(R.string.label_max_ph)
                )

                Divider(Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_large)))

                //Turbidity
                Text(
                    text = stringResource(R.string.title_turbidity_threshold),
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))
                ParameterSettingTextField(
                    value = settingsUiState.minTurb,
                    recommendedValue = stringResource(R.string.value_rec_min_turb),
                    onValueChange = { settingsViewModel.setMinTurbInput(it) },
                    label = stringResource(R.string.label_min_turb)
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))
                ParameterSettingTextField(
                    value = settingsUiState.maxTurb,
                    recommendedValue = stringResource(R.string.value_rec_max_turb),
                    onValueChange = {settingsViewModel.setMaxTurbInput(it)},
                    label = stringResource(R.string.label_max_turb)
                )

//                Divider(Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_large)))
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_xl)))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(onClick = onSaveButtonClick) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = stringResource(
                                    id = R.string.label_save_changes
                                ),
                                modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_small))
                            )
                            Text(text = stringResource(R.string.label_save_changes))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ParameterSettingTextField(
    value: String,
    recommendedValue: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String
) {
    TextField(
        value = value,
        label = {
            Text(text = label)
        },
        placeholder = {
            Text(text = "")
        },
        supportingText = {
            Text(text = "Recommended value: $recommendedValue")
        },
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            textColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
    )
}


@Preview
@Composable
fun SettingsPreview() {
    AquaqualityTheme {
        SettingsScreen(
            onSaveButtonClick = {},
        )
    }
}