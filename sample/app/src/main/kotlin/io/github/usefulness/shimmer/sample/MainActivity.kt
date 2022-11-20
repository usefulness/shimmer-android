package io.github.usefulness.shimmer.sample

import android.animation.ValueAnimator
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import io.github.usefulness.shimmer.android.Shimmer
import io.github.usefulness.shimmer.sample.databinding.MainBinding

class MainActivity : AppCompatActivity() {

    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val xmlPreset = binding.shimmerViewContainer.shimmer ?: Shimmer()
        binding.presetButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                binding.selectPreset(preset = index, showToast = true, default = xmlPreset)
            }
        }
        binding.selectPreset(preset = 0, showToast = false, xmlPreset)
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

    private fun MainBinding.selectPreset(preset: Int, showToast: Boolean, default: Shimmer) {
        val context = root.context

        presetButtons.forEach { it.setBackgroundResource(R.color.preset_button_background) }
        presetButtons[preset].setBackgroundResource(R.color.preset_button_background_selected)

        // If a toast is already showing, hide it
        toast?.cancel()

        shimmerViewContainer.shimmer = when (preset) {
            1 -> {
                // Slow and reverse
                toast = Toast.makeText(context, "Slow and reverse", Toast.LENGTH_SHORT)
                Shimmer(
                    animationDuration = 5000L,
                    repeatMode = ValueAnimator.REVERSE,
                )
            }

            2 -> {
                // Thin, straight and transparent
                toast = Toast.makeText(context, "Thin, straight and transparent", Toast.LENGTH_SHORT)
                Shimmer(
                    animationDuration = 3000,
                    baseAlpha = 0.1f,
                    dropoff = 0.1f,
                    tilt = 0f,
                )
            }

            3 -> {
                // Sweep angle 90
                toast = Toast.makeText(context, "Sweep angle 90", Toast.LENGTH_SHORT)
                Shimmer(
                    direction = Shimmer.Direction.TopToBottom,
                    tilt = 0f,
                )
            }

            4 -> {
                // Spotlight
                toast = Toast.makeText(context, "Spotlight", Toast.LENGTH_SHORT)
                Shimmer(
                    baseAlpha = 0f,
                    animationDuration = 2000L,
                    dropoff = 0.1f,
                    intensity = 0.35f,
                    shape = Shimmer.Shape.Radial,
                )
            }

            5 -> {
                // Spotlight angle 45
                toast = Toast.makeText(context, "Spotlight angle 45", Toast.LENGTH_SHORT)
                Shimmer(
                    baseAlpha = 0f,
                    animationDuration = 2000L,
                    dropoff = 0.1f,
                    intensity = 0.35f,
                    tilt = 45f,
                    shape = Shimmer.Shape.Radial,
                )
            }

            6 -> {
                // Off
                toast = Toast.makeText(context, "Off", Toast.LENGTH_SHORT)
                null
            }

            else -> {
                toast = Toast.makeText(context, "Default", Toast.LENGTH_SHORT)
                default
            }
        }

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
