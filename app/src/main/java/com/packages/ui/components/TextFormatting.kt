package com.packages.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PaddedText(text: String, modifier: Modifier = Modifier, style: TextStyle = MaterialTheme.typography.bodyMedium, fontWeight: FontWeight = FontWeight.Normal){
    Text(text = text, style = style, modifier = modifier.padding(start = 16.dp, bottom = 3.dp, top = 3.dp), fontWeight = fontWeight)
}

@Composable
fun PaddedCardText(text: String, modifier: Modifier = Modifier, style: TextStyle = MaterialTheme.typography.bodyMedium, fontWeight: FontWeight = FontWeight.Normal){
    Text(text = text, style = style, modifier = Modifier.padding(start = 8.dp, top = 3.dp), fontWeight = fontWeight)
}