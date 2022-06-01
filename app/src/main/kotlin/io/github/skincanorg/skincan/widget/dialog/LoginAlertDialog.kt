/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.widget.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View.OnClickListener
import androidx.appcompat.app.AlertDialog
import io.github.skincanorg.skincan.databinding.AlertDialogBinding

class LoginAlertDialog(context: Context) : AlertDialog(context) {

    private lateinit var binding: AlertDialogBinding

    private var onClickListener: OnClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AlertDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.apply {
            btnLogin.setOnClickListener {
                if (onClickListener != null)
                    onClickListener!!.onClick(it)
                this@LoginAlertDialog.dismiss()
            }
        }
    }

    fun setOnLoginListener(listener: OnClickListener?): LoginAlertDialog {
        onClickListener = listener
        return this
    }
}
