package com.griffith.classic_labyrinth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun InGameMenuOverlay(
    onResume: () -> Unit, onSettings: () -> Unit, onMainMenu: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))// dimmed the gameAct BG
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {}), contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.width(400.dp),
            contentAlignment = Alignment.Center
        ) {
            // placeholder
            // overlay backgrounf of the ingame setting
            Image(
                painter = painterResource(id = R.drawable.placeholder_place),
                contentDescription = "Menu Background",
                modifier = Modifier
                    .height(700.dp)
                    .width(500.dp),
                contentScale = ContentScale.FillBounds
            )

            Column(
                modifier = Modifier.padding(vertical = 32.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Main Menu button
                Image(
                    painter = painterResource(id = R.drawable.button_mainmenu),
                    contentDescription = "Main Menu",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .clickable {
                            SoundManager.playClick()
                            onMainMenu()
                        },
                    contentScale = ContentScale.Fit
                )
                // setting button
                Image(
                    painter = painterResource(id = R.drawable.button_settingtext),
                    contentDescription = "Settings",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .clickable {
                            SoundManager.playClick()
                            onSettings()
                        },
                    contentScale = ContentScale.Fit
                )
                // resume button
                Image(
                    painter = painterResource(id = R.drawable.button_resume),
                    contentDescription = "Resume",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .clickable {
                            SoundManager.playClick()
                            onResume()
                        },
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}
