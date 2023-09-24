package com.example.aquaquality.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.aquaquality.R
import com.example.aquaquality.ui.theme.AquaqualityTheme

@Composable
fun ReferencesScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        Text(
            text = "Water Quality Guide",
            textAlign = TextAlign.Right,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
//        Divider()
        Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))) {
            Text(
                text = "Temperature",
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
            Text(
                text = "The optimal water temperature range for aquaculture is 28℃ - 32℃.",
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
            Text(
                text = "\tFish are cold-blooded animals, but their body temperature changes depending on the temperature of the water around them. This affects their metabolism and physiology, which can impact their health and productivity.",
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
            Text(
                text = "\tWhen the water temperature increases, the fish's metabolism and respiration rate also increase. This means that they need more oxygen to survive. Moreover, the amount of oxygen that can dissolve in water decreases as the temperature increases. This can lead to oxygen levels in the water becoming too low for the fish to survive.",
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
            Text(
                text = "\tLow temperature can reduce the feed intake and growth rate of catfish. Catfish need a warm water temperature of about 26°C to 32°C to digest their food well and grow fast. Low temperature can also change the water quality in the fishponds. Low temperature can reduce the oxygen level and increase the ammonia level in the water. Oxygen is needed by catfish to breathe and by bacteria to break down wastes.",
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_xl)))
            Text(
                text = "pH",
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
            Text(
                text = "The desirable pH level range for freshwater fishes in the Philippines is 6.5 - 8.5.",
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
            Text(
                text = "\tThe pH of water is a measure of how acidic or alkaline it is. Most fish prefer water with a pH between 6.5 and 9.0. Water with a pH that is too low or too high can harm fishes. When the pH of water is too low, it can damage the fish's gills and make it difficult for them to breathe. It can also make the water more toxic to fish. When the pH of water is too high, it can make the water less oxygenated, which can suffocate the fish. It can also make the water more alkaline, which can damage the fish's skin and eyes.",
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
            Text(
                text = "\tThe pH of water can be affected by a number of factors, including the type of rocks and soil in the water, the amount of sunlight the water receives, and the type of plants and animals that live in the water.",
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_xl)))
            Text(
                text = "Turbidity",
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
            Text(
                text = "The recommended water turbidity range for fishponds is 0 - 180 NTU.",
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
            Text(
                text = "\tIf the turbidity of the water in the pond is too high, it can block the sunlight from reaching the plants and algae in the water. These plants and algae are important for producing oxygen and food for the fish. Without enough sunlight, they cannot grow well and may die. This reduces the oxygen level and the food supply for the fish.",
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
            Text(
                text = "\tHigh water turbidity can also carry harmful substances, such as sediments, chemicals, bacteria, and parasites, that can damage the gills, skin, eyes, and internal organs of the fish. These substances can also reduce the quality of the water and make it more acidic or alkaline. This can stress the fish and make them more vulnerable to diseases and infections.",
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_xl)))
        }

    }
}

@Preview
@Composable
fun ReferencesScreenPreview() {
    AquaqualityTheme() {
        ReferencesScreen(modifier = Modifier.background(color = Color.White))
    }
}