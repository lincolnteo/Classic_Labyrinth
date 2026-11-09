package com.griffith.classic_labyrinth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.jvm.java

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
            .background(Color.White)
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
                modifier = Modifier
                    .background(Color.LightGray, RoundedCornerShape(20.dp))
                    .border(2.dp, Color.Black, RoundedCornerShape(20.dp))
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "level: XX   XP: XX/100   Coins: XXXXX",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            // Settings button: Opens SettingsActivity
            Button(
                onClick = {
                    val intent = Intent(context, SettingsActivity::class.java)
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier.size(50.dp),
                contentPadding = PaddingValues(0.dp)

            ) {
                Text("⚙\uFE0F", fontSize = 20.sp, color = Color.Black)
            }
        }

        // Center: Game Title
        Text(
            text = "The Classic Labyrinth",
            fontFamily = FontFamily.Cursive,
            fontStyle = FontStyle.Italic,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.Center)
        )

        // Bottom Row: Shop, Rate Us, Start
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {

            // Shop button: Placeholder for shop screen
            Button(
                onClick = { /* TODO: Shop destination */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .size(90.dp)
                    .border(2.dp, Color.Black, CircleShape),
                shape = CircleShape
            ) {
                Text("Shop", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            // Rate US button: Opens Play Store URL
            Button(
                onClick = {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.maithu.griffith")
                    )
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Yellow,
                    contentColor = Color.Black
                ),
                modifier = Modifier.border(2.dp, Color.Black, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    "Rate US!", fontSize = 16.sp, fontWeight = FontWeight.Bold
                )
            }

            // Start button: Opens GameActivity
            Button(
                onClick = {
                    val intent = Intent(context, GameActivity::class.java)
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .size(90.dp)
                    .border(2.dp, Color.Black, CircleShape),
                shape = CircleShape
            ) {
                Text("Start!", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}