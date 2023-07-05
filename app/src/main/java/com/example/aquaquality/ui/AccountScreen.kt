package com.example.aquaquality.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AccountScreen(modifier: Modifier = Modifier)
{
    Box(modifier = modifier.fillMaxSize()) {
        Text(text = "This is the Account Screen", modifier = Modifier.align(Alignment.Center))
    }
}