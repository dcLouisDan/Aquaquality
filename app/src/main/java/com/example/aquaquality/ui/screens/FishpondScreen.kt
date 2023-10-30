package com.example.aquaquality.ui.screens

import android.app.DatePickerDialog
import android.graphics.Typeface
import android.os.Build
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.SensorsOff
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aquaquality.R
import com.example.aquaquality.data.ConnectingStatus
import com.example.aquaquality.data.FishpondInfo
import com.example.aquaquality.data.FishpondScreenUiState
import com.example.aquaquality.ui.components.DataTable
import com.example.aquaquality.ui.components.DisconnectDeviceDialog
import com.example.aquaquality.ui.components.IndicatorStatus
import com.example.aquaquality.ui.components.ParameterMonitor
import com.example.aquaquality.ui.components.SegmentedControl
import com.example.aquaquality.ui.components.rememberMarker
import com.example.aquaquality.ui.theme.AquaqualityTheme
import com.example.aquaquality.ui.theme.rememberChartStyle
import com.example.aquaquality.ui.viewmodels.FishpondScreenViewModel
import com.example.aquaquality.utilities.DateHelper
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.dimensions.MutableDimensions
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private val months = DateHelper.months

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalMaterial3Api
@Composable
fun FishpondScreen(
    modifier: Modifier = Modifier,
    fishpondScreenViewModel: FishpondScreenViewModel = viewModel(),
    uiState: FishpondScreenUiState,
) {
    val sheetState = rememberModalBottomSheetState()
//    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var showDisconnectDialog by remember { mutableStateOf(false) }
    val mContext = LocalContext.current


    // Declaring a string value to
    // store date in string format
    val mDate = remember {
        mutableStateOf(
            "${DateHelper.months[uiState.month]} ${uiState.day}, ${uiState.year}"
        )
    }

    // Declaring DatePickerDialog and setting
    // initial values as current values (present year, month and day)
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, dYear: Int, dMonth: Int, dDayOfMonth: Int ->
            mDate.value = "${months[dMonth]} $dDayOfMonth, $dYear"
            fishpondScreenViewModel.setSelectedDate(dYear, dMonth, dDayOfMonth)
        }, uiState.year, uiState.month, uiState.day
    )


    Scaffold { contentPadding ->
        Box(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .fillMaxSize()
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
                    uiState.fishpondInfo?.name?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    uiState.fishpondInfo?.desc?.let {
                        if (uiState.fishpondInfo.desc.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
                            Text(
                                text = stringResource(R.string.label_description),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    Divider(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_medium)))

                    if (uiState.fishpondInfo?.connectedDeviceId != null) {
                        Text(
                            text = "Connected Device:",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))

                        var isDeviceMenuVisible by remember {
                            mutableStateOf(false)
                        }


                        val deviceIcon =
                            if (!uiState.fishpondInfo.isOffline!!) Icons.Default.Devices else Icons.Default.SensorsOff

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = deviceIcon, contentDescription = null)
                            Text(
                                text = uiState.deviceInfo.name!!,
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
                                        onClick = {
                                            showDisconnectDialog = true
                                        }
                                    )
                                }
                            }
                        }
                        Divider(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_medium)))
                        IndicatorList(fishpondInfo = uiState.fishpondInfo)

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
                    var dataRepresentationIndex by rememberSaveable {
                        mutableStateOf(0)
                    }
                    var previousDataRepresentationIndex by rememberSaveable {
                        mutableStateOf(0)
                    }
                    SegmentedControl(
                        items = listOf("Tables", "Charts"),
                        modifier = Modifier.fillMaxWidth(),
                        useFixedWidth = true,
                        onItemSelection = { index ->
                            previousDataRepresentationIndex = dataRepresentationIndex
                            dataRepresentationIndex = index
                        })
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))
                    if (uiState.timeList.isNotEmpty()) {

                        val transitionToRight =
                            slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) with slideOutHorizontally(
                                targetOffsetX = { fullWidth -> -fullWidth })
                        val transitionToLeft =
                            slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth }) with slideOutHorizontally(
                                targetOffsetX = { fullWidth -> fullWidth })

                        AnimatedContent(
                            targetState = dataRepresentationIndex,
                            transitionSpec = {
                                if (previousDataRepresentationIndex < dataRepresentationIndex) {
                                    transitionToRight
                                } else {
                                    transitionToLeft
                                }
                            }
                        ) { targetState ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                when (targetState) {
                                    0 -> DataTableList(uiState)
                                    1 -> DataGraphList(uiState, fishpondScreenViewModel)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_large)))
                    } else {
                        Text(
                            text = stringResource(R.string.message_no_data),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(dimensionResource(id = R.dimen.padding_medium))
                        )
                    }
                }
            }
        }
