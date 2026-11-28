// Lincoln Teo Ming Kern - 3122215
// GitHub repo: https://github.com/lincolnteo/Classic_Labyrinth
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import androidx.activity.enableEdgeToEdge

// Main Activity, entry point of the app
class MainActivity : ComponentActivity() {
    // State variables for player stats (Level, XP, Coins)
    private var levelText by mutableStateOf("1")
    private var xpText by mutableStateOf("0/100")
    private var coinsText by mutableStateOf("0")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize sound manager
        SoundManager.init(this.applicationContext)
        SoundManager.resumeMusic()

        setContent {
            MainMenuScreen(levelText, xpText, coinsText)
            enableEdgeToEdge()
        }
    }
    // resume music when app is maximized
    // reload player stats from player_setting.csv when screen resumes
    override fun onResume() {
        super.onResume()
        SoundManager.isSwitchingScreens = false
        SoundManager.resumeMusic()

        // Load latest stats from player_setting.csv via csvManager
        val settings = CsvManager.loadPlayerSettings(this)
        levelText = settings["level"] ?: "1"
        xpText = "${settings["current_xp"] ?: "0"}/100"
        coinsText = settings["coins"] ?: "0"
    }

    // pause music when app is minimized, but keep playing if switching between activities
    override fun onPause() {
        super.onPause()
        if (!SoundManager.isSwitchingScreens) {
            SoundManager.pauseMusic()
        }
    }
}

// Main menu UIs
@Preview(showSystemUi = true)
@Composable
fun MainMenuScreen(
    levelText: String = "1",
    xpText: String = "0/100",
    coinsText: String = "0"
) {
    // Context for launching intents
    val context = LocalContext.current

    // Root container for the main menu screen
    Box(
        modifier = Modifier.fillMaxSize()
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
                .padding(16.dp)
                .fillMaxSize()
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
                        modifier = Modifier.size(
                            width = 320.dp, height = 50.dp
                        ) // Widen the coin bar
                    )

                    // Align the text into the three wooden slots
                    Row(
                        modifier = Modifier.fillMaxWidth(0.35f), // Scale to fit the image bounds
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Level slot
                        Text(
                            text = levelText,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 50.dp)
                        )

                        // XP slot
                        Text(
                            text = xpText,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(start = 65.dp)
                        )

                        // Coins slot
                        Text(
                            text = coinsText,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(start = 60.dp)
                        )
                    }
                }

                // Settings button: Opens SettingsActivity
                Image(
                    painter = painterResource(id = R.drawable.button_setting),
                    contentDescription = "Settings",
                    modifier = Modifier
                        .padding(end = 5.dp) // Move right
                        .size(60.dp)
                        .clickable {
                            SoundManager.playClick()
                            SoundManager.isSwitchingScreens = true
                            val intent = Intent(context, SettingsActivity::class.java)
                            context.startActivity(intent)
                        })
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
                            SoundManager.playClick()
                            SoundManager.isSwitchingScreens = true
                            val intent = Intent(context, ShopActivity::class.java)
                            context.startActivity(intent)
                        })

                // Rate US button: Opens Play Store URL
                Image(
                    painter = painterResource(id = R.drawable.button_rateme),
                    contentDescription = "Rate Us",
                    modifier = Modifier
                        .size(100.dp)
                        .clickable {
                            SoundManager.playClick()
                            val intent = Intent(// implicit intent
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=com.maithu.griffith")
                            )
                            context.startActivity(intent)
                        })

                // Start button: Opens GameActivity
                Image(
                    painter = painterResource(id = R.drawable.button_startgame),
                    contentDescription = "Start",
                    modifier = Modifier
                        .padding(end = 10.dp) // Move slightly toward center
                        .size(100.dp)
                        .clickable {
                            SoundManager.playClick()
                            SoundManager.isSwitchingScreens = true
                            val intent = Intent(context, LevelsActivity::class.java)
                            context.startActivity(intent)
                        })
            }
        }
    }
}