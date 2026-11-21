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
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.platform.LocalDensity
import androidx.activity.enableEdgeToEdge

class GameActivity : ComponentActivity(), SensorEventListener {

    // Sensor Initialization
    private lateinit var sensorManager: SensorManager
    private var mySensor: Sensor? = null

    // Level state
    private var currentLevelId = 1
    private var hasWon = false
    private var startTimeMillis = 0L
    private var isPositionInitialized = false

    // Ball position and speed
    private var ballX by mutableFloatStateOf(300f)
    private var ballY by mutableFloatStateOf(500f)
    private var speedX = 0f
    private var speedY = 0f

    // Screen size
    private var screenWidth = 0f
    private var screenHeight = 0f
    private val radius = 60f

    private var isGamePaused by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Get Level ID passed from LevelsActivity (defaults to 1)
        currentLevelId = intent.getIntExtra("EXTRA_LEVEL_ID", 1)
        startTimeMillis = System.currentTimeMillis()

        // Get data phone tilt sensor
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mySensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        // background and black ball
        setContent {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                // Background Image
                Image(
                    painter = painterResource(id = R.drawable.background_plain),
                    contentDescription = "Plain Background",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                val density = LocalDensity.current
                val equippedSkin = EquippedManager.equippedItem
                // Play Zone canvas
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    screenWidth = size.width
                    screenHeight = size.height

                    // set start position on the first frame
                    if (!isPositionInitialized && size.width > 0f) {
                        isPositionInitialized = true
                        val startZoneX = 220f
                        val topWallY = screenHeight * 0.35f
                        val bottomWallY = screenHeight * 0.65f
                        ballX = startZoneX / 2f // position ball inside start zone
                        ballY = (topWallY + bottomWallY) / 2f // center ball vertically
                    }
                    Level1.drawCanvas(this, screenWidth, screenHeight)

                    if (equippedSkin == null) {
                        drawCircle(
                            color = Color.Black, radius = radius, center = Offset(ballX, ballY)
                        )
                    }
                }
                // if no skin is equipped used the same ball
                if (equippedSkin != null) {
                    // access density scope to convert coordinates to Dp to compose images
                    with(density) {
                        val sizeDp = (radius * 2).toDp() // ball diameter in Dp
                        // x and y pos in Dp
                        val xDp = (ballX - radius).toDp()
                        val yDp = (ballY - radius).toDp()

                        // draw skins to the current ball position
                        Image(
                            painter = painterResource(id = equippedSkin.imageRes),
                            contentDescription = "Ball Skin",
                            modifier = Modifier
                                .size(sizeDp)
                                .offset(x = xDp, y = yDp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                // Top right exit button overlapping the canvas
                Image(
                    painter = painterResource(id = R.drawable.button_exit),
                    contentDescription = "Close Game",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .size(60.dp)
                        .clickable {
                            SoundManager.playClick()
                            isGamePaused = true
                        })
                // go to pause menu
                if (isGamePaused) {
                    InGameMenuOverlay(
                        onResume = { // pasue game
                            isGamePaused = false 
                        },
                        onSettings = { // go to setting explicit
                            SoundManager.isSwitchingScreens = true
                            val intent = Intent(this@GameActivity, SettingsActivity::class.java)
                            startActivity(intent)
                        },
                        onExit ={ // go to main menu AKA exit explicit
                            SoundManager.isSwitchingScreens = true
                            val intent = Intent(this@GameActivity, LevelsActivity::class.java)
                            startActivity(intent)
                            finish()
                        },
                        onMainMenu = { // go to main menu AKA exit explicit
                            SoundManager.isSwitchingScreens = true
                            val intent = Intent(this@GameActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    )
                }
            }
        }
    }

    // Runs every time the phone tilts
    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || isGamePaused || hasWon) return

        // In landscape mode the phone physical axes are rotated 90 degrees relative to the screen.
        // The phone Y-axis (values[1]) becomes the screen Left/Right.
        // The phone X-axis (values[0]) becomes the screen Up/Down.
        val tiltLeftRight = event.values[1] - CalibrationManager.offsetX
        val tiltUpDown = event.values[0] - CalibrationManager.offsetY

        // Adjust speed based on the tilt.
        // Tilting Right positive Y should increase speedX (move right)
        // Tilting Down positiveX should increase speedY (move down)
        speedX = speedX + tiltLeftRight
        speedY = speedY + tiltUpDown

        // Adjust speed based on sensitivity setting
        speedX = speedX * CalibrationManager.sensitivity
        speedY = speedY * CalibrationManager.sensitivity

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
        // Level-specific boundary limits and win condition checks
        if (screenHeight > 0f) {
            when (currentLevelId) {
                1 -> Level1.updatePhysics(
                    context = this,
                    ballX = ballX,
                    ballY = ballY,
                    radius = radius,
                    screenWidth = screenWidth,
                    screenHeight = screenHeight,
                    hasWon = hasWon,
                    startTimeMillis = startTimeMillis,
                    onUpdateY = { ballY = it },
                    onStopY = { speedY = 0f },
                    onWinState = { hasWon = true }
                )
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onResume() {
        super.onResume()
        SoundManager.isSwitchingScreens = false
        SoundManager.resumeMusic()
        sensorManager.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_GAME)

        // Load tilt sensitivity from player_setting.csv
        val settings = csvManager.loadPlayerSettings(this)
        CalibrationManager.sensitivity = settings["tilt_sensitivity"]?.toFloatOrNull() ?: 0.7f
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        if (!SoundManager.isSwitchingScreens) {
            SoundManager.pauseMusic()
        }
    }
}