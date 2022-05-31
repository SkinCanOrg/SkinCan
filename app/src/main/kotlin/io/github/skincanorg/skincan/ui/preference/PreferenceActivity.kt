package io.github.skincanorg.skincan.ui.preference

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import io.github.skincanorg.skincan.databinding.ActivityPreferenceBinding

class PreferenceActivity : AppCompatActivity() {
    private val binding: ActivityPreferenceBinding by viewBinding(CreateMethod.INFLATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.appbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        binding.appbar.setNavigationOnClickListener { finish() }
        supportFragmentManager
            .beginTransaction()
            .replace(binding.preferenceContainer.id, PreferenceFragment())
            .commit()
    }
}
