package io.github.usefulness.shimmer.android

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

open class ShimmerFrameLayout : FrameLayout {

    private val contentPaint = Paint()
    private val shimmerDrawable = ShimmerDrawable()

    /** Return whether the shimmer drawable is visible.  */
    var isShimmerVisible = true
        private set
    private var shimmerStoppedBecauseVisibility = false

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int,
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        setWillNotDraw(false)
        shimmerDrawable.callback = this
        shimmer = if (attrs == null) {
            Shimmer()
        } else {
            @Suppress("Recycle")
            context.obtainStyledAttributes(attrs, R.styleable.ShimmerFrameLayout, 0, 0)
                .useCompat { it.buildShimmer() }
        }
    }

    var shimmer
        get() = shimmerDrawable.shimmer
        set(value) {
            shimmerDrawable.shimmer = value
            if (shimmer?.clipToChildren == true) {
                setLayerType(LAYER_TYPE_HARDWARE, contentPaint)
            } else {
                setLayerType(LAYER_TYPE_NONE, null)
            }
        }

    /** Starts the shimmer animation.  */
    fun startShimmer() {
        if (isAttachedToWindow) {
            shimmerDrawable.startShimmer()
        }
    }

    /** Stops the shimmer animation.  */
    fun stopShimmer() {
        shimmerStoppedBecauseVisibility = false
        shimmerDrawable.stopShimmer()
    }

    /** Return whether the shimmer animation has been started.  */
    val isShimmerStarted: Boolean
        get() = shimmerDrawable.isShimmerStarted

    /**
     * Sets the ShimmerDrawable to be visible.
     *
     * @param startShimmer Whether to start the shimmer again.
     */
    fun showShimmer(startShimmer: Boolean) {
        isShimmerVisible = true
        if (startShimmer) {
            startShimmer()
        }
        invalidate()
    }

    /** Sets the ShimmerDrawable to be invisible, stopping it in the process.  */
    fun hideShimmer() {
        stopShimmer()
        isShimmerVisible = false
        invalidate()
    }

    val isShimmerRunning: Boolean
        get() = shimmerDrawable.isShimmerRunning

    public override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val width = width
        val height = height
        shimmerDrawable.setBounds(0, 0, width, height)
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility != VISIBLE) {
            if (isShimmerStarted) {
                stopShimmer()
                shimmerStoppedBecauseVisibility = true
            }
        } else if (shimmerStoppedBecauseVisibility) {
            shimmerDrawable.maybeStartShimmer()
            shimmerStoppedBecauseVisibility = false
        }
    }

    public override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        shimmerDrawable.maybeStartShimmer()
    }

    public override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopShimmer()
    }

    public override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (isShimmerVisible) {
            shimmerDrawable.draw(canvas)
        }
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return super.verifyDrawable(who) || who === shimmerDrawable
    }

    fun setStaticAnimationProgress(value: Float) {
        shimmerDrawable.setStaticAnimationProgress(value)
    }

    fun clearStaticAnimationProgress() {
        shimmerDrawable.clearStaticAnimationProgress()
    }
}


@Suppress("TooGenericExceptionCaught")
private inline fun <R> TypedArray.useCompat(block: (TypedArray) -> R) =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        use(block)
    } else {
        var exception: Throwable? = null
        try {
            block(this)
        } catch (e: Throwable) {
            exception = e
            throw e
        } finally {
            when (exception) {
                null -> recycle()
                else -> try {
                    recycle()
                } catch (closeException: Throwable) {
                    exception.addSuppressed(closeException)
                }
            }
        }
    }
