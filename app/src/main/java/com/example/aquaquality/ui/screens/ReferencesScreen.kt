package com.example.aquaquality.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aquaquality.R
import com.example.aquaquality.ui.theme.AquaqualityTheme

@Composable
fun ReferencesScreen(modifier: Modifier = Modifier) {

    var isTempExpanded by rememberSaveable {
        mutableStateOf(false)
    }
    var isPhExpanded by rememberSaveable {
        mutableStateOf(false)
    }
    var isTurbExpanded by rememberSaveable {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.inverseOnSurface,
            ),
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_medium), vertical = dimensionResource(
                id = R.dimen.padding_small
            ))

        ) {
            Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.label_temperature),
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .weight(1f),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.Bold
                        )
                    IconButton(onClick = { isTempExpanded = !isTempExpanded}) {
                        Icon(
                            imageVector = if(!isTempExpanded) Icons.Default.ExpandMore else Icons.Default.ExpandLess,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                AnimatedVisibility(visible = isTempExpanded) {
                    Column {
                        Divider(Modifier.padding(dimensionResource(id = R.dimen.padding_medium)))
                        Text(
                            text = stringResource(R.string.statement_optimal_temp),
                            textAlign = TextAlign.Justify,
                            modifier = Modifier
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 25.sp
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
                        Text(
                            text = stringResource(R.string.temp_paragraph_1),
                            textAlign = TextAlign.Justify,
                            modifier = Modifier
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 25.sp
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
                        Text(
                            text = stringResource(R.string.temp_paragraph_2),
                            textAlign = TextAlign.Justify,
                            modifier = Modifier
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 25.sp
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
                        Text(
                            text = stringResource(R.string.temp_paragraph_3),
                            textAlign = TextAlign.Justify,
                            modifier = Modifier
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 25.sp
                        )
                    }
                }
            }
        }
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.inverseOnSurface,
            ),
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_medium), vertical = dimensionResource(
                id = R.dimen.padding_small
            ))

        ) {
            Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = R.string.label_pH),
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .weight(1f),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight =FontWeight.Bold
                    )
                    IconButton(onClick = { isPhExpanded = !isPhExpanded}) {
                        Icon(
                            imageVector = if(!isPhExpanded) Icons.Default.ExpandMore else Icons.Default.ExpandLess,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                AnimatedVisibility(visible = isPhExpanded){
                    Column {
                        Divider(Modifier.padding(dimensionResource(id = R.dimen.padding_medium)))
                        Text(
                            text = stringResource(R.string.statement_optimal_ph),
                            textAlign = TextAlign.Justify,
                            modifier = Modifier
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 25.sp
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
                        Text(
                            text = stringResource(R.string.ph_paragraph_1),
                            textAlign = TextAlign.Justify,
                            modifier = Modifier
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 25.sp
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
                        Text(
                            text = stringResource(R.string.ph_paragraph_2),
                            textAlign = TextAlign.Justify,
                            modifier = Modifier
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 25.sp
                        )
                    }
                }
            }
        }
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.inverseOnSurface,
            ),
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_medium), vertical = dimensionResource(
                id = R.dimen.padding_small
            ))

        ) {
            Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))) {
                Row (verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = R.string.label_turbidity),
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .weight(1f),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(onClick = { isTurbExpanded = !isTurbExpanded}) {
                        Icon(
                            imageVector = if(!isTurbExpanded) Icons.Default.ExpandMore else Icons.Default.ExpandLess,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                AnimatedVisibility(visible = isTurbExpanded){
                    Column {
                        Divider(Modifier.padding(dimensionResource(id = R.dimen.padding_medium)))
                        Text(
                            text = stringResource(R.string.statement_optimal_turb),
                            textAlign = TextAlign.Justify,
                            modifier = Modifier
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 25.sp
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
                        Text(
                            text = stringResource(R.string.turb_paragraph_1),
                            textAlign = TextAlign.Justify,
                            modifier = Modifier
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 25.sp
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
                        Text(
                            text = stringResource(R.string.turb_paragraph_2),
                            textAlign = TextAlign.Justify,
                            modifier = Modifier
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 25.sp
                        )
                    }
                }
            }
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