// Lincoln Teo Ming Kern - 3122215
// github repo: https://github.com/lincolnteo/Classic_Labyrinth
package com.griffith.classic_labyrinth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
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

class GameActivity : ComponentActivity(), SensorEventListener {

    // Initialise sensur
    private lateinit var sensorManager: SensorManager
    private var mySensor: Sensor? = null

    // Ball position and speed
    private var ballX by mutableFloatStateOf(300f)
    private var ballY by mutableFloatStateOf(500f)
    private var speedX = 0f
    private var speedY = 0f

    // Screen size
    private var screenWidth = 0f
    private var screenHeight = 0f
    private val radius = 100f

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
                // Get screen width and height directly inside the Canvas
                screenWidth = size.width
                screenHeight = size.height

                drawCircle(
                    color = Color.Black,
                    radius = radius,
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

        // Don't let ball pass left wall
        if (ballX - radius < 0f) {
            ballX = radius
            speedX = 0f
        }

        // Don't let ball pass right wall
        if (ballX + radius > screenWidth) {
            ballX = screenWidth - radius
            speedX = 0f
        }

        // Don't let ball pass top wall
        if (ballY - radius < 0f) {
            ballY = radius
            speedY = 0f
        }

        // Don't let ball pass bottom wall
        if (ballY + radius > screenHeight) {
            ballY = screenHeight - radius
            speedY = 0f
        }
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