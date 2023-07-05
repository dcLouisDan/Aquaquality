package com.example.aquaquality.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.aquaquality.R


enum class IndicatorStatus{
    NORMAL,
    OVER_RANGE,
    UNDER_RANGE,
}

@Composable
fun ParameterMonitor(
    imageVector: ImageVector,
    parameterLabel: Int,
    parameterValue: String,
    modifier: Modifier = Modifier,
    parameterValueFormat: Int,
    indicatorStatus: IndicatorStatus? = IndicatorStatus.NORMAL
) {

    val monitorTheme = when (indicatorStatus) {
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
        else -> MonitorTheme(
            backgroundColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
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

private data class MonitorTheme(
    val backgroundColor: Color,
    val contentColor: Color
)