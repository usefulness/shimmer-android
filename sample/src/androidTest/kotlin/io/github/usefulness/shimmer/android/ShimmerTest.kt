package io.github.usefulness.shimmer.android

import android.view.LayoutInflater
import androidx.test.ext.junit.rules.activityScenarioRule
import com.karumi.shot.ActivityScenarioUtils.waitForActivity
import com.karumi.shot.ScreenshotTest
import io.github.usefulness.shimmer.sample.MainActivity
import io.github.usefulness.shimmer.sample.R
import org.junit.Rule
import org.junit.Test

class ShimmerTest {

    val test = object : ScreenshotTest {}

    @get:Rule
    val rule = activityScenarioRule<MainActivity>()

    @Test
    fun launchView() {
        val activity = rule.scenario.waitForActivity()
        val inflater = LayoutInflater.from(activity)
        val view = inflater.inflate(R.layout.main, null, false)
        val shimmer = view.findViewById<ShimmerFrameLayout>(R.id.shimmer_view_container)
        shimmer.setStaticAnimationProgress(0.5f)

        test.compareScreenshot(view, 2000, 1200)
        test.compareScreenshot(view, 2000, 1600, name = "launchView-wide")
    }
}
