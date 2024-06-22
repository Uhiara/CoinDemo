package com.example.coindemo.presentation.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit


@Composable
fun CommonText(
    modifier: Modifier,
    textValue: String,
    textSize: TextUnit,
    lineHeight: TextUnit,
    fontWeight: FontWeight,
    fontFamily: FontFamily,
    textAlign: TextAlign,
    colorValue: Color = Color.Black
) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = textValue,
        fontSize = textSize,
        lineHeight = lineHeight,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        textAlign = textAlign,
        color = colorValue,
    )
}