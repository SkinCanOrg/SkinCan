/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.data.result

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import data.Result
import io.github.skincanorg.skincan.databinding.ItemRowResultBinding
import io.github.skincanorg.skincan.lib.Util
import io.github.skincanorg.skincan.ui.result.ResultActivity

class ResultRecyclerAdapter :
    PagingDataAdapter<Result, ResultRecyclerAdapter.ListViewHolder>(DIFF_CALLBACK) {
    inner class ListViewHolder(private val binding: ItemRowResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Result) {
            binding.apply {
                root.setOnClickListener {
                    val ctx = itemView.context
                    ctx.startActivity(
                        Intent(ctx, ResultActivity::class.java).apply {
                            putExtra(ResultActivity.PHOTO_PATH, result.imgPath)
                            putExtra(ResultActivity.RESULT, result.result)
                            putExtra(ResultActivity.FROM, 1)
                        },
                    )
                }

                Glide.with(ivResultPict)
                    .load(Util.processBitmap(result.imgPath))
                    .into(ivResultPict)

                tvDatetime.text = result.scannedAt.toString()
                when (result.result) {
                    "Clear" -> {
                        tvResultStatus.text = "Clear"
                        tvStatus.text = "No cancer found"
                    }
                    null -> {
                        tvResultStatus.text = "ERROR"
                        tvStatus.text = "Error: Failed to retrieve data"
                    }
                    else -> {
                        tvResultStatus.text = "Cancer"
                        tvStatus.text = "Potientional risk cancer found"
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder = ListViewHolder(
        ItemRowResultBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    )

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(getItem(position) as Result)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Result>() {
            override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean = oldItem._id == newItem._id

            override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean = oldItem == newItem
        }
    }
}
