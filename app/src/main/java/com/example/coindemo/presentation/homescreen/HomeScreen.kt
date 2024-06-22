package com.example.coindemo.presentation.homescreen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coindemo.R
import com.example.coindemo.presentation.common.CommonText

@Composable
fun HomeScreen(
    state: HomeScreenState,
    onEvent: (HomeScreenEvent) -> Unit
) {

    val keys = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "0", "C")

    Surface(
        Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            CommonText(
                modifier = Modifier.fillMaxWidth(),
                textValue = "CoinXChange",
                textSize = 32.sp,
                lineHeight = 4.sp,
                fontWeight = FontWeight(700),
                fontFamily = FontFamily(Font(R.font.roboto_bold)),
                colorValue = colorResource(id = R.color.teal),
                textAlign = TextAlign.Center
            )

            Card(
                Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    CurrencyRow(
                        modifier = Modifier.fillMaxWidth(),
                        currencyCode = state.fromCurrencyCode,
                        currencyName = state.currencyRates[state.fromCurrencyCode]?.name ?: "",
                        onDroppedIconClicked = {}
                    )
                    Text(
                        text = state.fromCurrencyValue,
                        fontSize = 40.sp,
                        fontFamily = FontFamily.SansSerif,
                        color = colorResource(id = R.color.text),
                        modifier = Modifier.clickable {
                            onEvent(HomeScreenEvent.FromCurrencySelect)
                        }
                    )
                }
            }

            Card(
                Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = state.toCurrencyValue,
                        fontSize = 40.sp,
                        fontFamily = FontFamily.SansSerif,
                        color = colorResource(id = R.color.text),
                        modifier = Modifier.clickable {
                            onEvent(HomeScreenEvent.ToCurrencySelect)
                        }
                    )
                    CurrencyRow(
                        modifier = Modifier.fillMaxWidth(),
                        currencyCode = state.toCurrencyCode,
                        currencyName = state.currencyRates[state.toCurrencyCode]?.name ?: "",
                        onDroppedIconClicked = {}
                    )
                }
            }

            LazyVerticalGrid(
                modifier = Modifier.padding(horizontal = 35.dp),
                columns = GridCells.Fixed(3)
            ) {
                items(keys) { key ->
                    KeyboardButton(
                        modifier = Modifier.aspectRatio(1f),
                        key = key,
                        backgroundColor = if (key == "C") MaterialTheme.colorScheme.surfaceVariant
                        else colorResource(id = R.color.teal),
                        onClick = {
                            onEvent(HomeScreenEvent.NumberButtonClicked(key))
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun CurrencyRow(
    modifier: Modifier = Modifier,
    currencyCode: String,
    currencyName: String,
    onDroppedIconClicked: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = currencyCode,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            color = colorResource(id = R.color.teal),
        )
        IconButton(onClick = onDroppedIconClicked) {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Open Bottom Sheet",
                tint = colorResource(id = R.color.teal),
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = currencyName,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            color = colorResource(id = R.color.teal),
        )
    }
}

@Composable
fun KeyboardButton(
    modifier: Modifier = Modifier,
    key: String,
    backgroundColor: Color,
    onClick: (String) -> Unit
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .clip(CircleShape)
            .background(color = backgroundColor)
            .clickable { onClick(key) },
        contentAlignment = Alignment.Center
    ) {
        Text(text = key, fontSize = 32.sp)
    }
}


@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun OnBoardingScreenPreview() {
    HomeScreen(state = HomeScreenState()){}
}