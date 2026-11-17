package com.griffith.classic_labyrinth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.getValue
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

class EndGameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize sound manager here instead since we removed the App class
        SoundManager.init(this.applicationContext)
        SoundManager.resumeMusic()

        setContent {
            MainMenuScreen()
            enableEdgeToEdge()
        }
    }
}
@Preview(showBackground = true)
@Composable
fun EndGameScreen() {
    val context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_plain),
            contentDescription = "Plain Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomEnd),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom
        ) {
            Image(
                painter = painterResource(id = R.drawable.button_exittext),
                contentDescription = "Shop",
                modifier = Modifier
                    .size(100.dp)
                    .align(alignment = Alignment.Bottom)
                    .clickable {
                        SoundManager.playClick()
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                    })

            Image(
                painter = painterResource(id = R.drawable.button_calibrate),
                contentDescription = "Shop",
                modifier = Modifier
                    .size(100.dp)
                    .clickable {
                        SoundManager.playClick()
                        val intent = Intent(context, LevelsActivity::class.java)
                        context.startActivity(intent)
                    })
        }
    }
}