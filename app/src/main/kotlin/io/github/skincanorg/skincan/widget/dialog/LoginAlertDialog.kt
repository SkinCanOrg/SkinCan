package io.github.null2264.dicodingstories.widget.dialog

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