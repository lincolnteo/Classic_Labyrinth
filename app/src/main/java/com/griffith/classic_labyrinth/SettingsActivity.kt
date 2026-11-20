// Lincoln Teo Ming Kern 3122215
// GitHub repo: https://github.com/lincolnteo/Classic_Labyrinth
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
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.Toast

class SettingsActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var currentTiltX = 0f
    private var currentTiltY = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Get data from phone accelerometer
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        setContent {
            SettingsScreen(
                onCalibrate = {
                    // save the current phone tilt angles as new flat tilt
                    CalibrationManager.offsetX = currentTiltX
                    CalibrationManager.offsetY = currentTiltY
                    Toast.makeText(this, "Calibrated Successfully!", Toast.LENGTH_SHORT)
                        .show()// prompt user calibration is done
                })
            enableEdgeToEdge()
        }
    }

    // resume music when app is maximized
    override fun onResume() {
        super.onResume()
        SoundManager.isSwitchingScreens = false
        SoundManager.resumeMusic()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    // pause music when app is minimize, but keep playing if switching between activities
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        if (!SoundManager.isSwitchingScreens) {
            SoundManager.pauseMusic()
        }
    }
    // Update current tilt when the phone is moved
    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return
        currentTiltX = event.values[1]
        currentTiltY = event.values[0]
    }
    // not used, required if using Sensor event listener
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

@Preview
@Composable
// callback
fun SettingsScreen(onCalibrate: () -> Unit = {}) {

    // Get context and cast to Activity so we can use activity?.finish() to close the screen
    val context = LocalContext.current
    val activity = context as? Activity


    // Slider positions loaded from player_setting.csv
    var masterVolume by remember { mutableFloatStateOf(SoundManager.masterVolume) }
    var musicVolume by remember { mutableFloatStateOf(SoundManager.musicVolume) }
    var fxVolume by remember { mutableFloatStateOf(SoundManager.fxVolume) }

    // Load tilt sensitivity from player_setting.csv
    val playerSettings = remember { csvManager.loadPlayerSettings(context) }
    // if sensitivity is not set the default will be 0.5
    var sensitivity by remember { mutableFloatStateOf(playerSettings["tilt_sensitivity"]?.toFloatOrNull() ?: 0.5f) }

    // root container
    Box(
        modifier = Modifier.fillMaxSize()
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
                        SoundManager.isSwitchingScreens = true
                        activity?.finish()
                    })

            // 4 Setting Sliders
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.TopStart)
                    .padding(top = 50.dp), verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                // master volume slider
                SettingRow(
                    imageResId = R.drawable.settings_mastervolume,
                    value = masterVolume,
                    onValueChange = {
                        masterVolume = it
                        SoundManager.masterVolume = it
                        SoundManager.updateVolumes()
                        // save new music volume to player_settings.csv
                        csvManager.savePlayerSetting(context, "master_vol", it.toString())
                    })
                // music volume slider
                SettingRow(
                    imageResId = R.drawable.settings_musicvolume,
                    value = musicVolume,
                    onValueChange = {
                        musicVolume = it
                        SoundManager.musicVolume = it
                        SoundManager.updateVolumes()
                        // save new music volume to player_settings.csv
                        csvManager.savePlayerSetting(context, "music_vol", it.toString())
                    })
                // fx volume slider
                SettingRow(
                    imageResId = R.drawable.settings_fxvolume,
                    value = fxVolume,
                    onValueChange = {
                        fxVolume = it
                        SoundManager.fxVolume = it
                        // save new music volume to player_settings.csv
                        csvManager.savePlayerSetting(context, "fx_vol", it.toString())
                    })
                // sensitivity slider range 0 to 1
                SettingRow(
                    imageResId = R.drawable.settings_sensitivity,
                    value = sensitivity,
                    onValueChange = {
                        sensitivity = it
                        CalibrationManager.sensitivity = it
                        // save new sensitivity to player_setting.csv
                        csvManager.savePlayerSetting(context, "tilt_sensitivity", it.toString())
                    })
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
                            onCalibrate()
                        })
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

// Singleton that holds the calibrated tilt angle and velocity sensitivity
object CalibrationManager {
    var offsetX = 0f // horizontal
    var offsetY = 0f // vertical
    var sensitivity = 0.7f // ball velocity
}