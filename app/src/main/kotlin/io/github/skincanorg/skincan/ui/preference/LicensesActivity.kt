package io.github.skincanorg.skincan.ui.preference

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mikepenz.aboutlibraries.LibsBuilder
import dagger.hilt.android.AndroidEntryPoint
import io.github.skincanorg.skincan.R
import io.github.skincanorg.skincan.databinding.ActivityLicensesBinding

@AndroidEntryPoint
class LicensesActivity : AppCompatActivity() {
    private val binding: ActivityLicensesBinding by viewBinding(CreateMethod.INFLATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = getString(R.string.licenses)

        binding.apply {
            setSupportActionBar(appbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setHomeButtonEnabled(true)
            }
            appbar.setNavigationOnClickListener { finish() }
            supportFragmentManager
                .beginTransaction()
                .replace(librariesContainer.id, LibsBuilder().supportFragment())
                .commit()
        }
    }
}
