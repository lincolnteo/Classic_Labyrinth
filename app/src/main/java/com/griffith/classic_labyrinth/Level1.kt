// Lincoln Teo Ming Kern 3122215
// GitHub repo: https://github.com/lincolnteo/Classic_Labyrinth
package com.griffith.classic_labyrinth

import android.content.Context
import android.content.Intent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

object Level1 {
    const val LEVEL_ID = 1

    // Draws on Canvas
    // The wall start zone and finish line
    fun drawCanvas(drawScope: DrawScope, screenWidth: Float, screenHeight: Float) {
        val topWallY = screenHeight * 0.35f
        val bottomWallY = screenHeight * 0.65f
        val startZoneX = 220f
        val finishLineX = screenWidth - 220f

        // Top horizontal wall
        drawScope.drawLine(
            color = Color.Black,
            strokeWidth = 10f,
            start = Offset(0f, topWallY),
            end = Offset(screenWidth, topWallY)
        )
        // Bottom horizontal wall
        drawScope.drawLine(
            color = Color.Black,
            strokeWidth = 10f,
            start = Offset(0f, bottomWallY),
            end = Offset(screenWidth, bottomWallY)
        )
        // Start zone purple line
        drawScope.drawLine(
            color = Color.Red,
            strokeWidth = 8f,
            start = Offset(startZoneX, topWallY),
            end = Offset(startZoneX, bottomWallY)
        )
        // Finish line green line
        drawScope.drawLine(
            color = Color.Green,
            strokeWidth = 10f,
            start = Offset(finishLineX, topWallY),
            end = Offset(finishLineX, bottomWallY)
        )
    }

    // Handles Level 1 physics boundaries, Y clamping, and victory triggering
    fun updatePhysics(
        context: Context,// Activity context for saving CSV data and launching EndGameActivity
        ballX: Float,// Current ball horizontal X coordinate
        ballY: Float,// Current ball vertical Y coordinate
        radius: Float,// Ball radius size
        screenWidth: Float,// Total screen width in pixels
        screenHeight: Float,// Total screen height in pixels
        hasWon: Boolean,// Current win status flag
        startTimeMillis: Long,// Timestamp when level started (for timer calculation)
        onUpdateY: (Float) -> Unit,// Callback to clamp ball Y position on wall collision
        onStopY: () -> Unit,// Callback to zero out vertical speed on wall hit
        onWinState: () -> Unit // Callback to lock victory state when crossing finish line
    ) {
        val topWallY = screenHeight * 0.35f
        val bottomWallY = screenHeight * 0.65f
        val finishLineX = screenWidth - 220f

        // Restrict ball between top and bottom walls
        if (ballY - radius < topWallY) {
            onUpdateY(topWallY + radius)
            onStopY() // reset speed to 0 simulate a wall
        } else if (ballY + radius > bottomWallY) {
            onUpdateY(bottomWallY - radius)
            onStopY()
        }

        // Win condition check: Ball crosses green finish line
        if (ballX + radius >= finishLineX && !hasWon) {
            onWinState() // set hasWon to true in GameAct
            onVictory(context, startTimeMillis)
        }
    }

    // Handles victory sound, updates CSV , launches EndGameActivity
    private fun onVictory(context: Context, startTimeMillis: Long) {
        SoundManager.playVictory() // play fx

        // Calculate completion time
        val elapsedTimeSec = (System.currentTimeMillis() - startTimeMillis) / 1000f
        val timeFormatted = String.format(java.util.Locale.ENGLISH, "%.2fs", elapsedTimeSec)

        // Complete level 1 in level_progress.csv, update Personal Best if faster, and unlock level 2
        csvManager.completeLevel(context, LEVEL_ID, elapsedTimeSec)

        // Read level details and reward coins and XP
        val levelInfo = csvManager.loadAllLevelDetails(context)[LEVEL_ID]
        val coinsReward = levelInfo?.rewards ?: 0
        val xpReward = levelInfo?.xp ?: 0
        val objectiveStr = levelInfo?.objective ?: "Victory Demo"
        val personalBestStr = levelInfo?.personalBest ?: timeFormatted

        val playerSettings = csvManager.loadPlayerSettings(context)
        val currentCoins = playerSettings["coins"]?.toIntOrNull() ?: 0
        val currentXp = playerSettings["current_xp"]?.toIntOrNull() ?: 0
        var playerLevel = playerSettings["level"]?.toIntOrNull() ?: 1

        val newCoins = currentCoins + coinsReward
        var newXp = currentXp + xpReward

        // Level up if XP reaches 100
        if (newXp >= 100) {
            playerLevel += 1
            newXp -= 100
        }
        // save updates player settings
        csvManager.savePlayerSetting(context, "coins", newCoins.toString())
        csvManager.savePlayerSetting(context, "current_xp", newXp.toString())
        csvManager.savePlayerSetting(context, "level", playerLevel.toString())

        // Launch EndGameActivity with level stats
        SoundManager.isSwitchingScreens = true
        val intent = Intent(context, EndGameActivity::class.java).apply {
            putExtra("EXTRA_LEVEL_ID", LEVEL_ID)
            putExtra("EXTRA_PLAYER_LEVEL", playerLevel)
            putExtra("EXTRA_OBJECTIVE", objectiveStr)
            putExtra("EXTRA_REWARDS", coinsReward)
            putExtra("EXTRA_XP_REWARD", xpReward)
            putExtra("EXTRA_CURRENT_XP", newXp)
            putExtra("EXTRA_TIME", timeFormatted)
            putExtra("EXTRA_PERSONAL_BEST", personalBestStr)
        }
        context.startActivity(intent)
        if (context is android.app.Activity) {
            context.finish()
        }
    }
}
