package io.callista.bikelight.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun BikeLightApp(
    initialColor1Index: Int,
    initialColor2Index: Int,
    initialSpeedIndex: Int,
    initialPatternIndex: Int,
    initialSoundEnabled: Boolean,
    onSave: (color1: Int, color2: Int, speed: Int, pattern: Int, sound: Boolean) -> Unit
) {
    var color1Index by remember { mutableIntStateOf(initialColor1Index) }
    var color2Index by remember { mutableIntStateOf(initialColor2Index) }
    var speedIndex by remember { mutableIntStateOf(initialSpeedIndex) }
    var patternIndex by remember { mutableIntStateOf(initialPatternIndex) }
    var soundEnabled by remember { mutableStateOf(initialSoundEnabled) }
    var showSettings by remember { mutableStateOf(false) }

    if (showSettings) {
        BackHandler { showSettings = false }
        SettingsScreen(
            color1Index = color1Index,
            color2Index = color2Index,
            speedIndex = speedIndex,
            patternIndex = patternIndex,
            soundEnabled = soundEnabled,
            onColor1Change = { color1Index = it; onSave(color1Index, color2Index, speedIndex, patternIndex, soundEnabled) },
            onColor2Change = { color2Index = it; onSave(color1Index, color2Index, speedIndex, patternIndex, soundEnabled) },
            onSpeedChange = { speedIndex = it; onSave(color1Index, color2Index, speedIndex, patternIndex, soundEnabled) },
            onPatternChange = { patternIndex = it; onSave(color1Index, color2Index, speedIndex, patternIndex, soundEnabled) },
            onSoundChange = { soundEnabled = it; onSave(color1Index, color2Index, speedIndex, patternIndex, soundEnabled) },
            onBack = { showSettings = false }
        )
    } else {
        LightScreen(
            color1 = PRESET_COLORS[color1Index].second,
            color2 = PRESET_COLORS[color2Index].second,
            speed = PulseSpeed.entries[speedIndex],
            pattern = PulsePattern.entries[patternIndex],
            soundEnabled = soundEnabled,
            onTap = { showSettings = true }
        )
    }
}
