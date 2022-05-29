/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.data.onboarding

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import io.github.skincanorg.skincan.databinding.OnboardingItemBinding

class OnboardAdapter(private val context: Context, data: List<OnboardScreen>) :
    RecyclerView.Adapter<OnboardAdapter.ViewHolder>() {
    private val fakeData = listOf(data.last()) + data + listOf(data.first())

    class ViewHolder(private val context: Context, private val binding: OnboardingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(screen: OnboardScreen) {
            screen.apply {
                binding.ivOnboard.setImageDrawable(AppCompatResources.getDrawable(context, image))
                binding.tvOnboardTitle.text = context.getString(title)
                binding.tvOnboardSubtitle.text = context.getString(subtitle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            context,
            OnboardingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(fakeData[position])
    }

    override fun getItemCount(): Int = fakeData.size
}
