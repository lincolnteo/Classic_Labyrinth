package com.griffith.classic_labyrinth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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

class LevelsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LevelsScreen()
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun LevelsScreen() {
    // Get context and cast to Activity so we can use activity?.finish() to close the screen
    val context = LocalContext.current
    val activity = context as? Activity

    // Default to Level 2 at launch as requested
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
            Triple(levelNum, state, "Level $levelNum")
        }
    }

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
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text("✕", color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        // Title
        Text(
            text = "Select Stage",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.TopStart)
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
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(4.dp)
            ) {
                items(levels) { (levelNum, state) ->
                    val isSelected = (selectedLevel == levelNum)

                    // Determine color based on status
                    val buttonColor = when {
                        state == "completed" -> Color.Green
                        isSelected -> Color.Cyan
                        state == "unlocked" -> Color.LightGray
                        else -> Color.Red
                    }

                    // if level selected it turns cyan with a black border
                    Box(
                        modifier = Modifier
                            .height(55.dp)
                            .border(
                                width = if (isSelected) 3.dp else 2.dp,
                                color = if (isSelected) Color.Black else Color.DarkGray,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .background(buttonColor, RoundedCornerShape(12.dp))
                            .clickable {
                                // Only click if its not locked
                                if (state != "locked") {
                                    selectedLevel = levelNum
                                }
                            }, contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$levelNum",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            // Stage Info Box and Start Game Button
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .border(2.dp, Color.Black, RoundedCornerShape(16.dp))
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Stage Details",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Level: $selectedLevel",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                    Text(
                        text = "Objective: Collect all token",
                        fontSize = 15.sp,
                        color = Color.DarkGray
                    )
                    Text(text = "Traps: 4 holes", fontSize = 15.sp, color = Color.DarkGray)
                    Text(text = "Reward: 200 coins", fontSize = 15.sp, color = Color.DarkGray)
                }

                // Start Game Button
                Button(
                    onClick = {
                        val intent = Intent(activity, GameActivity::class.java).apply {
                            putExtra("EXTRA_LEVEL_ID", selectedLevel)
                        }
                        activity?.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .border(2.dp, Color.Black, RoundedCornerShape(25.dp)),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                ) {
                    Text(
                        text = "Start Game!",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}