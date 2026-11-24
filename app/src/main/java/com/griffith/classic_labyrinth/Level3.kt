// Lincoln Teo Ming Kern 3122215
// GitHub repo: https://github.com/lincolnteo/Classic_Labyrinth
package com.griffith.classic_labyrinth

import android.content.Context
import android.content.Intent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

object Level3 {

    const val LEVEL_ID = 3

    // Draws on Canvas
    // The wall start zone and finish line and Trap hole
    fun drawCanvas(drawScope: DrawScope, screenWidth: Float, screenHeight: Float) {
        val startZoneX = 180f
        val finishLineX = screenWidth - 180f

        // Wall 1 Hanging from top down
        val wall1X = screenWidth * 0.32f
        val wall1Top = 0f
        val wall1Bottom = screenHeight * 0.75f

        // Wall 2  floating middle wall
        val wall2X = screenWidth * 0.55f
        val wall2Top = screenHeight * 0.15f
        val wall2Bottom = screenHeight * 0.85f

        // Wall 3 Hanging from top down
        val wall3X = screenWidth * 0.78f
        val wall3Top = 0f
        val wall3Bottom = screenHeight * 0.75f

        //Hole Trap near at bottom gap
        val trapCenter = Offset(wall2X, screenHeight * 0.92f)
        val trapRadius = 70f

        // Start zone red line
        drawScope.drawLine(
            color = Color.Red,
            strokeWidth = 8f,
            start = Offset(startZoneX, 0f),
            end = Offset(startZoneX, screenHeight)
        )
        // Wall 1 Hanging from top down
        drawScope.drawLine(
            color = Color.Black,
            strokeWidth = 12f,
            start = Offset(wall1X, wall1Top),
            end = Offset(wall1X, wall1Bottom)
        )
        // Wall 2  floating middle wall
        drawScope.drawLine(
            color = Color.Black,
            strokeWidth = 12f,
            start = Offset(wall2X, wall2Top),
            end = Offset(wall2X, wall2Bottom)
        )
        // Wall 3 Hanging from top down
        drawScope.drawLine(
            color = Color.Black,
            strokeWidth = 12f,
            start = Offset(wall3X, wall3Top),
            end = Offset(wall3X, wall3Bottom)
        )
        // Hole Trap in the middle Dark grey outer ring
        drawScope.drawCircle(
            color = Color.DarkGray,
            radius = trapRadius,
            center = trapCenter
        )
        // black inner
        drawScope.drawCircle(
            color = Color.Black,
            radius = trapRadius * 0.8f,
            center = trapCenter
        )

        // Finish line green line
        drawScope.drawLine(
            color = Color.Green,
            strokeWidth = 10f,
            start = Offset(finishLineX, 0f),
            end = Offset(finishLineX, screenHeight)
        )
    }

