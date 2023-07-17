package com.example.aquaquality.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aquaquality.R
import com.example.aquaquality.ui.theme.AquaqualityTheme

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.aquaquality_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(
                Color(0xFF006B59), blendMode = BlendMode.Hardlight
            )
        )
        Column(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            //Logo
            Box(
                modifier = Modifier.weight(1f)
            ) {
                val logo = ImageVector.vectorResource(id = R.drawable.aquaqualitylogo)
                Image(
                    imageVector = logo, contentDescription = stringResource(
                        id = R.string.app_name
                    ), modifier = Modifier
                        .size(250.dp)
                        .align(Alignment.BottomCenter)
                )
            }

            Box(
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color.White)
            }
        }
        Text(
            text = stringResource(id = R.string.credits),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = dimensionResource(id = R.dimen.padding_medium)),
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

@Preview
@Composable
fun LoadingScreenPreview() {
    AquaqualityTheme {
        LoadingScreen()
    }
}