/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.ui.result

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import io.github.skincanorg.skincan.databinding.ActivityResultBinding
import io.github.skincanorg.skincan.ui.main.MainActivity

@AndroidEntryPoint
class ResultActivity : AppCompatActivity() {
    private val binding: ActivityResultBinding by viewBinding(CreateMethod.INFLATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupCoreFunctions(intent.extras?.getInt(FROM) ?: 1)
    }

    private fun setupCoreFunctions(from: Int) {
        binding.apply {
            appbar.isVisible = from == 1
            if (from == 1) {
                setSupportActionBar(appbar)
                supportActionBar?.apply {
                    setDisplayHomeAsUpEnabled(true)
                    setHomeButtonEnabled(true)
                }
                appbar.setNavigationOnClickListener { onBackPressed() }
            }
            btnGroup.isVisible = from == 0

            btnHome.setOnClickListener { onBackPressed() }
            tvResultStatus.text = when (intent.extras?.getString(RESULT)) {
                "Clear" -> "Clear"
                null -> "ERROR"
                else -> "Cancer"
            }
        }
    }

    override fun onBackPressed() {
        startActivity(
            Intent(this@ResultActivity, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            },
        )
    }

    companion object {
        const val RESULT = "IS_CANCER"
        const val FROM = "FROM" // 0 = Scanner, 1 = List
    }
}
