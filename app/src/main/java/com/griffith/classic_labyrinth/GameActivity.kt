// Lincoln Teo Ming Kern - 3122215
// github repo: https://github.com/lincolnteo/Classic_Labyrinth
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.enableEdgeToEdge

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
        enableEdgeToEdge()

        // Get data phone tilt sensor
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mySensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        // background and black ball
        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Background Image
                Image(
                    painter = painterResource(id = R.drawable.background_plain),
                    contentDescription = "Plain Background",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Playzone Canvas (draws behind the button)
                Canvas(
                    modifier = Modifier.fillMaxSize()
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

                // Top right exit button overlapping the canvas
                Button(
                    onClick = { finish() },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .size(44.dp)
                        .border(2.dp, Color.Black, CircleShape),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("✕", color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // Runs every time the phone tilts
    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        // In landscape mode the phone physical axes are rotated 90 degrees relative to the screen.
        // The phone Y-axis (values[1]) becomes the screen Left/Right.
        // The phone X-axis (values[0]) becomes the screen Up/Down.
        val tiltLeftRight = event.values[1]
        val tiltUpDown = event.values[0]

        // Adjust speed based on the tilt.
        // Tilting Right positive Y should increase speedX (move right)
        // Tilting Down positiveX should increase speedY (move down)
        speedX = speedX + tiltLeftRight
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