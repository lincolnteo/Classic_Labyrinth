// Lincoln Teo Ming Kern - 3122215
// github repo: https://github.com/lincolnteo/Classic_Labyrinth
package com.griffith.classic_labyrinth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.jvm.java
  import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

// Main Activity, entry point of the app
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainMenuScreen()
        }
    }
}

// Main menu UI
@Preview(showSystemUi = true)
@Composable
fun MainMenuScreen() {
    // Context for launching intents
    val context = LocalContext.current

    // Root container for the main menu screen
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.background_mainmenu),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // ensures it fills the screen without white bars
        )

        // Content overlay container
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // Top Bar: Player Stats and Settings
            Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Player stats badge (Level, XP, Coins)
            Box(
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.coinbar),
                    contentDescription = "Coin Bar",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.size(width = 280.dp, height = 50.dp)
                )
                Text(
                    text = "level: XX   XP: XX/100   Coins: XXXXX",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            // Settings button: Opens SettingsActivity
            Image(
                painter = painterResource(id = R.drawable.button_setting),
                contentDescription = "Settings",
                modifier = Modifier
                    .padding(end = 5.dp) // Move right
                    .size(60.dp)
                    .clickable {
                        val intent = Intent(context, SettingsActivity::class.java)
                        context.startActivity(intent)
                    }
            )
        }

        // Bottom Row: Shop, Rate Us, Start
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {

            // Shop button: Opens ShopActivity
            Image(
                painter = painterResource(id = R.drawable.button_shop),
                contentDescription = "Shop",
                modifier = Modifier
                    .padding(start = 10.dp) // Move slightly toward center
                    .size(100.dp)
                    .clickable {
                        val intent = Intent(context, ShopActivity::class.java)
                        context.startActivity(intent)
                    }
            )

            // Rate US button: Opens Play Store URL
            Image(
                painter = painterResource(id = R.drawable.button_rateme),
                contentDescription = "Rate Us",
                modifier = Modifier
                    .size(100.dp)
                    .clickable {
                        val intent = Intent(// implicit intent
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.maithu.griffith")
                        )
                        context.startActivity(intent)
                    }
            )

            // Start button: Opens GameActivity
            Image(
                painter = painterResource(id = R.drawable.button_startgame),
                contentDescription = "Start",
                modifier = Modifier
                    .padding(end = 10.dp) // Move slightly toward center
                    .size(100.dp)
                    .clickable {
                        val intent = Intent(context, LevelsActivity::class.java)
                        context.startActivity(intent)
                    }
            )
        }
    }
}
}