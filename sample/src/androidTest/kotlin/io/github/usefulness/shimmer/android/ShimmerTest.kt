package io.github.usefulness.shimmer.android

import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec
import android.view.View.MeasureSpec.makeMeasureSpec
import androidx.test.ext.junit.rules.activityScenarioRule
import com.facebook.testing.screenshot.Screenshot
import com.facebook.testing.screenshot.TestNameDetector
import io.github.usefulness.shimmer.sample.MainActivity
import io.github.usefulness.shimmer.sample.R
import org.junit.Rule
import org.junit.Test

class ShimmerTest {

    @get:Rule
    val rule = activityScenarioRule<MainActivity>()

    @Test
    fun launchView() {
        rule.scenario.onActivity { activity ->
            val inflater = LayoutInflater.from(activity)
            val view = inflater.inflate(R.layout.main, null, false)
            val shimmer = view.findViewById<ShimmerFrameLayout>(R.id.shimmer_view_container)
            shimmer.setStaticAnimationProgress(0.5f)

            listOf(
                2000 to 1200,
                2000 to 1600,
                1500 to 1100,
                3500 to 1800,
            )
                .forEach { (width, height) -> view.record(width, height) }
        }
    }
}

private fun View.record(width: Int, height: Int) {
    measure(
        makeMeasureSpec(width, MeasureSpec.EXACTLY),
        makeMeasureSpec(height, MeasureSpec.EXACTLY),
    )

    layout(0, 0, measuredWidth, measuredHeight)

    val testInfo = TestNameDetector.getTestMethodInfo().let(::checkNotNull)
    val testClassText = testInfo.className.substringAfterLast('.').removeSuffix("Test")
    val screenshotFilePrefix = "$testClassText-${testInfo.methodName}"

    Screenshot.snap(this)
        .setMaxPixels(Long.MAX_VALUE)
        .setGroup(screenshotFilePrefix)
        .setName("$screenshotFilePrefix-${width}x$height")
        .record()
}
