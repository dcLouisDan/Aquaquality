package com.example.aquaquality.ui.screens

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DeviceThermostat
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.SensorsOff
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Water
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aquaquality.R
import com.example.aquaquality.data.FishpondInfo
import com.example.aquaquality.ui.components.IndicatorStatus
import com.example.aquaquality.ui.components.ParameterMonitor
import com.example.aquaquality.ui.theme.AquaqualityTheme
import com.example.aquaquality.ui.theme.rememberChartStyle
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.entryModelOf
import java.util.Calendar


private val months = listOf(
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December"
)

@ExperimentalMaterial3Api
@Composable
fun FishpondScreen(fishpondInfo: FishpondInfo, modifier: Modifier = Modifier) {
    val isConnected = true

    val sheetState = rememberModalBottomSheetState()
//    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val mContext = LocalContext.current

    // Declaring integer values
    // for year, month and day
    val mYear: Int
    val mMonth: Int
    val mDay: Int

    // Initializing a Calendar
    val mCalendar = Calendar.getInstance()

    // Fetching current year, month and day
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)


    // Declaring a string value to
    // store date in string format
    val mDate = remember { mutableStateOf("${months[mMonth]} $mDay, $mYear") }

    // Declaring DatePickerDialog and setting
    // initial values as current values (present year, month and day)
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "${months[mMonth]} $mDayOfMonth, $mYear"
        }, mYear, mMonth, mDay
    )


    Scaffold() { contentPadding ->
        Box(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(contentPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.padding_medium))
                ) {
                    Text(
                        text = stringResource(R.string.label_name),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(text = fishpondInfo.name, style = MaterialTheme.typography.titleLarge)
                    Divider(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_medium)))

                    if (isConnected) {
                        Text(
                            text = "Connected Device:",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))

                        var isDeviceMenuVisible by remember {
                            mutableStateOf(false)
                        }

                        var isDeviceOnline = false

                        val deviceIcon =
                            if (isDeviceOnline) Icons.Default.Devices else Icons.Default.SensorsOff

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = deviceIcon, contentDescription = null)
                            Text(
                                text = "Device 1",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                            IconButton(
                                onClick = {
                                    isDeviceMenuVisible = !isDeviceMenuVisible
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    contentColor = MaterialTheme.colorScheme.onSurface,
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = null
                                )
                                DropdownMenu(
                                    expanded = isDeviceMenuVisible,
                                    onDismissRequest = { isDeviceMenuVisible = false },

                                    ) {
                                    DropdownMenuItem(
                                        text = { Text(text = "Disconnect") },
                                        onClick = {}
                                    )
                                }
                            }
                        }
                        Divider(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_medium)))
                        IndicatorList(fishpondInfo = fishpondInfo)
                        Divider(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_medium)))

                        Text(
                            text = "View Data From:",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))

                        OutlinedButton(
                            onClick = {
                                mDatePickerDialog.show()
                            },
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(dimensionResource(id = R.dimen.padding_small)),
                            border = BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.outline
                                )
                                Text(
                                    text = mDate.value,
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Center
                                )
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))

                        DataGraph(chartTitle = R.string.label_wUnit_temperature)
                        Divider(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_medium)))

                        DataGraph(chartTitle = R.string.label_wUnit_pH)
                        Divider(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_medium)))

                        DataGraph(chartTitle = R.string.label_wUnit_turbidity)
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_large)))
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = stringResource(R.string.message_no_device),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
                            )

                            OutlinedButton(onClick = {
                                showBottomSheet = true
                            }) {
                                Text(text = stringResource(R.string.label_add_device))
                            }
                        }
                    }
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                tonalElevation = 5.dp,
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surface,
                scrimColor = Color.Black.copy(alpha = 0.5f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(700.dp)
                        .padding(dimensionResource(id = R.dimen.padding_medium))
                ) {
                    Text(
                        text = "Connect to a device",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Divider(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_medium)))
                }
            }
        }
    }
}

@Composable
fun IndicatorList(fishpondInfo: FishpondInfo, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        ParameterMonitor(
            Icons.Default.Thermostat,
            R.string.label_temperature,
            fishpondInfo.tempValue.toString(),
            parameterValueFormat = R.string.parameter_temperature,
            indicatorStatus = IndicatorStatus.OVER_RANGE
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.height_xs)))
        ParameterMonitor(
            Icons.Default.Science,
            R.string.label_pH,
            fishpondInfo.phValue.toString(),
            parameterValueFormat = R.string.parameter_pH
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.height_xs)))
        ParameterMonitor(
            Icons.Default.Water,
            R.string.label_turbidity,
            fishpondInfo.turbidityValue.toString(),
            parameterValueFormat = R.string.parameter_turbidity,
            indicatorStatus = IndicatorStatus.UNDER_RANGE
        )
    }
}

@Composable
fun DataGraph(@StringRes chartTitle: Int, modifier: Modifier = Modifier) {
    val hoursOfTheDay = listOf(
        "0 AM",
        "1 AM",
        "2 AM",
        "3 AM",
        "4 AM",
        "5 AM",
        "6 AM",
        "7 AM",
        "8 AM",
        "9 AM",
        "10 AM",
        "11 AM",
        "12 NN",
        "1 PM",
        "2 PM",
        "3 PM",
        "4 PM",
        "5 PM",
        "6 PM",
        "7 PM",
        "8 PM",
        "9 PM",
        "10 PM",
        "11 PM",
    )
    val bottomAxisValueFormatter =
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { x, _ -> hoursOfTheDay[x.toInt() % hoursOfTheDay.size] }

    val chartEntryModel = entryModelOf(
        12,
        23,
        23,
        30,
        20,
        22,
        23,
        13,
        15,
        15,
        10,
        3,
        23,
        30,
        20,
        22,
        23,
        23,
        7,
        9,
        11,
        13,
        18,
        30,
        20,
    )

    Column(modifier = modifier) {
        Text(
            text = stringResource(id = chartTitle),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(id = R.dimen.padding_small))
        )


        ProvideChartStyle(rememberChartStyle(chartColors = listOf(MaterialTheme.colorScheme.primary))) {
            Chart(
                chart = lineChart(),
                model = chartEntryModel,
                startAxis = startAxis(
                ),
                bottomAxis = bottomAxis(
                    valueFormatter = bottomAxisValueFormatter,
                    labelRotationDegrees = 90f
                ),
            )
        }
    }
}


@Preview
@Composable
fun FishpondScreenPreview() {
    AquaqualityTheme {
        val fishpondInfo = FishpondInfo(
            id = 1,
            name = "Fishpond 1"
        )
        FishpondScreen(fishpondInfo)
    }
}