package com.example.aquaquality.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.aquaquality.ui.theme.AquaqualityTheme
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.entryModelOf

@Composable
fun FishpondScreen(modifier: Modifier = Modifier) {
    val chartEntryModel = entryModelOf(22, 23,23,30,20)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Chart(
            chart = lineChart(),
            model = chartEntryModel,
            startAxis = startAxis(),
            bottomAxis = bottomAxis()
        )
    }

}

@Preview
@Composable
fun FishpondScreenPreview() {
    AquaqualityTheme {
        FishpondScreen()
    }
}