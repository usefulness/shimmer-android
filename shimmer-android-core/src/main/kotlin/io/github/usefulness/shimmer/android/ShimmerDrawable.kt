package io.github.usefulness.shimmer.android

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RadialGradient
import android.graphics.Rect
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.view.animation.LinearInterpolator
import io.github.usefulness.shimmer.android.Shimmer.Shape
import io.github.usefulness.shimmer.android.Shimmer.Direction
import kotlin.math.sqrt
import kotlin.math.tan

internal class ShimmerDrawable : Drawable() {

    private val updateListener = AnimatorUpdateListener { invalidateSelf() }
    private val shimmerPaint = Paint()
    private val drawRect = Rect()
    private val shaderMatrix = Matrix()
    private var valueAnimator: ValueAnimator? = null
    private var staticAnimationProgress = -1f

    init {
        shimmerPaint.isAntiAlias = true
    }

    var shimmer: Shimmer? = null
        set(value) {
            field = value
            if (value != null) {
                shimmerPaint.xfermode = PorterDuffXfermode(
                    when (value.style) {
                        Shimmer.Style.Alpha -> PorterDuff.Mode.DST_IN
                        is Shimmer.Style.Colored -> PorterDuff.Mode.SRC_IN
                    },
                )
            }
            updateShader()
            updateValueAnimator()
            invalidateSelf()
        }

    /**
     * Starts the shimmer animation.
     */
    fun startShimmer() {
        if (!isShimmerStarted && callback != null) {
            valueAnimator?.start()
        }
    }

    /**
     * Stops the shimmer animation.
     */
    fun stopShimmer() {
        if (isShimmerStarted) {
            valueAnimator?.cancel()
        }
    }

    /**
     * Return whether the shimmer animation has been started.
     */
    val isShimmerStarted
        get() = valueAnimator?.isStarted == true

    /**
     * Return whether the shimmer animation is running.
     */
    val isShimmerRunning
        get() = valueAnimator?.isRunning == true

    public override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        drawRect.set(bounds)
        updateShader()
        maybeStartShimmer()
    }

    fun setStaticAnimationProgress(value: Float) {
        if (value == staticAnimationProgress || value < 0f && staticAnimationProgress < 0f) {
            return
        }
        staticAnimationProgress = value.coerceAtMost(1f)
        invalidateSelf()
    }

    fun clearStaticAnimationProgress() {
        setStaticAnimationProgress(-1f)
    }

    override fun draw(canvas: Canvas) {
        val shimmer = shimmer ?: return
        shimmerPaint.shader ?: return

        val tiltTan = tan(Math.toRadians(shimmer.tilt.toDouble())).toFloat()
        val translateHeight = drawRect.height() + tiltTan * drawRect.width()
        val translateWidth = drawRect.width() + tiltTan * drawRect.height()

        val animatedValue = if (staticAnimationProgress < 0f) {
            valueAnimator?.animatedValue as? Float ?: 0f
        } else {
            staticAnimationProgress
        }

        val dx: Float
        val dy: Float
        when (shimmer.direction) {
            Direction.LeftToRight -> {
                dx = offset(-translateWidth, translateWidth, animatedValue)
                dy = 0f
            }

            Direction.RightToLeft -> {
                dx = offset(translateWidth, -translateWidth, animatedValue)
                dy = 0f
            }

            Direction.TopToBottom -> {
                dx = 0f
                dy = offset(-translateHeight, translateHeight, animatedValue)
            }

            Direction.BottomToTop -> {
                dx = 0f
                dy = offset(translateHeight, -translateHeight, animatedValue)
            }
        }
        shaderMatrix.reset()
        shaderMatrix.setRotate(shimmer.tilt, drawRect.width() / 2f, drawRect.height() / 2f)
        shaderMatrix.preTranslate(dx, dy)
        shimmerPaint.shader.setLocalMatrix(shaderMatrix)
        canvas.drawRect(drawRect, shimmerPaint)
    }

    override fun setAlpha(alpha: Int) {
        // No-op, modify the Shimmer object you pass in instead
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        // No-op, modify the Shimmer object you pass in instead
    }

    @Deprecated("This method is no longer used in graphics optimizations")
    override fun getOpacity(): Int {
        val shimmer = shimmer ?: return PixelFormat.TRANSLUCENT

        return if (shimmer.clipToChildren || shimmer.style is Shimmer.Style.Alpha) {
            PixelFormat.TRANSLUCENT
        } else {
            PixelFormat.OPAQUE
        }
    }

    private fun offset(start: Float, end: Float, percent: Float) = start + (end - start) * percent

    private fun updateValueAnimator() {
        val shimmer = shimmer ?: return
        val existingAnimator = valueAnimator
        val wasStartedBefore = existingAnimator?.isStarted == true

        existingAnimator?.apply {
            cancel()
            removeAllUpdateListeners()
        }

        valueAnimator = ValueAnimator.ofFloat(0f, 1f + (shimmer.repeatDelay / shimmer.animationDuration).toFloat()).apply {
            interpolator = LinearInterpolator()
            repeatMode = shimmer.repeatMode
            startDelay = shimmer.startDelay
            repeatCount = shimmer.repeatCount
            duration = shimmer.animationDuration + shimmer.repeatDelay
            addUpdateListener(updateListener)

            if (wasStartedBefore) {
                start()
            }
        }
    }

    fun maybeStartShimmer() {
        val animator = valueAnimator ?: return
        val shimmer = shimmer ?: return
        if (!animator.isStarted && shimmer.autoStart && callback != null) {
            animator.start()
        }
    }

    private fun updateShader() {
        val shimmer = shimmer ?: return
        val boundsWidth = bounds.width().takeIf { it > 0 } ?: return
        val boundsHeight = bounds.height().takeIf { it > 0 } ?: return
        val width = shimmer.width(boundsWidth)
        val height = shimmer.height(boundsHeight)

        shimmerPaint.shader = when (shimmer.shape) {
            Shape.Linear -> {
                val vertical = shimmer.direction.isVertical
                val endX = if (vertical) 0f else width
                val endY = if (vertical) height else 0f

                LinearGradient(
                    0f,
                    0f,
                    endX,
                    endY,
                    shimmer.colors,
                    shimmer.positions,
                    Shader.TileMode.CLAMP,
                )
            }

            Shape.Radial -> RadialGradient(
                width / 2f,
                height / 2f,
                (maxOf(width, height) / sqrt(2.0)).toFloat(),
                shimmer.colors,
                shimmer.positions,
                Shader.TileMode.CLAMP,
            )
        }
    }
}