    // Checks full-board physics, wall collisions, hole trap loss, and finish line win
    fun updatePhysics(
        context: Context,// Activity context for saving CSV data and launching EndGameActivity
        ballX: Float,// Current ball horizontal X coordinate
        ballY: Float,// Current ball vertical Y coordinate
        radius: Float,// Ball radius size
        screenWidth: Float,// Total screen width in pixels
        screenHeight: Float,// Total screen height in pixels
        hasWon: Boolean,// Current win status flag
        hasLost: Boolean, // Current loss status flag
        startTimeMillis: Long,// Timestamp when level started (for timer calculation)
        onUpdateX: (Float) -> Unit,
        onUpdateY: (Float) -> Unit,// Callback to clamp ball Y position on wall collision
        onStopX: () -> Unit,// Callback to zero out vertical speed on wall hit
        onStopY: () -> Unit,// Callback to zero out vertical speed on wall hit
        onWinState: () -> Unit, // Callback to lock victory state when crossing finish line
        onLossState: () -> Unit // Callback to lock loss state when falling in trap
    ) {
        val finishLineX = screenWidth - 180f

        val wall1X = screenWidth * 0.32f
        val wall1Bottom = screenHeight * 0.75f
        val wall2X = screenWidth * 0.55f
        val wall2Top = screenHeight * 0.15f
        val wall2Bottom = screenHeight * 0.85f
        val wall3X = screenWidth * 0.78f
        val wall3Bottom = screenHeight * 0.75f

        val trapCenter = Offset(wall2X, screenHeight * 0.92f)
        val trapRadius = 70f

        // Screen boundary limits
        if (ballX - radius < 0f) {
            onUpdateX(radius)
            onStopX()
        } else if (ballX + radius > screenWidth) {
            onUpdateX(screenWidth - radius)
            onStopX()
        }

        if (ballY - radius < 0f) {
            onUpdateY(radius)
            onStopY()
        } else if (ballY + radius > screenHeight) {
            onUpdateY(screenHeight - radius)
            onStopY()
        }

        // Collision with Wall 1 (x = wall1X, y from 0 to wall1Bottom)
        if (kotlin.math.abs(ballX - wall1X) <= radius && ballY <= wall1Bottom) {
            if (ballX < wall1X) {
                onUpdateX(wall1X - radius)
            } else {
                onUpdateX(wall1X + radius)
            }
            onStopX()
        }

        // Collision with Wall 2
        // middle wall: x = wall2X, y between wall2Top and wall2Bottom
        if (kotlin.math.abs(ballX - wall2X) <= radius && ballY >= wall2Top && ballY <= wall2Bottom) {
            if (ballX < wall2X) {
                onUpdateX(wall2X - radius)
            } else {
                onUpdateX(wall2X + radius)
            }
            onStopX()
        }

        // Collision with Wall 3
        // x = wall3X, y from 0 to wall3Bottom
        if (kotlin.math.abs(ballX - wall3X) <= radius && ballY <= wall3Bottom) {
            if (ballX < wall3X) {
                onUpdateX(wall3X - radius)
            } else {
                onUpdateX(wall3X + radius)
            }
            onStopX()
        }

        // Loss condition: Larger Hole trap
        val distanceToTrap = (Offset(ballX, ballY) - trapCenter).getDistance()
        if (distanceToTrap <= radius + trapRadius && !hasLost && !hasWon) {
            onLossState()
            onLoss(context)
            return
        }

        // Win condition: Finish line
        if (ballX + radius >= finishLineX && !hasWon && !hasLost) {
            onWinState()
            onVictory(context, startTimeMillis)
        }
    }

    // Handles Level 3 loss: plays loss sound and launches EndGameActivity
    private fun onLoss(context: Context) {
        SoundManager.playLoss()

        val levelInfo = csvManager.loadAllLevelDetails(context)[LEVEL_ID]
        val objectiveStr = levelInfo?.objective ?: "Simple Demo"
        val personalBestStr = levelInfo?.personalBest

        val playerSettings = csvManager.loadPlayerSettings(context)
        val playerLevel = playerSettings["level"]?.toIntOrNull() ?: 1

        SoundManager.isSwitchingScreens = true
        val intent = Intent(context, EndGameActivity::class.java).apply {
            putExtra("EXTRA_LEVEL_ID", LEVEL_ID)
            putExtra("EXTRA_PLAYER_LEVEL", playerLevel)
            putExtra("EXTRA_OBJECTIVE", "$objectiveStr (Failed - Fell in Trap)")
            putExtra("EXTRA_REWARDS", 0)
            putExtra("EXTRA_XP_REWARD", 0)
            putExtra("EXTRA_TIME", "Failed")
            putExtra("EXTRA_PERSONAL_BEST", personalBestStr)
        }
        context.startActivity(intent)
        if (context is android.app.Activity) {
            context.finish()
        }
    }

    // Handles victory sound, updates CSV , launches EndGameActivity
    private fun onVictory(context: Context, startTimeMillis: Long) {
        SoundManager.playVictory() // play fx

        // Calculate completion time
        val elapsedTimeSec = (System.currentTimeMillis() - startTimeMillis) / 1000f
        val timeFormatted = String.format(java.util.Locale.ENGLISH, "%.2fs", elapsedTimeSec)

        // Complete level 2 in level_progress.csv, update Personal Best if faster, and unlock level 3
        csvManager.completeLevel(context, LEVEL_ID, elapsedTimeSec)

        // Read level details and reward coins and XP
        val levelInfo = csvManager.loadAllLevelDetails(context)[LEVEL_ID]
        val coinsReward = levelInfo?.rewards ?: 250
        val xpReward = levelInfo?.xp ?: 110
        val objectiveStr = levelInfo?.objective ?: "Loss Demo"
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
