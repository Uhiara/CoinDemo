package com.example.coindemo.presentation.homescreen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeScreenState,
    onEvent: (HomeScreenEvent) -> Unit
) {

    val keys = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "0", "C")

    val context = LocalContext.current

    val interactionSource = remember { MutableInteractionSource() }

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var shouldBottomSheetShow by remember {
        mutableStateOf(false)
    }

    if (shouldBottomSheetShow) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { shouldBottomSheetShow = false },
            dragHandle = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BottomSheetDefaults.DragHandle()
                    Text(
                        text = "Select Currency",
                        fontSize = 40.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.text),
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider()
                }
            },
            content = {
                BottomSheetContent(
                    onItemClicked = { currencyCode ->
                        onEvent(HomeScreenEvent.BottomSheetItemClicked(currencyCode))
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) shouldBottomSheetShow = false
                        }
                    },
                    currenciesList = state.currencyRates.values.toList()
                )
            })
    }

    LaunchedEffect(key1 = state.error) {
        if (state.error != null) {
            Toast.makeText(context, state.error, Toast.LENGTH_LONG).show()
        }
    }

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
                        onDroppedIconClicked = {
                            shouldBottomSheetShow = true
                            onEvent(HomeScreenEvent.FromCurrencySelect)
                        }
                    )
                    Text(
                        text = state.fromCurrencyValue,
                        fontSize = 40.sp,
                        fontFamily = FontFamily.SansSerif,
                        color = colorResource(id = R.color.text),
                        modifier = Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = { onEvent(HomeScreenEvent.FromCurrencySelect) }
                        )
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
                        modifier = Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = { onEvent(HomeScreenEvent.ToCurrencySelect) }
                        )
                    )
                    CurrencyRow(
                        modifier = Modifier.fillMaxWidth(),
                        currencyCode = state.toCurrencyCode,
                        currencyName = state.currencyRates[state.toCurrencyCode]?.name ?: "",
                        onDroppedIconClicked = {
                            shouldBottomSheetShow = true
                            onEvent(HomeScreenEvent.ToCurrencySelect)
                        }
                    )
                }
            }

            LazyVerticalGrid(
                modifier = Modifier.padding(horizontal = 25.dp),
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
    ) {
        FloatingActionButton(
            onClick = { onClick(key) },
            modifier = modifier
                .padding(8.dp),
            containerColor = backgroundColor
        ) {
            Text(text = key, color = colorResource(id = R.color.text), fontSize = 32.sp)
        }
    }
}


@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun OnBoardingScreenPreview() {
    HomeScreen(state = HomeScreenState()) {}
}