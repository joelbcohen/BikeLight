package io.callista.bikelight.presentation

import androidx.compose.ui.graphics.Color

enum class PulsePattern(val label: String) {
    PULSE("Pulse"),
    BREATHE("Breathe"),
    FLASH("Flash"),
    HEARTBEAT("Heartbeat"),
    SOS("SOS")
}

enum class PulseSpeed(val label: String, val halfPeriodMs: Int) {
    SLOW("Slow", 2000),
    MEDIUM("Medium", 1000),
    FAST("Fast", 500),
    STROBE("Strobe", 150)
}

val PRESET_COLORS = listOf(
    "White" to Color.White,
    "Red" to Color.Red,
    "Yellow" to Color.Yellow,
    "Green" to Color.Green,
    "Blue" to Color.Blue,
    "Amber" to Color(0xFFFFBF00.toInt()),
    "Purple" to Color(0xFF8B00FF.toInt()),
    "Off" to Color.Black
)
