// Lincoln Teo Ming Kern - 3122215
// github repo: https://github.com/lincolnteo/Classic_Labyrinth
package com.griffith.classic_labyrinth

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

class ShopActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShopScreen()
        }
    }
}

@Preview
@Composable
fun ShopScreen() {
    // Get context and cast to Activity so we can use activity?.finish() to close the screen
    val context = LocalContext.current
    val activity = context as? Activity

    // Tracks which shop category the user currently has selected
    var selectedCategory by remember { mutableStateOf("Animals") }

    // Root container holding the background and the overlay UI
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.background_shop),
            contentDescription = "Shop Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Ensures the image stretches to fill screen edges
        )

        // Content overlay container
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Top Right Coin and Exit Buttons
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp), // Padding to avoid blocking
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Top Right Coin Button
                Image(
                    painter = painterResource(id = R.drawable.button_coin),
                    contentDescription = "Coins",
                    modifier = Modifier.height(60.dp)
                )

                // Top Right Exit Button
                Image(
                    painter = painterResource(id = R.drawable.button_exit),
                    contentDescription = "Close Shop",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(end = 10.dp)
                        .clickable { activity?.finish() } // Triggers the Android activity finish loop
                )
            }

            // Animals, Woods, Metals Categories
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(180.dp)
                    .padding(top = 40.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Categories text
                Image(
                    painter = painterResource(id = R.drawable.shop_categories),
                    contentDescription = "Categories",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp, start = 12.dp),
                    contentScale = ContentScale.Fit
                )

                // Define the available categories and their image assets
                val categories = listOf(
                    "Animals" to R.drawable.shop_animals,
                    "Woods" to R.drawable.shop_wood,
                    "Metals" to R.drawable.shop_metals
                )

                // Loop through the categories list to generate a clickable image button for each category
                for ((category, imageRes) in categories) {
                    val isSelected = selectedCategory == category // Check if this is the active tab
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = category,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                            .clickable {
                                selectedCategory = category
                            }, // update status when clicked
                        contentScale = ContentScale.Fit,
                        // Unselected buttons are semi transparent
                        alpha = if (isSelected) 1.0f else 0.5f
                    )
                }
            }

            //Showing selected category items placeholder
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(start = 200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Items for: $selectedCategory",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
            }
            //Purchase Button
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.button_purchase),
                    contentDescription = "Purchase",
                    modifier = Modifier
                        .height(50.dp)
                        .width(160.dp)
                        .clickable {})
            }
        }
    }
}