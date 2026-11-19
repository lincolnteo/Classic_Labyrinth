// Lincoln Teo Ming Kern - 3122215
// GitHub repo: https://github.com/lincolnteo/Classic_Labyrinth
package com.griffith.classic_labyrinth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

class LevelsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LevelsScreen()
            enableEdgeToEdge()
        }
    }
    // resume music when app is maximized
    override fun onResume() {
        super.onResume()
        SoundManager.isSwitchingScreens = false
        SoundManager.resumeMusic()
    }
    // pause music when app is minimize, but keep playing if switching between activities
    override fun onPause() {
        super.onPause()
        if (!SoundManager.isSwitchingScreens) {
            SoundManager.pauseMusic()
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun LevelsScreen() {
    // Get context and cast to Activity so we can use activity?.finish() to close the screen
    val context = LocalContext.current
    val activity = context as? Activity

    // Selected Level State
    var selectedLevel by remember { mutableIntStateOf(2) }

    // List of 18 levels with their status
    // Level 1: completed, Levels 2-3: unlocked, Levels 4-18: locked
    val levels = remember {
        (1..18).map { levelNum ->
            val state = when (levelNum) {
                1 -> "completed"
                2, 3 -> "unlocked"
                else -> "locked"
            }
            // group 3 related data into a single object
            // each level is stored at triple (level state and title)
            Triple(levelNum, state, "Level $levelNum")
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.background_levels),
            contentDescription = "Levels Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Content container
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Close Button
            Image(
                painter = painterResource(id = R.drawable.button_exit),
                contentDescription = "Close Levels",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(60.dp)
                    .clickable { 
                        SoundManager.playClick()
                        SoundManager.isSwitchingScreens = true
                        activity?.finish() 
                    } // Triggers the Android activity finish loop
            )

            // Left Grid Levels and Right Info Panel
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 50.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Side 3x6 Level Grid 3 columns, 6 rows = 18 level
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .weight(1.3f)
                        .fillMaxHeight()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    items(levels) { (levelNum, state) ->
                        val isSelected = (selectedLevel == levelNum)

                        val imageRes = when (levelNum) {
                            1 -> R.drawable.level_1
                            2 -> R.drawable.level_2
                            3 -> R.drawable.level_3
                            4 -> R.drawable.level_4
                            5 -> R.drawable.level_5
                            6 -> R.drawable.level_6
                            7 -> R.drawable.level_7
                            8 -> R.drawable.level_8
                            9 -> R.drawable.level_9
                            10 -> R.drawable.level_10
                            11 -> R.drawable.level_11
                            12 -> R.drawable.level_12
                            13 -> R.drawable.level_13
                            14 -> R.drawable.level_14
                            15 -> R.drawable.level_15
                            16 -> R.drawable.level_16
                            17 -> R.drawable.level_17
                            18 -> R.drawable.level_18
                            else -> R.drawable.level_1
                        }

                        // Determine image alpha (faded if locked)
                        val imageAlpha = if (state == "locked") 0.5f else 1.0f

                        // if level selected it has a cyan border
                        Box(
                            modifier = Modifier
                                .height(55.dp) // Restore original button height
                                .clickable {
                                    // Only click if its not locked
                                    if (state != "locked") {
                                        SoundManager.playClick()
                                        selectedLevel = levelNum
                                    }
                                }, contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = imageRes),
                                contentDescription = "Level $levelNum",
                                modifier = Modifier
                                    .fillMaxHeight() // Make the image fill the vertical space
                                    .border(
                                        width = if (isSelected) 3.dp else 0.dp,
                                        color = if (isSelected) Color.Cyan else Color.Transparent,
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                contentScale = ContentScale.Fit, // Preserve original aspect ratio inside the box
                                alpha = imageAlpha
                            )
                        }
                    }
                }

                // Stage Info Box and Start Game Button
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Level Details Plank
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f) // Takes up remaining space above the start button
                            .padding(bottom = 8.dp), contentAlignment = Alignment.TopCenter
                    ) {
                        // Background Image for Stage Details
                        Image(
                            painter = painterResource(id = R.drawable.level_details),
                            contentDescription = "Level Details Background",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )

                        // Content overlay text (centered horizontally, aligned down into the boxes)
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 50.dp), // Push all the text down to align with the empty slots in the image
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(20.dp)) // Space down to Objective area
                            Text(
                                text = "Collect all tokens",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(15.dp)) // Space down to Traps area
                            Text(
                                text = "4 holes",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            Text(
                                text = "200 coins",
                                fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White
                            )
                        }
                    }

                    // Start Game Button
                    Image(
                        painter = painterResource(id = R.drawable.level_startgame),
                        contentDescription = "Start Game",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .clickable {
                                SoundManager.playClick()
                                SoundManager.isSwitchingScreens = true
                                val intent = Intent(activity, GameActivity::class.java).apply {
                                    putExtra("EXTRA_LEVEL_ID", selectedLevel)
                                }
                                activity?.startActivity(intent)
                            },
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}
