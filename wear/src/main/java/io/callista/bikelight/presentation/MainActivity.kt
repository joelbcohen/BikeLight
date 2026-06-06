package io.callista.bikelight.presentation

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.callista.bikelight.presentation.theme.BikeLightTheme

class MainActivity : ComponentActivity() {
    private val prefs by lazy { getSharedPreferences("bikelight_prefs", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val color1 = prefs.getInt("color1", 0)
        val color2 = prefs.getInt("color2", 1)
        val speed = prefs.getInt("speed", PulseSpeed.MEDIUM.ordinal)
        val pattern = prefs.getInt("pattern", PulsePattern.PULSE.ordinal)

        setContent {
            BikeLightTheme {
                BikeLightApp(
                    initialColor1Index = color1,
                    initialColor2Index = color2,
                    initialSpeedIndex = speed,
                    initialPatternIndex = pattern,
                    onSave = { c1, c2, s, p ->
                        prefs.edit()
                            .putInt("color1", c1)
                            .putInt("color2", c2)
                            .putInt("speed", s)
                            .putInt("pattern", p)
                            .apply()
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Force maximum brightness — critical for visibility on handlebars
        window.attributes = window.attributes.apply {
            screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL
        }
    }
}
