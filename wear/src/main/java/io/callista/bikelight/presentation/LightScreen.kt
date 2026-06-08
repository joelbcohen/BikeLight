package io.callista.bikelight.presentation

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import kotlinx.coroutines.delay

private val SineEasing = CubicBezierEasing(0.37f, 0f, 0.63f, 1f)

@Composable
fun LightScreen(
    color1: Color,
    color2: Color,
    speed: PulseSpeed,
    pattern: PulsePattern,
    soundEnabled: Boolean,
    onTap: () -> Unit
) {
    val animFraction = remember { Animatable(0f) }
    val toneGenerator = remember { mutableStateOf<ToneGenerator?>(null) }

    DisposableEffect(soundEnabled) {
        if (soundEnabled) {
            toneGenerator.value = try {
                ToneGenerator(AudioManager.STREAM_ALARM, ToneGenerator.MAX_VOLUME)
            } catch (e: RuntimeException) {
                null
            }
        }
        onDispose {
            toneGenerator.value?.release()
            toneGenerator.value = null
        }
    }

    LaunchedEffect(speed, pattern) {
        val toneDurationMs = minOf(100, speed.halfPeriodMs / 4).coerceAtLeast(20)
        animFraction.snapTo(0f)
        when (pattern) {
            PulsePattern.PULSE -> {
                while (true) {
                    animFraction.animateTo(1f, tween(speed.halfPeriodMs, easing = LinearEasing))
                    toneGenerator.value?.startTone(ToneGenerator.TONE_PROP_BEEP, toneDurationMs)
                    animFraction.animateTo(0f, tween(speed.halfPeriodMs, easing = LinearEasing))
                }
            }
            PulsePattern.BREATHE -> {
                // Sine-wave easing: slow at both ends, faster through the middle
                while (true) {
                    animFraction.animateTo(1f, tween(speed.halfPeriodMs, easing = SineEasing))
                    toneGenerator.value?.startTone(ToneGenerator.TONE_PROP_BEEP, toneDurationMs)
                    animFraction.animateTo(0f, tween(speed.halfPeriodMs, easing = SineEasing))
                }
            }
            PulsePattern.FLASH -> {
                while (true) {
                    animFraction.snapTo(1f)
                    toneGenerator.value?.startTone(ToneGenerator.TONE_PROP_BEEP, toneDurationMs)
                    delay(speed.halfPeriodMs.toLong())
                    animFraction.snapTo(0f)
                    delay(speed.halfPeriodMs.toLong())
                }
            }
            PulsePattern.HEARTBEAT -> {
                // Fixed beat duration for crisp feel; speed controls the rate (pause length)
                val beat = 100L
                val intraGap = 150L
                val interGap = (speed.halfPeriodMs * 2L - beat * 2 - intraGap).coerceAtLeast(200L)
                while (true) {
                    animFraction.snapTo(1f)
                    toneGenerator.value?.startTone(ToneGenerator.TONE_PROP_BEEP, beat.toInt())
                    delay(beat)
                    animFraction.snapTo(0f); delay(intraGap)
                    animFraction.snapTo(1f)
                    toneGenerator.value?.startTone(ToneGenerator.TONE_PROP_BEEP, beat.toInt())
                    delay(beat)
                    animFraction.snapTo(0f); delay(interGap)
                }
            }
            PulsePattern.SOS -> {
                // Morse: ...---... with speed-scaled unit, minimum 80ms
                val unit = (speed.halfPeriodMs / 3L).coerceAtLeast(80L)
                while (true) {
                    repeat(3) { // S: dot dot dot
                        animFraction.snapTo(1f)
                        toneGenerator.value?.startTone(ToneGenerator.TONE_PROP_BEEP, unit.toInt())
                        delay(unit)
                        animFraction.snapTo(0f); delay(unit)
                    }
                    delay(unit * 2) // complete 3-unit letter gap
                    repeat(3) { // O: dash dash dash
                        animFraction.snapTo(1f)
                        toneGenerator.value?.startTone(ToneGenerator.TONE_PROP_BEEP, (unit * 3).toInt())
                        delay(unit * 3)
                        animFraction.snapTo(0f); delay(unit)
                    }
                    delay(unit * 2) // complete 3-unit letter gap
                    repeat(3) { // S: dot dot dot
                        animFraction.snapTo(1f)
                        toneGenerator.value?.startTone(ToneGenerator.TONE_PROP_BEEP, unit.toInt())
                        delay(unit)
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
