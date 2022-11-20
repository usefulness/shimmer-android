package io.github.usefulness.shimmer.android

import android.animation.ValueAnimator
import android.graphics.Color
import io.github.usefulness.shimmer.android.Shimmer.Shape

internal val Shimmer.colors: IntArray
    get() {
        fun Float.mixWith(reference: Int): Int {
            val intAlpha = (coerceIn(0f, 1f) * 0xFF).toInt()
            return intAlpha shl 24 or (reference and 0x00FFFFFF)
        }

        val mixedBaseColor: Int = baseAlpha.mixWith(reference = baseColor)
        val mixedHighlightColor: Int = highlightAlpha.mixWith(reference = highlightColor)

        return when (shape) {
            Shape.Linear -> intArrayOf(
                mixedBaseColor,
                mixedHighlightColor,
                mixedHighlightColor,
                mixedBaseColor,
            )

            Shape.Radial -> intArrayOf(
                mixedHighlightColor,
                mixedHighlightColor,
                mixedBaseColor,
                mixedBaseColor,
            )
        }
    }

private val Shimmer.baseColor
    get() = when (style) {
        Shimmer.Style.Alpha -> 0x4cffffff
        is Shimmer.Style.Colored -> style.baseColor
    }

private val Shimmer.highlightColor
    get() = when (style) {
        Shimmer.Style.Alpha -> Color.WHITE
        is Shimmer.Style.Colored -> style.highlightColor
    }

internal val Shimmer.RepeatMode.valueAnimatorValue
    get() = when (this) {
        Shimmer.RepeatMode.Restart -> ValueAnimator.RESTART
        Shimmer.RepeatMode.Reverse -> ValueAnimator.REVERSE
    }

internal val Shimmer.positions
    get() = when (shape) {
        Shape.Linear -> floatArrayOf(
            ((1f - intensity - dropoff) / 2f).coerceIn(0f, 1f),
            ((1f - intensity - 0.001f) / 2f).coerceIn(0f, 1f),
            ((1f + intensity + 0.001f) / 2f).coerceIn(0f, 1f),
            ((1f + intensity + dropoff) / 2f).coerceIn(0f, 1f),
        )

        Shape.Radial -> floatArrayOf(
            0f,
            intensity.coerceIn(0f, 1f),
            (intensity + dropoff).coerceIn(0f, 1f),
            1f,
        )
    }

internal val Shimmer.Direction.isVertical
    get() = when (this) {
        Shimmer.Direction.TopToBottom,
        Shimmer.Direction.BottomToTop,
        -> true

        Shimmer.Direction.LeftToRight,
        Shimmer.Direction.RightToLeft,
        -> false
    }
