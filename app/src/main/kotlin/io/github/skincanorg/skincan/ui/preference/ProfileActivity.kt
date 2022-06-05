/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.ui.preference

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import io.github.skincanorg.skincan.databinding.ActivityProfileBinding
import io.github.skincanorg.skincan.ui.OnboardingActivity
import io.github.skincanorg.skincan.ui.auth.AuthViewModel

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    private val binding: ActivityProfileBinding by viewBinding(CreateMethod.INFLATE)
    private val viewModel: AuthViewModel by viewModels()

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
            supportFragmentManager
                .beginTransaction()
                .replace(preferenceContainer.id, ProfileFragment())
                .commit()

            tvName.text = viewModel.getUser().displayName
            tvEmail.text = viewModel.getUser().email
            btnLogout.setOnClickListener {
                val intent = Intent(this@ProfileActivity, OnboardingActivity::class.java)
                startActivity(intent)
                viewModel.logout()
                finishAffinity()
            }
        }
    }
}
