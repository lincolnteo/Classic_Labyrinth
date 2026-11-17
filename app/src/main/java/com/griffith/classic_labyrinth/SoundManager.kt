package com.griffith.classic_labyrinth

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
// Using object instead of class creates a Singleton
// so music keeps playing smoothly as you switch activites
object SoundManager {
    // Media player for long running background music
    private var bgPlayer: MediaPlayer? = null

    // SoundPool for short, quick sound effects (like clicks or wall bumps)
    private var soundPool: SoundPool? = null
    private var clickSoundId: Int = 0

    // Global volume controls  0.0f to 1.0f
    var masterVolume = 1.0f
    var musicVolume = 1.0f
    var fxVolume = 1.0f

    // var for detect internal navigation so music wont pause when changing screens
    var isSwitchingScreens = false

    fun init(context: Context) {
        // Setup SoundPool for FX so it wont cut off the audio
        if (soundPool == null) {
            val audioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

            soundPool = SoundPool.Builder().setMaxStreams(5) // Max simultaneous sounds
                .setAudioAttributes(audioAttributes).build()

            // Load the button click sound into memory
            clickSoundId = soundPool?.load(context, R.raw.fx_button_click, 1) ?: 0
        }

        // Setup MediaPlayer for Background Music
        if (bgPlayer == null) {
            bgPlayer = MediaPlayer.create(context, R.raw.background_music).apply {
                isLooping = true // music loop
                setVolume(masterVolume * musicVolume, masterVolume * musicVolume)
            }
        }
    }

    //Plays the UI click sound effect.
    fun playClick() {
        val volume = masterVolume * fxVolume
        soundPool?.play(clickSoundId, volume, volume, 1, 0, 1f)
    }

    //Pauses the background music

    fun pauseMusic() {
        if (bgPlayer?.isPlaying == true) {
            bgPlayer?.pause()
        }
    }

    // Resumes the background music from where it left off

    fun resumeMusic() {
        bgPlayer?.start()
    }

    //applies the current volume settings to the playing music
    // call this whenever the user adjusts in the settings

    fun updateVolumes() {
        bgPlayer?.setVolume(masterVolume * musicVolume, masterVolume * musicVolume)
    }
}
