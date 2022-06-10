/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.ui.result

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.children
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import data.Result
import io.github.skincanorg.skincan.databinding.ItemRowResultBinding
import io.github.skincanorg.skincan.lib.Extension.toDateTime
import io.github.skincanorg.skincan.lib.Util

class ResultRecyclerAdapter :
    PagingDataAdapter<Result, ResultRecyclerAdapter.ListViewHolder>(DIFF_CALLBACK) {
    inner class ListViewHolder(private val binding: ItemRowResultBinding, private val parent: ViewGroup) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Result) {
            binding.apply {
                root.setOnClickListener {
                    val stringId = result._id.toString()
                    val ctx = itemView.context

                    parent.children.forEach { it.isEnabled = false }

                    ctx.startActivity(
                        Intent(ctx, ResultActivity::class.java).apply {
                            putExtra(ResultActivity.ID, stringId)
                            putExtra(ResultActivity.PHOTO_PATH, result.imgPath)
                            putExtra(ResultActivity.RESULT, result.result)
                            putExtra(ResultActivity.TIMESTAMP, result.scannedAt)
                            putExtra(ResultActivity.FROM, 1)
                        },
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            ctx as Activity,
                            resultPictWrapper,
                            stringId,
                        ).toBundle(),
                    )
                }

                Glide.with(ivResultPict)
                    .load(Util.processBitmap(result.imgPath))
                    .into(ivResultPict)

                tvDatetime.text = result.scannedAt.toDateTime("d MMM YYYY, HH:mm")

                when (result.result) {
                    "Clear" -> {
                        tvResultStatus.isEnabled = true
                        tvResultStatus.text = "Clear"
                        tvStatus.text = "No cancer found"
                    }

                    null -> {
                        tvResultStatus.isEnabled = false
                        tvResultStatus.text = "ERROR"
                        tvStatus.text = "Error: Failed to retrieve data"
                    }

                    else -> {
                        tvResultStatus.isEnabled = false
                        tvResultStatus.text = "Cancer"
                        tvStatus.text = "Potential risk cancer found"
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder = ListViewHolder(
        ItemRowResultBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        parent,
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
