package io.github.usefulness.shimmer.sample

import android.animation.ValueAnimator
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import io.github.usefulness.shimmer.android.Shimmer
import io.github.usefulness.shimmer.sample.databinding.MainBinding

class MainActivity : AppCompatActivity() {

    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.presetButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                binding.selectPreset(preset = index, showToast = true)
            }
        }
        binding.selectPreset(preset = 0, showToast = false)
        lifecycle.addObserver(
            object : DefaultLifecycleObserver {

                override fun onResume(owner: LifecycleOwner) {
                    binding.shimmerViewContainer.startShimmer()
                }

                override fun onPause(owner: LifecycleOwner) {
                    binding.shimmerViewContainer.stopShimmer()
                }
            },
        )
    }

    private fun MainBinding.selectPreset(preset: Int, showToast: Boolean) {
        val context = root.context

        presetButtons.forEach { it.setBackgroundResource(R.color.preset_button_background) }
        presetButtons[preset].setBackgroundResource(R.color.preset_button_background_selected)

        // If a toast is already showing, hide it
        toast?.cancel()

        val shimmerBuilder = Shimmer.AlphaHighlightBuilder()
        shimmerViewContainer.shimmer = when (preset) {
            1 -> {
                // Slow and reverse
                toast = Toast.makeText(context, "Slow and reverse", Toast.LENGTH_SHORT)
                shimmerBuilder.setDuration(5000L).setRepeatMode(ValueAnimator.REVERSE)
            }

            2 -> {
                // Thin, straight and transparent
                toast = Toast.makeText(context, "Thin, straight and transparent", Toast.LENGTH_SHORT)
                shimmerBuilder.setBaseAlpha(0.1f).setDropoff(0.1f).setTilt(0f)
            }

            3 -> {
                // Sweep angle 90
                toast = Toast.makeText(context, "Sweep angle 90", Toast.LENGTH_SHORT)
                shimmerBuilder.setDirection(Shimmer.Direction.TOP_TO_BOTTOM).setTilt(0f)
            }

            4 -> {
                // Spotlight
                toast = Toast.makeText(context, "Spotlight", Toast.LENGTH_SHORT)
                shimmerBuilder
                    .setBaseAlpha(0f)
                    .setDuration(2000L)
                    .setDropoff(0.1f)
                    .setIntensity(0.35f)
                    .setShape(Shimmer.Shape.RADIAL)
            }

            5 -> {
                // Spotlight angle 45
                toast = Toast.makeText(context, "Spotlight angle 45", Toast.LENGTH_SHORT)
                shimmerBuilder
                    .setBaseAlpha(0f)
                    .setDuration(2000L)
                    .setDropoff(0.1f)
                    .setIntensity(0.35f)
                    .setTilt(45f)
                    .setShape(Shimmer.Shape.RADIAL)
            }

            6 -> {
                // Off
                toast = Toast.makeText(context, "Off", Toast.LENGTH_SHORT)
                null
            }

            else -> {
                toast = Toast.makeText(context, "Default", Toast.LENGTH_SHORT)
                shimmerBuilder
            }
        }
            ?.build()

        if (showToast) {
            toast?.show()
        }
    }
}

private val MainBinding.presetButtons
    get() = arrayOf(
        presetButton0,
        presetButton1,
        presetButton2,
        presetButton3,
        presetButton4,
        presetButton5,
        presetButton6,
    )
