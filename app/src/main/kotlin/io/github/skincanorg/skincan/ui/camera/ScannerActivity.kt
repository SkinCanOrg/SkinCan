package io.github.skincanorg.skincan.ui.camera

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionLayout.TransitionListener
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import io.github.skincanorg.skincan.R
import io.github.skincanorg.skincan.databinding.ActivityScannerBinding
import io.github.skincanorg.skincan.lib.Util
import java.io.File

class ScannerActivity : AppCompatActivity() {
    private val binding: ActivityScannerBinding by viewBinding(CreateMethod.INFLATE)
    private var shouldLoop = false
    private var looped = true

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

            root.setTransitionListener(object : TransitionListener {
                override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                    if (shouldLoop) {
                        if (!looped)
                            motionLayout.transitionToState(R.id.loop_start)
                        else
                            motionLayout.transitionToState(R.id.loop_end)
                        looped = !looped
                    }
                }

                override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {}

                override fun onTransitionChange(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int,
                    progress: Float,
                ) {}

                override fun onTransitionTrigger(
                    motionLayout: MotionLayout?,
                    triggerId: Int,
                    positive: Boolean,
                    progress: Float,
                ) {}

            })

            btnScan.setOnClickListener {
                shouldLoop = true
                root.transitionToState(R.id.loop_start, 1)
            }

            btnCancel.setOnClickListener {
                finish()
            }
        }
    }
}
