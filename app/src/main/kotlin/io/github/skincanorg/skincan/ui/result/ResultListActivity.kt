/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.ui.result

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import io.github.skincanorg.skincan.databinding.ActivityResultListBinding

@AndroidEntryPoint
class ResultListActivity : AppCompatActivity() {
    private val binding: ActivityResultListBinding by viewBinding(CreateMethod.INFLATE)
    private val resultsAdapter by lazy { ResultRecyclerAdapter() }
    private val viewModel: ResultViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            setSupportActionBar(appbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setHomeButtonEnabled(true)
            }
            appbar.setNavigationOnClickListener { finish() }

            rvResults.apply {
                adapter = resultsAdapter.apply {
                    addLoadStateListener { state ->
                        rvResults.isVisible = state.refresh !is LoadState.Error
                        tvError.isVisible = state.refresh is LoadState.Error
                        pbLoading.isVisible = state.refresh is LoadState.Loading
                    }
                }
                layoutManager = LinearLayoutManager(this@ResultListActivity)
                setHasFixedSize(true)
            }
        }

        viewModel.results.observe(this) {
            resultsAdapter.submitData(lifecycle, it)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.rvResults.children.forEach { it.isEnabled = true }
    }
}
