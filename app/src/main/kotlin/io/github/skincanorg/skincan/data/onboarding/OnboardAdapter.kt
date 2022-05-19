package io.github.skincanorg.skincan.data.onboarding

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import io.github.skincanorg.skincan.databinding.OnboardingItemBinding

class OnboardAdapter(private val context: Context, data: List<OnboardScreen>) : RecyclerView.Adapter<OnboardAdapter.ViewHolder>() {
    private val actualData = listOf(data.last()) + data + listOf(data.first())
    class ViewHolder(private val context: Context, private val binding: OnboardingItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(screen: OnboardScreen) {
            screen.apply {
                binding.ivOnboard.setImageDrawable(AppCompatResources.getDrawable(context, image))
                binding.tvOnboardTitle.text = context.getString(title)
                binding.tvOnboardSubtitle.text = context.getString(subtitle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(context, OnboardingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(actualData[position])
    }

    override fun getItemCount(): Int = actualData.size
}