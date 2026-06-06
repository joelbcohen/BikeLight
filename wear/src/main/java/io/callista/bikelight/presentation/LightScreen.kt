package io.callista.bikelight.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import kotlinx.coroutines.delay

@Composable
fun LightScreen(
    color1: Color,
    color2: Color,
    speed: PulseSpeed,
    pattern: PulsePattern,
    onTap: () -> Unit
) {
    val animFraction = remember { Animatable(0f) }

    LaunchedEffect(speed, pattern) {
        animFraction.snapTo(0f)
        when (pattern) {
            PulsePattern.STEADY -> Unit
            PulsePattern.PULSE -> {
                while (true) {
                    animFraction.animateTo(1f, tween(speed.halfPeriodMs, easing = LinearEasing))
                    animFraction.animateTo(0f, tween(speed.halfPeriodMs, easing = LinearEasing))
                }
            }
            PulsePattern.FLASH -> {
                while (true) {
                    animFraction.snapTo(1f)
                    delay(speed.halfPeriodMs.toLong())
                    animFraction.snapTo(0f)
                    delay(speed.halfPeriodMs.toLong())
                }
            }
            PulsePattern.SOS -> {
                // Morse: ...---... with speed-scaled unit, minimum 80ms
                val unit = (speed.halfPeriodMs / 3L).coerceAtLeast(80L)
                while (true) {
                    repeat(3) { // S: dot dot dot
                        animFraction.snapTo(1f); delay(unit)
                        animFraction.snapTo(0f); delay(unit)
                    }
                    delay(unit * 2) // complete 3-unit letter gap
                    repeat(3) { // O: dash dash dash
                        animFraction.snapTo(1f); delay(unit * 3)
                        animFraction.snapTo(0f); delay(unit)
                    }
                    delay(unit * 2) // complete 3-unit letter gap
                    repeat(3) { // S: dot dot dot
                        animFraction.snapTo(1f); delay(unit)
                        animFraction.snapTo(0f); delay(unit)
                    }
                    delay(unit * 6) // 7-unit word gap
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(lerp(color1, color2, animFraction.value))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onTap
            )
    )
}