//        val context = LocalContext.current

        if (showDisconnectDialog) {
            DisconnectDeviceDialog(name = uiState.deviceInfo.name, onConfirmClick = {
                fishpondScreenViewModel.disconnectDeviceFromFishpond(uiState.deviceInfo)
                showDisconnectDialog = false
            }, onDismissRequest = { showDisconnectDialog = false })
        }


        val coroutineScope = rememberCoroutineScope()

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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Connect to a device",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { fishpondScreenViewModel.getDeviceList() }) {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                        }
                    }
                    Divider(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_medium)))
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(uiState.deviceList) { device ->
                            var connectingStatus by remember {
                                mutableStateOf(ConnectingStatus.AVAILABLE)
                            }

                            if (device.isAvailable!! && device.fishpondId == null) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(dimensionResource(id = R.dimen.padding_medium))
                                        .clickable {
                                            coroutineScope.launch {
                                                connectingStatus = ConnectingStatus.LOADING
                                                delay(1000)
                                                fishpondScreenViewModel.connectDeviceToFishpond(
                                                    device
                                                )
                                                if (uiState.isConnectionSuccess) {
                                                    connectingStatus = ConnectingStatus.CONNECTED
                                                    delay(2000)
                                                }
                                            }
                                            showBottomSheet = false
                                        },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Devices,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_medium)))
                                    Text(
                                        text = device.name!!,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_medium)))
                                    when (connectingStatus) {
                                        ConnectingStatus.AVAILABLE -> Icon(
                                            imageVector = Icons.Default.Wifi,
                                            contentDescription = null
                                        )

                                        ConnectingStatus.LOADING -> CircularProgressIndicator(
                                            modifier = Modifier.size(
                                                16.dp
                                            )
                                        )

                                        ConnectingStatus.CONNECTED -> Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun DataGraphList(
    uiState: FishpondScreenUiState,
    fishpondScreenViewModel: FishpondScreenViewModel
) {
    DataGraph(
        chartTitle = R.string.label_wUnit_temperature,
        timeList = uiState.timeList,
        valueList = uiState.tempValueList,
        chartEntryModelProducer = fishpondScreenViewModel.tempChartEntryModelProducer
    )
    Divider(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_medium)))

    DataGraph(
        chartTitle = R.string.label_wUnit_pH,
        timeList = uiState.timeList,
        valueList = uiState.phValueList,
        chartEntryModelProducer = fishpondScreenViewModel.phChartEntryModelProducer
    )
    Divider(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_medium)))

    DataGraph(
        chartTitle = R.string.label_wUnit_turbidity,
        timeList = uiState.timeList,
        valueList = uiState.turbidityValueList,
        chartEntryModelProducer = fishpondScreenViewModel.turbidityChartEntryModelProducer
    )
}

@Composable
private fun DataTableList(
    uiState: FishpondScreenUiState
) {
    DataTable(
        columnItems = listOf(
            stringResource(R.string.label_time),
            stringResource(id = R.string.label_wUnit_temperature)
        ),
        rowItems = listOf(uiState.timeList, uiState.tempValueList)
    )
    Divider(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_medium)))
    DataTable(
        columnItems = listOf(
            stringResource(R.string.label_time),
            stringResource(id = R.string.label_wUnit_pH)
        ),
        rowItems = listOf(uiState.timeList, uiState.phValueList)
    )
    Divider(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_medium)))
    DataTable(
        columnItems = listOf(
            stringResource(R.string.label_time),
            stringResource(id = R.string.label_wUnit_pH)
        ),
        rowItems = listOf(uiState.timeList, uiState.turbidityValueList)
    )
}

