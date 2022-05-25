package io.github.skincanorg.skincan.ui.camera

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import io.github.skincanorg.skincan.databinding.ActivityScannerBinding
import io.github.skincanorg.skincan.lib.Util
import java.io.File

class ScannerActivity : AppCompatActivity() {
    private val binding: ActivityScannerBinding by viewBinding(CreateMethod.INFLATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            val photoFile = intent.extras?.get("IMG_FILE") as File?
            if (photoFile != null) {
                val img = Util.processBitmap(
                    BitmapFactory.decodeFile(photoFile.path),
                    photoFile,
                )

                Glide.with(ivImagePreview)
                    .load(img)
                    .into(ivImagePreview)
            } else {
                finish()
            }
            
            btnCancel.setOnClickListener {
                finish()
            }
        }
    }
}