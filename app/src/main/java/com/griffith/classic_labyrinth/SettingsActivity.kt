// Lincoln Teo Ming Kern - 3122215
// github repo: https://github.com/lincolnteo/Classic_Labyrinth
package com.griffith.classic_labyrinth

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SettingsScreen()
            enableEdgeToEdge()
        }
    }
}

@Preview
@Composable
fun SettingsScreen() {

    // Get context and cast to Activity so we can use activity?.finish() to close the screen
    val context = LocalContext.current
    val activity = context as? Activity

    // Slider default position
    var masterVolume by remember { mutableFloatStateOf(1f) }
    var musicVolume by remember { mutableFloatStateOf(0.5f) }
    var fxVolume by remember { mutableFloatStateOf(0.5f) }
    var sensitivity by remember { mutableFloatStateOf(0.5f) }

    // root container
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.background_settings),
            contentDescription = "Settings Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Ensures it fills the screen without white bars
        )

        // Content overlay container
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(21.dp)
        ) {
            // Top-Right Close Button
            Image(
                painter = painterResource(id = R.drawable.button_exit),
                contentDescription = "Close Settings",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(50.dp)
                    .clickable { 
                        SoundManager.playClick()
                        activity?.finish() 
                    }
            )

        // 4 Setting Sliders
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.TopStart)
                .padding(top = 50.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            SettingRow(
                imageResId = R.drawable.settings_mastervolume,
                value = masterVolume,
                onValueChange = { 
                    masterVolume = it 
                    SoundManager.masterVolume = it
                    SoundManager.updateVolumes()
                })
            SettingRow(
                imageResId = R.drawable.settings_musicvolume,
                value = musicVolume,
                onValueChange = { 
                    musicVolume = it 
                    SoundManager.musicVolume = it
                    SoundManager.updateVolumes()
                })
            SettingRow(
                imageResId = R.drawable.settings_fxvolume, 
                value = fxVolume, 
                onValueChange = { 
                    fxVolume = it 
                    SoundManager.fxVolume = it
                })
            SettingRow(
                imageResId = R.drawable.settings_sensitivity, 
                value = sensitivity, 
                onValueChange = { sensitivity = it })
        }

        // Calibrate Accelerometer Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 5.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.button_calibrate),
                contentDescription = "Calibrate Accelerometer",
                modifier = Modifier
                    .height(60.dp)
                    .clickable {
                        SoundManager.playClick()
                        // Calibrate
                    }
            )
        }
    }
}
}

@Composable
fun SettingRow(
    imageResId: Int, value: Float, onValueChange: (Float) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        // Image Label on the left
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = "Setting Label",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(135.dp)
                .height(67.5.dp)
                .padding(start = 10.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        // Slider on the right
        Slider(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            colors = SliderDefaults.colors(
                thumbColor = Color(red = 43, green = 39, blue = 29), // muddish brown thumb
                activeTrackColor = Color(red = 43, green = 39, blue = 29),// muddish brown
                inactiveTrackColor = Color.DarkGray,  // Dark un-filled track
            )
        )
    }
}