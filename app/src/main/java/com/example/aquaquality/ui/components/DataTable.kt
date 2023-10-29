package com.example.aquaquality.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aquaquality.R
import com.example.aquaquality.ui.theme.AquaqualityTheme

@Composable
fun DataTable(
    columnItems: List<Any>,
    rowItems: List<List<Any>>,
    modifier: Modifier = Modifier,
    @StringRes chartTitle: Int? = null,
    headerContentColor: Color = MaterialTheme.colorScheme.onPrimary,
    headerBackgroundColor: Color = MaterialTheme.colorScheme.primary,
    elevation: CardElevation = CardDefaults.cardElevation(),
    bodyBackgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    bodyContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    ) {
    if (chartTitle != null) {
        Text(
            text = stringResource(id = chartTitle),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(id = R.dimen.padding_small))
        )
    }
    Card(
        modifier = modifier.width(330.dp)
            .sizeIn(maxHeight = 400.dp)
            .verticalScroll(rememberScrollState()),
        elevation = elevation,
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(headerBackgroundColor)
                .height(IntrinsicSize.Min)
        ) {
            columnItems.forEachIndexed { index, item ->
                Text(
                    text = item.toString(),
                    color = headerContentColor,
                    modifier = Modifier
                        .padding(
                            dimensionResource(id = R.dimen.padding_small)
                        )
                        .weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
                if (index != columnItems.size - 1) {
                    Divider(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight()
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            rowItems.forEachIndexed { index, item ->
                Column(
                    Modifier
                        .background(bodyBackgroundColor)
                        .weight(1f)
                ) {
                    item.forEachIndexed { index, item ->
                        Text(
                            text = item.toString(),
                            color = bodyContentColor,
                            modifier = Modifier
                                .padding(
                                    dimensionResource(id = R.dimen.padding_small)
                                )
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        if (index != columnItems.size - 1) {
                            Divider()
                        }
                    }
                }
                if (index != columnItems.size - 1) {
                    Divider(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    AquaqualityTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(dimensionResource(id = R.dimen.padding_large))
        ) {

            DataTable(
                columnItems = listOf("Time", "Value"),
                rowItems = listOf(listOf("1pm", "2pm"), listOf(100, 200)),
                headerContentColor = MaterialTheme.colorScheme.onPrimary,
                headerBackgroundColor = MaterialTheme.colorScheme.primary,
                bodyBackgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                bodyContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            )
        }
    }
}