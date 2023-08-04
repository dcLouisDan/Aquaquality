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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aquaquality.R
import com.example.aquaquality.data.SuggestionInfo
import com.example.aquaquality.data.SuggestionType
import com.example.aquaquality.ui.theme.AquaqualityTheme


val highTempTreatments = listOf(
    "This is a sentence that narrates a possible treatment solution that the farmer can implement in order to manage the fishpond’s high water temperature.",
    "This is also a sentence that narrates another possible treatment solution that the farmer can implement in order to manage the fishpond’s high water temperature."
)
val highTempConsequences = listOf(
    "This is a paragraph that narrates a possible consequence that might happen if the water temperature is left unmanaged.",
    "This is also another paragraph that narrates a possible consequence that might happen if the water temperature is left unmanaged."
)

val highTempSuggestionInfo = SuggestionInfo(
    suggestionType = SuggestionType.HIGH,
    headline = "The water temperature has exceeded the recommended range.",
    solutionList = highTempTreatments,
    consequenceList = highTempConsequences
)

val lowPhSuggestionInfo = SuggestionInfo(
    suggestionType = SuggestionType.LOW,
    headline = "The ph level is below the recommended range.",
    solutionList = highTempTreatments,
    consequenceList = highTempConsequences
)


@Composable
fun SuggestionScreen(
    suggestionInfo: SuggestionInfo,
    onCloseButtonClick: () -> Unit = {},
) {
    val colorScheme = when (suggestionInfo.suggestionType) {
        SuggestionType.LOW -> SuggestionScreenColors(
            iconAndHeadlineColor = MaterialTheme.colorScheme.tertiary,
            labelColor = MaterialTheme.colorScheme.onTertiaryContainer,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )

        SuggestionType.HIGH -> SuggestionScreenColors(
            iconAndHeadlineColor = MaterialTheme.colorScheme.error,
            labelColor = MaterialTheme.colorScheme.onErrorContainer,
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.containerColor)
            .padding(dimensionResource(id = R.dimen.padding_xl))
            .verticalScroll(rememberScrollState())
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Icon(
                imageVector = Icons.Filled.Error,
                contentDescription = null,
                tint = colorScheme.iconAndHeadlineColor,
                modifier = Modifier.size(60.dp)
            )
            IconButton(onClick = onCloseButtonClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    modifier = Modifier.size(35.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))

        Text(
            text = suggestionInfo.headline,
            style = MaterialTheme.typography.headlineSmall,
            color = colorScheme.iconAndHeadlineColor
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_large)))

        Text(
            text = "Possible treatment solutions:",
            style = MaterialTheme.typography.labelLarge,
            color = colorScheme.labelColor
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))

        for ((index, solution) in suggestionInfo.solutionList.withIndex()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "${index + 1}.")
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_small)))
                Text(text = solution)
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_large)))

        Text(
            text = "Possible consequences if left unmanaged:",
            style = MaterialTheme.typography.labelLarge,
            color = colorScheme.labelColor
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))

        for ((index, consequence) in suggestionInfo.consequenceList.withIndex()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "${index + 1}.")
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_small)))
                Text(text = consequence)
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))
        }
    }
}

@Preview
@Composable
fun SuggestionScreenPreview() {
    AquaqualityTheme {
        SuggestionScreen(
            suggestionInfo = highTempSuggestionInfo,
            onCloseButtonClick = {},
        )
    }
}
@Preview
@Composable
fun SuggestionScreenPreview2() {
    AquaqualityTheme {
        SuggestionScreen(
            suggestionInfo = lowPhSuggestionInfo,
            onCloseButtonClick = {},
        )
    }
}

data class SuggestionScreenColors(
    val iconAndHeadlineColor: Color,
    val labelColor: Color,
    val containerColor: Color
)