package io.github.skincanorg.skincan.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import io.github.null2264.dicodingstories.widget.dialog.LoginAlertDialog
import io.github.skincanorg.skincan.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private val binding: ActivityRegisterBinding by viewBinding(CreateMethod.INFLATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupCoreFunctions()
    }

    private fun setupCoreFunctions() {
        binding.apply {
            btnRegister.setOnClickListener {
                val alert = LoginAlertDialog(this@RegisterActivity)
                alert.setOnLoginListener {
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                }.show()
            }

            btnGotoLoginContainer.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
        }
    }
}