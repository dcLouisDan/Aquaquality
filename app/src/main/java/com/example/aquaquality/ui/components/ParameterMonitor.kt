package com.example.aquaquality.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aquaquality.R
import com.example.aquaquality.ui.theme.AquaqualityTheme


enum class IndicatorStatus {
    NORMAL,
    OVER_RANGE,
    UNDER_RANGE,
    OFFLINE
}

@Composable
fun ParameterMonitor(
    imageVector: ImageVector,
    parameterLabel: Int,
    parameterValue: String,
    modifier: Modifier = Modifier,
    parameterValueFormat: Int,
    stringIndicatorStatus: String = "NORMAL"
) {
    val monitorTheme = when (IndicatorStatus.valueOf(stringIndicatorStatus)) {
        IndicatorStatus.NORMAL -> MonitorTheme(
            backgroundColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )

        IndicatorStatus.OVER_RANGE -> MonitorTheme(
            backgroundColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError
        )

        IndicatorStatus.UNDER_RANGE -> MonitorTheme(
            backgroundColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        )

        IndicatorStatus.OFFLINE -> MonitorTheme(
            backgroundColor = MaterialTheme.colorScheme.onSurfaceVariant,
            contentColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(monitorTheme.backgroundColor)
                .padding(dimensionResource(id = R.dimen.padding_small)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                tint = monitorTheme.contentColor
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_small)))
            Text(
                text = stringResource(parameterLabel),
                color = monitorTheme.contentColor,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(parameterValueFormat, parameterValue),
                color = monitorTheme.contentColor,
                style = MaterialTheme.typography.bodyLarge,
            )
        }

    }
}

@Composable
fun ParameterMonitorLarge(
    imageVector: ImageVector,
    parameterLabel: Int,
    parameterValue: String,
    modifier: Modifier = Modifier,
    parameterValueFormat: Int,
    stringIndicatorStatus: String = "NORMAL"
) {
    val monitorTheme = when (IndicatorStatus.valueOf(stringIndicatorStatus)) {
        IndicatorStatus.NORMAL -> MonitorTheme(
            contentColor = MaterialTheme.colorScheme.primary,
            backgroundColor = MaterialTheme.colorScheme.onPrimary
        )

        IndicatorStatus.OVER_RANGE -> MonitorTheme(
            contentColor = MaterialTheme.colorScheme.error,
            backgroundColor = MaterialTheme.colorScheme.onError
        )

        IndicatorStatus.UNDER_RANGE -> MonitorTheme(
            contentColor = MaterialTheme.colorScheme.tertiary,
            backgroundColor = MaterialTheme.colorScheme.onTertiary
        )

        IndicatorStatus.OFFLINE -> MonitorTheme(
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
//                .background(
//                    MaterialTheme.colorScheme.surfaceVariant
//                )
            ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .clip(shape = RoundedCornerShape(50.dp))
                    .width(50.dp)
                    .height(50.dp)
                    .background(monitorTheme.contentColor),
            ) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                    tint = monitorTheme.backgroundColor,
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.Center)
                )
            }

            Row(
                Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(parameterLabel),
                    color = monitorTheme.contentColor,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(parameterValueFormat, parameterValue),
                    color = monitorTheme.contentColor,
                    textAlign = TextAlign.Right,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }

    }
}

private data class MonitorTheme(
    val backgroundColor: Color,
    val contentColor: Color
)

@Preview
@Composable
private fun IndicatorPreview() {
    AquaqualityTheme {
        ParameterMonitorLarge(
            imageVector = Icons.Default.Thermostat,
            parameterLabel = R.string.label_temperature,
            parameterValue = "33",
            parameterValueFormat = R.string.parameter_temperature,
            stringIndicatorStatus = "UNDER_RANGE"
        )
    }
}