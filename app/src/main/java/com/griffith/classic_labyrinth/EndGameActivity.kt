// Lincoln Teo Ming Kern 3122215
// GitHub repo: https://github.com/lincolnteo/Classic_Labyrinth
package com.griffith.classic_labyrinth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

class EndGameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoundManager.init(this.applicationContext)
        SoundManager.resumeMusic()

        val levelId = intent.getIntExtra("EXTRA_LEVEL_ID", 1)
        val playerLevel = intent.getIntExtra("EXTRA_PLAYER_LEVEL", 1)
        val objective = intent.getStringExtra("EXTRA_OBJECTIVE") ?: "NOT FOUND"
        val rewards = intent.getIntExtra("EXTRA_REWARDS", 200)
        val xpReward = intent.getIntExtra("EXTRA_XP_REWARD", 100)
        val currentXp = intent.getIntExtra("EXTRA_CURRENT_XP", 150)
        val completionTime = intent.getStringExtra("EXTRA_TIME") ?: "NOT FOUND"
        val personalBest = intent.getStringExtra("EXTRA_PERSONAL_BEST") ?: "NO RECORD"

        setContent {
            EndGameScreen(
                levelId = levelId,
                playerLevel = playerLevel,
                objective = objective,
                rewards = rewards,
                xpReward = xpReward,
                completionTime = completionTime,
                personalBest = personalBest
            )
            enableEdgeToEdge()
        }
    }
    // resume music when app is maximized
    // reload player stats from player_setting.csv when screen resumes
    override fun onResume() {
        super.onResume()
        SoundManager.isSwitchingScreens = false
        SoundManager.resumeMusic()
    }
    // pause music when app is minimized, but keep playing if switching between activities
    override fun onPause() {
        super.onPause()
        if (!SoundManager.isSwitchingScreens) {
            SoundManager.pauseMusic()
        }
    }
}
@Composable
fun EndGameScreen(
    levelId: Int,
    playerLevel: Int,
    objective: String,
    rewards: Int,
    xpReward: Int,
    completionTime: String,
    personalBest: String
) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 50.dp)
                .align(Alignment.TopStart)
        ) {
            Text(
                text = "Level: $levelId  (Player Level: $playerLevel)",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Objective Achieve: $objective",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Rewards: $rewards coins",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Rewards XP: $xpReward XP",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Yellow
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Completion Time: $completionTime",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Personal Best: $personalBest",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomEnd),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom
        ) {
            // exit button
            Image(
                painter = painterResource(id = R.drawable.button_exittext),
                contentDescription = "Exit",
                modifier = Modifier
                    .size(100.dp)
                    .align(alignment = Alignment.Bottom)
                    .padding(end = 10.dp)
                    .clickable {
                        SoundManager.playClick()
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                    })
            // play again button
            Image(
                painter = painterResource(id = R.drawable.button_playagain),
                contentDescription = "Play Again",
                modifier = Modifier
                    .size(100.dp)
                    .align(alignment = Alignment.Bottom)
                    .padding(end = 10.dp)
                    .clickable {
                        SoundManager.playClick()
                        val intent = Intent(context, LevelsActivity::class.java)
                        context.startActivity(intent)
                    })
        }
    }
}