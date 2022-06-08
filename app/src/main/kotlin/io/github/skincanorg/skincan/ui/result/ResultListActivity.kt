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

        setSupportActionBar(binding.appbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        binding.appbar.setNavigationOnClickListener { finish() }

        binding.apply {
            rvResults.apply {
                adapter = resultsAdapter
                layoutManager = LinearLayoutManager(this@ResultListActivity)
                setHasFixedSize(true)
            }
        }

        viewModel.results.observe(this) {
            resultsAdapter.submitData(lifecycle, it)
        }
    }
}