@Composable
fun IndicatorList(fishpondInfo: FishpondInfo, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        val isOffline = fishpondInfo.isOffline!!
        ParameterMonitor(
            Icons.Default.Thermostat,
            R.string.label_temperature,
            if (isOffline) "--" else fishpondInfo.tempValue.toString(),
            parameterValueFormat = R.string.parameter_temperature,
            stringIndicatorStatus = if (isOffline) IndicatorStatus.OFFLINE.name else fishpondInfo.tempStatus!!
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.height_xs)))
        ParameterMonitor(
            Icons.Default.Science,
            R.string.label_pH,
            if (isOffline) "--" else fishpondInfo.phValue.toString(),
            parameterValueFormat = R.string.parameter_pH,
            stringIndicatorStatus = if (isOffline) IndicatorStatus.OFFLINE.name else fishpondInfo.phStatus!!
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.height_xs)))
        ParameterMonitor(
            Icons.Default.Water,
            R.string.label_turbidity,
            if (isOffline) "--" else fishpondInfo.turbidityValue.toString(),
            parameterValueFormat = R.string.parameter_turbidity,
            stringIndicatorStatus = if (isOffline) IndicatorStatus.OFFLINE.name else fishpondInfo.turbStatus!!
        )
    }
}

@Composable
fun DataGraph(
    @StringRes chartTitle: Int,
    timeList: List<String>,
    valueList: List<Number>,
    modifier: Modifier = Modifier,
    chartEntryModelProducer: ChartEntryModelProducer,
) {
    val entries: List<FloatEntry> = mutableListOf()

    for ((index, value) in valueList.withIndex()) {
        val entry = FloatEntry(x = index.toFloat(), y = value.toFloat())
        entries.plus(entry)
    }

    val bottomAxisValueFormatter =
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { x, _ -> timeList[x.toInt() % timeList.size] }

    Column(modifier = modifier) {
        Text(
            text = stringResource(id = chartTitle),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(id = R.dimen.padding_small))
        )
        val marker = rememberMarker()

        ProvideChartStyle(rememberChartStyle(chartColors = listOf(MaterialTheme.colorScheme.primary))) {
            Chart(
                chart = lineChart(),
                marker = marker,
                chartModelProducer = chartEntryModelProducer,
                startAxis = startAxis(
                    title = "Values",
                    titleComponent = textComponent(
                        color = MaterialTheme.colorScheme.onTertiary,
                        background = shapeComponent(
                            Shapes.pillShape,
                            MaterialTheme.colorScheme.tertiary
                        ),
                        padding = axisTitlePadding,
                        margins = startAxisTitleMargins,
                        typeface = Typeface.MONOSPACE,
                    ),
                    label = textComponent(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                ),
                bottomAxis = bottomAxis(
                    titleComponent = textComponent(
                        color = MaterialTheme.colorScheme.onPrimary,
                        background = shapeComponent(
                            Shapes.pillShape,
                            MaterialTheme.colorScheme.primary
                        ),
                        padding = axisTitlePadding,
                        margins = startAxisTitleMargins,
                        typeface = Typeface.MONOSPACE,
                    ),
                    valueFormatter = bottomAxisValueFormatter,
                    labelRotationDegrees = 90f,
                    label = textComponent(
                        color = MaterialTheme.colorScheme.onSurface,
                        margins = MutableDimensions(16f, 8f),
                    ),

                    ),

                )
        }
    }
}

private val axisTitleHorizontalPaddingValue = 8.dp
private val axisTitleVerticalPaddingValue = 8.dp
private val axisTitlePadding =
    dimensionsOf(axisTitleHorizontalPaddingValue, axisTitleVerticalPaddingValue)
private val axisTitleMarginValue = 8.dp
private val startAxisTitleMargins = dimensionsOf(end = axisTitleMarginValue)


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun FishpondScreenPreview() {
    AquaqualityTheme {
        //FishpondScreen ViewModel
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_large))
        ) {
            SegmentedControl(
                items = listOf("Data Tables", "Data Charts"),
                onItemSelection = {},
                cornerRadius = 50,
                modifier = Modifier.fillMaxWidth(),
                useFixedWidth = true
            )
        }
    }
}