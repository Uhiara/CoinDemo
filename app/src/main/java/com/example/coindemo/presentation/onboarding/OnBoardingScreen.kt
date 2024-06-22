package com.example.coindemo.presentation.onboarding

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.coindemo.R
import com.example.coindemo.presentation.common.CommonText
import kotlinx.coroutines.launch


@Composable
fun OnBoardingScreen(navController: NavHostController) {

    val images = listOf(
        R.drawable.coinxchangei,
        R.drawable.coinxchangeii,
        R.drawable.coinxchangeiii,
    )

    val titles = listOf(
        "Keep tabs with exchange rates",
        "Convert currencies across continents",
        "The World is your Wallet"
    )

    val descriptions = listOf(
        "Exchange currencies easily, wherever you are.",
        "Keep in touch with the exchange rates across the globe",
        "Quick, simple, easy to use and user friendly"
    )

    val pagerState = rememberPagerState(
        pageCount = { images.size }
    )

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HorizontalPager(
            state = pagerState,
            Modifier.wrapContentSize()
        ) { currentPage ->
            Column(
                Modifier
                    .wrapContentSize()
                    .padding(26.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(painter = painterResource(id = images[currentPage]), contentDescription = "")

                CommonText(
                    modifier = Modifier,
                    textValue = titles[currentPage],
                    textSize = 32.sp,
                    lineHeight = 38.sp,
                    fontWeight = FontWeight(700),
                    fontFamily = FontFamily(Font(R.font.roboto_bold)),
                    colorValue = colorResource(id = R.color.teal),
                    textAlign = TextAlign.Center
                )
                CommonText(
                    modifier = Modifier.padding(top = 45.dp),
                    textValue = descriptions[currentPage],
                    textSize = 16.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight(400),
                    fontFamily = FontFamily(Font(R.font.roboto_medium)),
                    colorValue = colorResource(id = R.color.teal),
                    textAlign = TextAlign.Center,
                )
            }
        }

        PageIndicator(
            pageCount = images.size,
            currentPage = pagerState.currentPage,
            modifier = Modifier.padding(60.dp)
        )

        // Buttons for navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            val scope = rememberCoroutineScope()
            // Previous button
            Button(
                onClick = {
                    if (pagerState.currentPage > 0) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }
                },
                enabled = pagerState.currentPage > 0
            ) {
                Text("Previous")
            }

            Button(
                onClick = {
                    if (pagerState.currentPage < pagerState.pageCount - 1) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        navController.popBackStack()
                        navController.navigate("Home Screen")
                    }
                },
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.teal))
            ) {
                Text(if (pagerState.currentPage < pagerState.pageCount - 1) "Next" else "Get Started")
            }
        }
    }
}

@Preview (showBackground = true)
@Preview (uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun OnBoardingScreenPreview() {
    OnBoardingScreen(navController = rememberNavController())
}