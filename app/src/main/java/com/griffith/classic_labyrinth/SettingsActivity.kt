package com.griffith.classic_labyrinth

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SettingsScreen()
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
    var masterVolume by remember { mutableFloatStateOf(0.5f) }
    var musicVolume by remember { mutableFloatStateOf(0.5f) }
    var fxVolume by remember { mutableFloatStateOf(0.5f) }
    var sensitivity by remember { mutableFloatStateOf(0.5f) }

    // root container
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Top-Right Close Button
        Button(
            onClick = { activity?.finish() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(44.dp)
                .border(2.dp, Color.Black, CircleShape),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text("✕", color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        // 4 Setting Sliders
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.TopStart)
                .padding(top = 2.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            SettingRow(
                label = "Master volume",
                value = masterVolume,
                onValueChange = { masterVolume = it })
            SettingRow(
                label = "Music volume", value = musicVolume, onValueChange = { musicVolume = it })
            SettingRow(label = "FX Volume", value = fxVolume, onValueChange = { fxVolume = it })
            SettingRow(
                label = "Sensitivity", value = sensitivity, onValueChange = { sensitivity = it })
        }

        // Calibrate Accelerometer Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(all = 1.dp)
        ) {
            Button(
                onClick = {
                    //Calibarte
                },
                modifier = Modifier.border(2.dp, Color.Black, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
            ) {
                Text(
                    text = "Calibrate",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
fun SettingRow(
    label: String, value: Float, onValueChange: (Float) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        // Label on the left
        Box(
            modifier = Modifier
                .width(170.dp)
                .border(2.dp, Color.Black, RoundedCornerShape(20.dp))
                .background(Color.Magenta, RoundedCornerShape(20.dp))
                .padding(horizontal = 14.dp, vertical = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = label, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.Black
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Slider on the right
        Slider(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            colors = SliderDefaults.colors(
                thumbColor = Color.Red,
                activeTrackColor = Color.Black,
                inactiveTrackColor = Color.LightGray
            )
        )
    }
}