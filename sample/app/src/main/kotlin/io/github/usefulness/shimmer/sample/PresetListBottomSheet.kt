package io.github.usefulness.shimmer.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.usefulness.shimmer.android.Shimmer
import io.github.usefulness.shimmer.sample.databinding.FragmentPresetSheetBinding
import io.github.usefulness.shimmer.sample.databinding.ItemPresetBinding

class PresetListBottomSheet : BottomSheetDialogFragment() {

    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentPresetSheetBinding.inflate(inflater, container, false)
        val adapter = PresetsAdapter(
            onSelected = { selected ->
                viewModel.config.value = selected
                dismiss()
            },
        ).apply { submitList(viewModel.buildPresets()) }
        binding.list.adapter = adapter

        return binding.root
    }

    private class PresetsAdapter(private val onSelected: (Shimmer) -> Unit) :
        ListAdapter<PresetButton, PresetsAdapter.ViewHolder>(DiffCallback) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(ItemPresetBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = getItem(position)
            holder.binding.root.text = item.name
            holder.binding.root.setOnClickListener { onSelected(item.config) }
        }

        object DiffCallback : DiffUtil.ItemCallback<PresetButton>() {

            override fun areItemsTheSame(oldItem: PresetButton, newItem: PresetButton) = oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: PresetButton, newItem: PresetButton) = oldItem == newItem
        }

        class ViewHolder(val binding: ItemPresetBinding) : RecyclerView.ViewHolder(binding.root)
    }
}
