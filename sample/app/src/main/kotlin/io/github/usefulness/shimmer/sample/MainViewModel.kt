package io.github.usefulness.shimmer.sample

import androidx.lifecycle.ViewModel
import io.github.usefulness.shimmer.android.Shimmer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.time.Duration.Companion.seconds

class MainViewModel : ViewModel() {

    val config = MutableStateFlow(Shimmer())

    fun buildPresets() = listOf(
        PresetButton(
            name = "Default",
            config = Shimmer(),
        ),
        PresetButton(
            name = "Slow and reverse",
            config = Shimmer(
                animationDuration = 5.seconds,
                repeatMode = Shimmer.RepeatMode.Reverse,
            ),
        ),
        PresetButton(
            name = "Thin, straight and transparent",
            config = Shimmer(
                animationDuration = 3.seconds,
                baseAlpha = 0.1f,
                dropoff = 0.1f,
                tilt = 0f,
            ),
        ),
        PresetButton(
            name = "Sweep angle 90",
            config = Shimmer(
                direction = Shimmer.Direction.TopToBottom,
                tilt = 0f,
            ),
        ),
        PresetButton(
            name = "Spotlight",
            config = Shimmer(
                baseAlpha = 0f,
                animationDuration = 2.seconds,
                dropoff = 0.1f,
                intensity = 0.35f,
                shape = Shimmer.Shape.Radial,
            ),
        ),
        PresetButton(
            name = "Spotlight angle 45",
            config = Shimmer(
                baseAlpha = 0f,
                animationDuration = 2.seconds,
                dropoff = 0.1f,
                intensity = 0.35f,
                tilt = 45f,
                shape = Shimmer.Shape.Radial,
            ),
        ),
        PresetButton(
            name = "Colored",
            config = Shimmer(
                animationDuration = 2.seconds,
                style = Shimmer.Style.Colored(
                    baseColor = 0x86BBD8,
                    highlightColor = 0xF6AE2D,
                ),
                baseAlpha = 0.7f,
                dropoff = 0.3f,
                intensity = 0.35f,
                tilt = 15f,
                shape = Shimmer.Shape.Linear,
            ),
        ),
    )
}

data class PresetButton(
    val name: String,
    val config: Shimmer,
)
