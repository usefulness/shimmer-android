package io.github.usefulness.shimmer.android

import android.animation.ValueAnimator
import android.content.res.TypedArray
import android.graphics.Color
import io.github.usefulness.shimmer.android.Shimmer.Direction
import io.github.usefulness.shimmer.android.Shimmer.Shape

internal fun TypedArray.buildShimmer(): Shimmer {
    val direction = getInt(R.styleable.ShimmerFrameLayout_shimmer_direction, Direction.LeftToRight.ordinal)
    val baseAlpha = getFloat(R.styleable.ShimmerFrameLayout_shimmer_base_alpha, 0.3f)
    val highlightAlpha = getFloat(R.styleable.ShimmerFrameLayout_shimmer_highlight_alpha, 1f)
    val shape = getInt(R.styleable.ShimmerFrameLayout_shimmer_shape, Shape.Linear.ordinal)
    val fixedWidth = getFloat(R.styleable.ShimmerFrameLayout_shimmer_fixed_width, 0f)
    val fixedHeight = getFloat(R.styleable.ShimmerFrameLayout_shimmer_fixed_height, 0f)
    val widthRatio = getFloat(R.styleable.ShimmerFrameLayout_shimmer_width_ratio, 1f)
    val heightRatio = getFloat(R.styleable.ShimmerFrameLayout_shimmer_height_ratio, 1f)
    val intensity = getFloat(R.styleable.ShimmerFrameLayout_shimmer_intensity, 0f)
    val dropoff = getFloat(R.styleable.ShimmerFrameLayout_shimmer_dropoff, 0.5f)
    val tilt = getFloat(R.styleable.ShimmerFrameLayout_shimmer_tilt, 20f)
    val clipToChildren = getBoolean(R.styleable.ShimmerFrameLayout_shimmer_clip_to_children, true)
    val autoStart = getBoolean(R.styleable.ShimmerFrameLayout_shimmer_auto_start, true)
    val repeatCount = getInt(R.styleable.ShimmerFrameLayout_shimmer_repeat_count, 1000)
    val repeatMode = getInt(R.styleable.ShimmerFrameLayout_shimmer_repeat_mode, ValueAnimator.RESTART)
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
        fixedWidth = fixedWidth,
        fixedHeight = fixedHeight,
        widthRatio = widthRatio,
        heightRatio = heightRatio,
        intensity = intensity,
        dropoff = dropoff,
        tilt = tilt,
        clipToChildren = clipToChildren,
        autoStart = autoStart,
        repeatCount = repeatCount,
        repeatMode = repeatMode,
        animationDuration = animationDuration,
        repeatDelay = repeatDelay,
        startDelay = startDelay,
    )
}
