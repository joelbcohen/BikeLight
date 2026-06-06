package io.callista.bikelight.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight

@Composable
fun SettingsScreen(
    color1Index: Int,
    color2Index: Int,
    speedIndex: Int,
    patternIndex: Int,
    onColor1Change: (Int) -> Unit,
    onColor2Change: (Int) -> Unit,
    onSpeedChange: (Int) -> Unit,
    onPatternChange: (Int) -> Unit,
    onBack: () -> Unit
) {
    val listState = rememberTransformingLazyColumnState()
    val transformationSpec = rememberTransformationSpec()

    AppScaffold {
        ScreenScaffold(
            scrollState = listState,
            edgeButton = {
                EdgeButton(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Text("Done")
                }
            }
        ) { contentPadding ->
            TransformingLazyColumn(state = listState, contentPadding = contentPadding) {
                item {
                    ListHeader(
                        modifier = Modifier.fillMaxWidth()
                            .transformedHeight(this, transformationSpec),
                        transformation = SurfaceTransformation(transformationSpec)
                    ) {
                        Text("BikeLight")
                    }
                }
                item {
                    Button(
                        onClick = { onColor1Change((color1Index + 1) % PRESET_COLORS.size) },
                        modifier = Modifier.fillMaxWidth()
                            .transformedHeight(this, transformationSpec),
                        transformation = SurfaceTransformation(transformationSpec)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Primary", modifier = Modifier.weight(1f))
                            ColorSwatch(PRESET_COLORS[color1Index].second)
                            Spacer(Modifier.width(4.dp))
                            Text(PRESET_COLORS[color1Index].first)
                        }
                    }
                }
                item {
                    Button(
                        onClick = { onColor2Change((color2Index + 1) % PRESET_COLORS.size) },
                        modifier = Modifier.fillMaxWidth()
                            .transformedHeight(this, transformationSpec),
                        transformation = SurfaceTransformation(transformationSpec)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Flash", modifier = Modifier.weight(1f))
                            ColorSwatch(PRESET_COLORS[color2Index].second)
                            Spacer(Modifier.width(4.dp))
                            Text(PRESET_COLORS[color2Index].first)
                        }
                    }
                }
                item {
                    Button(
                        onClick = { onSpeedChange((speedIndex + 1) % PulseSpeed.entries.size) },
                        modifier = Modifier.fillMaxWidth()
                            .transformedHeight(this, transformationSpec),
                        transformation = SurfaceTransformation(transformationSpec)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Speed", modifier = Modifier.weight(1f))
                            Text(PulseSpeed.entries[speedIndex].label)
                        }
                    }
                }
                item {
                    Button(
                        onClick = { onPatternChange((patternIndex + 1) % PulsePattern.entries.size) },
                        modifier = Modifier.fillMaxWidth()
                            .transformedHeight(this, transformationSpec),
                        transformation = SurfaceTransformation(transformationSpec)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Pattern", modifier = Modifier.weight(1f))
                            Text(PulsePattern.entries[patternIndex].label)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ColorSwatch(color: Color) {
    Box(
        modifier = Modifier
            .size(12.dp)
            .clip(CircleShape)
            .background(if (color == Color.Black) Color.DarkGray else color)
    )
}
