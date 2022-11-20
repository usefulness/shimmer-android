package io.github.usefulness.shimmer.android

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class Shimmer(
    val style: Style = Style.Alpha,
    val direction: Direction = Direction.LeftToRight,
    @FloatRange(from = 0.0, to = 1.0) val baseAlpha: Float = 0.3f,
    @FloatRange(from = 0.0, to = 1.0) val highlightAlpha: Float = 1f,
    val shape: Shape = Shape.Linear,
    val intensity: Float = 0f,
    val dropoff: Float = 0.5f,
    val tilt: Float = 20f,
    val clipToChildren: Boolean = true,
    val autoStart: Boolean = true,
    val repeatCount: Int = 1000,
    val repeatMode: RepeatMode = RepeatMode.Restart,
    val animationDuration: Duration = 1.seconds,
    val repeatDelay: Duration = 0.seconds,
    val startDelay: Duration = 0.seconds,
) {

    init {
        require(intensity >= 0f) { "Given invalid intensity value: $intensity" }
        require(dropoff >= 0f) { "Given invalid dropoff value: $dropoff" }
    }

    sealed class Style {

        object Alpha : Style()

        data class Colored(
            @ColorInt val baseColor: Int = Color.WHITE,
            @ColorInt val highlightColor: Int = Color.WHITE,
        ) : Style()
    }

    /** The shape of the shimmer's highlight. By default LINEAR is used.  */
    enum class Shape(val attrValue: Int) {
        /** Linear gives a ray reflection effect.  */
        Linear(0),

        /** Radial gives a spotlight effect.  */
        Radial(1),
        ;

        companion object {

            fun fromAttr(value: Int) = values().first { it.attrValue == value }
        }
    }

    /** Direction of the shimmer's sweep.  */
    enum class Direction(val attrValue: Int) {
        LeftToRight(0),
        TopToBottom(1),
        RightToLeft(2),
        BottomToTop(3),
        ;

        companion object {

            fun fromAttr(value: Int) = values().first { it.attrValue == value }
        }
    }

    enum class RepeatMode(val attrValue: Int) {
        Restart(0),
        Reverse(1),
        ;

        companion object {

            fun fromAttr(value: Int) = values().first { it.attrValue == value }
        }
    }
}
