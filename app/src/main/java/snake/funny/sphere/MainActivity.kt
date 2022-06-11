package snake.funny.sphere

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextPaint
import android.widget.SeekBar
import snake.funny.sphere.util.CustomOnSeekBarChangeListener
import com.google.android.material.snackbar.Snackbar
import com.magicgoop.tagsphere.OnTagLongPressedListener
import com.magicgoop.tagsphere.OnTagTapListener
import com.magicgoop.tagsphere.item.TagItem
import com.magicgoop.tagsphere.item.TextTagItem
import com.magicgoop.tagsphere.utils.EasingFunction
import snake.funny.sphere.databinding.ActivityMainBinding
import snake.funny.sphere.util.EmojiConstants
import kotlin.random.Random

class MainActivity : AppCompatActivity(), OnTagLongPressedListener, OnTagTapListener {

    lateinit var binding: ActivityMainBinding

    companion object {
        private const val MIN_SENSITIVITY = 1
        private const val MIN_RADIUS = 10f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTagView()
        initSettings()
    }

    private fun initTagView() {
        val samples = EmojiConstants.emojiCodePoints.size - 1
        binding.tagView.setTextPaint(
            TextPaint().apply {
                isAntiAlias = true
                textSize = resources.getDimension(R.dimen.tag_text_size)
            }
        )
        (0..100).map {
            TextTagItem(
                text = String(
                    Character.toChars(EmojiConstants.emojiCodePoints[Random.nextInt(samples)])
                )
            )
        }.toList().let {
            binding.tagView.addTagList(it)
        }
        binding.tagView.setOnLongPressedListener(this)
        binding.tagView.setOnTagTapListener(this)
    }

    private fun initSettings() {
        binding.sbRadius.setOnSeekBarChangeListener(object : CustomOnSeekBarChangeListener() {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.tagView.setRadius((progress + MIN_RADIUS) / 10f)
            }
        })
        binding.sbTouchSensitivity.setOnSeekBarChangeListener(object : CustomOnSeekBarChangeListener() {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.tagView.setTouchSensitivity(progress + MIN_SENSITIVITY)
            }
        })
        binding.cbRotateOnTouch.setOnCheckedChangeListener { _, isChecked ->
            binding.tagView.rotateOnTouch(isChecked)
            if (isChecked) {
                binding.cbAutoRotate.isChecked = false
                binding.tagView.stopAutoRotation()
            }
        }
        binding.cbAutoRotate.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.cbRotateOnTouch.isChecked = false
                val multiplier = Random.nextInt(1, 5)
                binding.tagView.startAutoRotation(
                    Random.nextFloat() * multiplier,
                    -Random.nextFloat() * multiplier
                )
            } else {
                binding.cbRotateOnTouch.isChecked = true
            }
        }


        binding.rgEasingFunctions.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbEaseInExpo -> binding.tagView.setEasingFunction { EasingFunction.easeInExpo(it) }
                R.id.rbEaseOutExpo -> binding.tagView.setEasingFunction { EasingFunction.easeOutExpo(it) }
                R.id.rbCustom -> binding.tagView.setEasingFunction { t -> 1f - t * t * t * t * t }
                else -> binding.tagView.setEasingFunction(null)

            }
        }
    }

    override fun onLongPressed(tagItem: TagItem) {
        Snackbar
            .make(binding.tagView, "onLongPressed: " + (tagItem as TextTagItem).text, Snackbar.LENGTH_LONG)
            .setAction("Delete tag") { binding.tagView.removeTag(tagItem) }
            .show()
    }

    override fun onTap(tagItem: TagItem) {
        Snackbar
            .make(binding.tagView, "onTap: " + (tagItem as TextTagItem).text, Snackbar.LENGTH_SHORT)
            .show()
    }
}