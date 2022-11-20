package io.github.usefulness.shimmer.android

import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec
import android.view.View.MeasureSpec.makeMeasureSpec
import androidx.test.ext.junit.rules.activityScenarioRule
import com.facebook.testing.screenshot.Screenshot
import com.facebook.testing.screenshot.TestMethodInfo
import com.facebook.testing.screenshot.TestNameDetector
import io.github.usefulness.shimmer.sample.MainActivity
import io.github.usefulness.shimmer.sample.databinding.ScreenshotTestLayoutBinding
import org.junit.Rule
import org.junit.Test

class ShimmerTest {

    @get:Rule
    val rule = activityScenarioRule<MainActivity>()

    @Test
    fun resizing() = recordWithData(
        data = Shimmer(),
        shimmerProgresses = listOf(0.5),
        sizes = listOf(
            2000 to 1200,
            2000 to 1600,
            1500 to 1100,
            3500 to 1800,
        ),
    )

    @Test
    fun thingStraightTransparent() = recordWithData(
        data = Shimmer(
            baseAlpha = 0.1f,
            dropoff = 0.1f,
            tilt = 0f,
        ),
    )

    @Test
    fun topToBottom() = recordWithData(
        data = Shimmer(
            direction = Shimmer.Direction.TopToBottom,
            tilt = 0f,
        ),
    )

    @Test
    fun radial() = recordWithData(
        data = Shimmer(
            baseAlpha = 0f,
            dropoff = 0.1f,
            intensity = 0.35f,
            shape = Shimmer.Shape.Radial,
        ),
    )

    @Test
    fun radialAngle() = recordWithData(
        data = Shimmer(
            baseAlpha = 0f,
            dropoff = 0.1f,
            intensity = 0.35f,
            tilt = 45f,
            shape = Shimmer.Shape.Radial,
        ),
    )

    @Test
    fun colored() = recordWithData(
        data = Shimmer(
            style = Shimmer.Style.Colored(
                baseColor = 0x86BBD8,
                highlightColor = 0xF6AE2D,
            ),
            dropoff = 0.1f,
            intensity = 0.5f,
            baseAlpha = 0.4f,
            highlightAlpha = 0.9f,
            direction = Shimmer.Direction.RightToLeft,
            tilt = -10f,
            shape = Shimmer.Shape.Linear,
        ),
    )

    private fun recordWithData(
        data: Shimmer,
        shimmerProgresses: List<Number> = listOf(0.3, 0.5, 0.7),
        sizes: List<Pair<Int, Int>> = listOf(1400 to 800),
    ) {
        val testInfo = TestNameDetector.getTestMethodInfo().let(::checkNotNull)

        rule.scenario.onActivity { activity ->
            val inflater = LayoutInflater.from(activity)
            val binding = ScreenshotTestLayoutBinding.inflate(inflater)
            binding.shimmerViewContainer.shimmer = data
            shimmerProgresses.forEach { progress ->
                binding.shimmerViewContainer.setStaticAnimationProgress(progress.toFloat())

                sizes.forEach { (width, height) ->
                    binding.root.layout(width = width, height = height)

                    binding.root.record(testInfo, "[$progress]-[${width}x$height]")
                }
            }
        }
    }
}

private fun View.layout(width: Int, height: Int) {
    measure(
        makeMeasureSpec(width, MeasureSpec.EXACTLY),
        makeMeasureSpec(height, MeasureSpec.EXACTLY),
    )

    layout(0, 0, measuredWidth, measuredHeight)
}

private fun View.record(testInfo: TestMethodInfo, suffix: String) {
    val testClassText = testInfo.className.substringAfterLast('.').removeSuffix("Test")
    val screenshotFilePrefix = "$testClassText-${testInfo.methodName}"

    Screenshot.snap(this)
        .setMaxPixels(Long.MAX_VALUE)
        .setGroup(screenshotFilePrefix)
        .setName("$screenshotFilePrefix-$suffix")
        .record()
}
