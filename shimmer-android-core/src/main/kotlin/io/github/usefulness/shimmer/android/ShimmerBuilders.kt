package io.github.usefulness.shimmer.android

import android.content.res.TypedArray
import android.graphics.Color
import io.github.usefulness.shimmer.android.Shimmer.Direction
import io.github.usefulness.shimmer.android.Shimmer.Shape
import kotlin.time.Duration.Companion.milliseconds

internal fun TypedArray.buildShimmer(): Shimmer {
    val direction = getInt(R.styleable.ShimmerFrameLayout_shimmer_direction, Direction.LeftToRight.attrValue)
    val baseAlpha = getFloat(R.styleable.ShimmerFrameLayout_shimmer_base_alpha, 0.3f)
    val highlightAlpha = getFloat(R.styleable.ShimmerFrameLayout_shimmer_highlight_alpha, 1f)
    val shape = getInt(R.styleable.ShimmerFrameLayout_shimmer_shape, Shape.Linear.attrValue)
    val intensity = getFloat(R.styleable.ShimmerFrameLayout_shimmer_intensity, 0f)
    val dropoff = getFloat(R.styleable.ShimmerFrameLayout_shimmer_dropoff, 0.5f)
    val tilt = getFloat(R.styleable.ShimmerFrameLayout_shimmer_tilt, 20f)
    val clipToChildren = getBoolean(R.styleable.ShimmerFrameLayout_shimmer_clip_to_children, true)
    val autoStart = getBoolean(R.styleable.ShimmerFrameLayout_shimmer_auto_start, true)
    val repeatCount = getInt(R.styleable.ShimmerFrameLayout_shimmer_repeat_count, 1000)
    val repeatMode = getInt(R.styleable.ShimmerFrameLayout_shimmer_repeat_mode, Shimmer.RepeatMode.Restart.attrValue)
    val animationDuration = getInt(R.styleable.ShimmerFrameLayout_shimmer_duration, 1000).toLong()
    val repeatDelay = getInt(R.styleable.ShimmerFrameLayout_shimmer_repeat_delay, 0).toLong()
    val startDelay = getInt(R.styleable.ShimmerFrameLayout_shimmer_start_delay, 0).toLong()

    val style = if (getBoolean(R.styleable.ShimmerFrameLayout_shimmer_colored, false)) {
        val baseColor = getColor(R.styleable.ShimmerFrameLayout_shimmer_base_color, Color.WHITE)
        val highlightColor = getColor(R.styleable.ShimmerFrameLayout_shimmer_highlight_color, Color.WHITE)

        Shimmer.Style.Colored(
            baseColor = baseColor,
            highlightColor = highlightColor,
        )
    } else {
        Shimmer.Style.Alpha
    }

    return Shimmer(
        style = style,
        direction = Direction.fromAttr(direction),
        baseAlpha = baseAlpha,
        highlightAlpha = highlightAlpha,
        shape = Shape.fromAttr(shape),
        intensity = intensity,
        dropoff = dropoff,
        tilt = tilt,
        clipToChildren = clipToChildren,
        autoStart = autoStart,
        repeatCount = repeatCount,
        repeatMode = Shimmer.RepeatMode.fromAttr(repeatMode),
        animationDuration = animationDuration.milliseconds,
        repeatDelay = repeatDelay.milliseconds,
        startDelay = startDelay.milliseconds,
    )
}
