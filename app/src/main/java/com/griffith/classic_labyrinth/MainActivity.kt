package com.griffith.classic_labyrinth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.griffith.classic_labyrinth.ui.theme.Classic_LabyrinthTheme
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity(), SensorEventListener {

    // Initialise sensur
    private lateinit var sensorManager: SensorManager
    private var mySensor: Sensor? = null

    // Ball position and speed
    private var ballX by mutableFloatStateOf(300f)
    private var ballY by mutableFloatStateOf(500f)
    private var speedX = 0f
    private var speedY = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get data phone tilt sensor
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mySensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        // white screen and black ball
        setContent {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                drawCircle(
                    color = Color.Black,
                    radius = 100f,
                    center = Offset(ballX, ballY)
                )
            }
        }
    }

    // Runs every time the phone tilts
    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        val tiltLeftRight = event.values[0]
        val tiltUpDown = event.values[1]

        // Add tilt to speed
        speedX = speedX - tiltLeftRight
        speedY = speedY + tiltUpDown

        // Slow the ball down by half

        speedX = speedX * 0.7f
        speedY = speedY * 0.7f

        // Move the ball
        ballX = ballX + speedX
        ballY = ballY + speedY
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    // Turn sensor on when opening app
    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_GAME)
    }

    // Turn sensor off when closing app
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}