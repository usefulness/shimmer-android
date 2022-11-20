package io.github.usefulness.shimmer.sample

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import androidx.core.view.get
import androidx.core.view.updatePadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import io.github.usefulness.shimmer.android.Shimmer
import io.github.usefulness.shimmer.sample.databinding.MainBinding
import io.github.usefulness.shimmer.sample.databinding.ViewGroupBinding
import io.github.usefulness.shimmer.sample.databinding.ViewGroupItemBinding
import io.github.usefulness.shimmer.sample.databinding.ViewSliderBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.content.updatePadding(top = insets.top)
            binding.contentScrollView.updatePadding(bottom = insets.bottom)

            WindowInsetsCompat.CONSUMED
        }

        lifecycleScope.launchWhenCreated {
            lifecycle.repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                launch {
                    viewModel.config.collect { shimmer ->
                        binding.shimmerViewContainer.shimmer = shimmer
                    }
                }
                launch {
                    binding.style.bindStylePickerSection(
                        data = viewModel.config.map { it.style },
                        updateData = { copy(style = it) },
                    )
                }
                launch {
                    binding.direction.bind(
                        name = "Direction",
                        allValues = Shimmer.Direction.values().toList(),
                        data = viewModel.config.map { it.direction },
                        updateData = { copy(direction = it) },
                        labelMapping = Shimmer.Direction::name,
                    )
                }
                launch {
                    binding.baseAlpha.bind(
                        name = "baseAlpha",
                        data = viewModel.config.map { it.baseAlpha },
                        updateData = { copy(baseAlpha = it) },
                    )
                }
                launch {
                    binding.highlightAlpha.bind(
                        name = "highlightAlpha",
                        data = viewModel.config.map { it.highlightAlpha },
                        updateData = { copy(highlightAlpha = it) },
                    )
                }
                launch {
                    binding.shape.bind(
                        name = "shape",
                        allValues = Shimmer.Shape.values().toList(),
                        data = viewModel.config.map { it.shape },
                        updateData = { copy(shape = it) },
                        labelMapping = Shimmer.Shape::name,
                    )
                }
                launch {
                    binding.intensity.bind(
                        name = "intensity",
                        data = viewModel.config.map { it.intensity },
                        updateData = { copy(intensity = it) },
                    )
                }
                launch {
                    binding.dropoff.bind(
                        name = "dropoff",
                        data = viewModel.config.map { it.dropoff },
                        updateData = { copy(dropoff = it) },
                    )
                }
                launch {
                    binding.tilt.bind(
                        name = "tilt",
                        data = viewModel.config.map { it.tilt },
                        stepSize = 1f,
                        valueFrom = 0f,
                        valueTo = 90f,
                        updateData = { copy(tilt = it) },
                    )
                }
                launch {
                    binding.repeatMode.bind(
                        name = "repeatMode",
                        allValues = Shimmer.RepeatMode.values().toList(),
                        data = viewModel.config.map { it.repeatMode },
                        updateData = { copy(repeatMode = it) },
                        labelMapping = Shimmer.RepeatMode::name,
                    )
                }
                launch {
                    binding.animationDuration.bind(
                        name = "animationDuration",
                        data = viewModel.config.map { it.animationDuration.inWholeMilliseconds.toFloat() },
                        valueFrom = 0f,
                        valueTo = 5000f,
                        stepSize = 200f,
                        updateData = { copy(animationDuration = it.toLong().milliseconds) },
                    )
                }
                launch {
                    binding.repeatDelay.bind(
                        name = "repeatDelay",
                        data = viewModel.config.map { it.repeatDelay.inWholeMilliseconds.toFloat() },
                        valueFrom = 0f,
                        valueTo = 5000f,
                        stepSize = 500f,
                        updateData = { copy(repeatDelay = it.toLong().milliseconds) },
                    )
                }
            }
        }
        binding.btnLoadPreset.setOnClickListener {
            PresetListBottomSheet().show(supportFragmentManager, PresetListBottomSheet::class.java.name)
        }
    }

    private suspend fun ViewSliderBinding.bind(
        name: String,
        data: Flow<Float>,
        stepSize: Float = 0.01f,
        valueFrom: Float = 0f,
        valueTo: Float = 1f,
        updateData: Shimmer.(Float) -> Shimmer,
    ) {
        sliderLabel.text = name

        slider.stepSize = stepSize
        slider.valueFrom = valueFrom
        slider.valueTo = valueTo
        slider.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                viewModel.config.value = viewModel.config.value.updateData(value)
            }
        }
        data.collect {
            @Suppress("SetTextI18n")
            sliderLabel.text = "$name = $it"
            slider.value = it
        }
    }

    private suspend fun <T> ViewGroupBinding.bind(
        name: String,
        allValues: List<T>,
        data: Flow<T>,
        updateData: Shimmer.(T) -> Shimmer,
        labelMapping: (T) -> String,
    ) {
        val inflater = LayoutInflater.from(root.context)

        allValues.forEach { value ->
            val itemBinding = ViewGroupItemBinding.inflate(inflater, group, true)
            itemBinding.root.text = labelMapping(value)
        }
        groupTitle.text = name
        group.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                val index = group.children.indexOfFirst { it.id == checkedId }
                viewModel.config.value = viewModel.config.value.updateData(allValues[index])
            }
        }
        data.collect { selected ->
            val index = allValues.indexOfFirst { it == selected }.takeIf { it >= 0 }
            if (index == null) {
                group.clearChecked()
            } else {
                val buttonId = group[index].id
                group.check(buttonId)
            }
        }
    }

    private suspend fun ViewGroupBinding.bindStylePickerSection(
        data: Flow<Shimmer.Style>,
        updateData: Shimmer.(Shimmer.Style) -> Shimmer,
    ) {
        val inflater = LayoutInflater.from(root.context)

        groupTitle.text = "shimmer_colored"
        val allValues = listOf(
            "Alpha" to Shimmer.Style.Alpha,
            "Colored(blue-yellow)" to Shimmer.Style.Colored(
                baseColor = 0x86BBD8,
                highlightColor = 0xF6AE2D,
            ),
            "Colored(red-white)" to Shimmer.Style.Colored(
                baseColor = 0xFEC0AA,
                highlightColor = 0xEC4E20,
            ),
        )
        allValues.forEach { (name, _) ->
            val itemBinding = ViewGroupItemBinding.inflate(inflater, group, true)
            itemBinding.root.text = name
        }
        group.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                val index = group.children.indexOfFirst { it.id == checkedId }
                viewModel.config.value = viewModel.config.value.updateData(allValues[index].second)
            }
        }
        data.collect { selected ->
            val index = allValues.indexOfFirst { it.second == selected }.takeIf { it >= 0 }
            if (index == null) {
                group.clearChecked()
            } else {
                val buttonId = group[index].id
                group.check(buttonId)
            }
        }
    }
}
