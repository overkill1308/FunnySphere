package snake.funny.sphere.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextPaint
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import snake.funny.sphere.util.CustomOnSeekBarChangeListener
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.view.visibility
import com.magicgoop.tagsphere.OnTagLongPressedListener
import com.magicgoop.tagsphere.OnTagTapListener
import com.magicgoop.tagsphere.item.TagItem
import com.magicgoop.tagsphere.item.TextTagItem
import com.magicgoop.tagsphere.utils.EasingFunction
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import snake.funny.sphere.R
import snake.funny.sphere.databinding.ActivityMainBinding
import snake.funny.sphere.databinding.DialogResultBinding
import snake.funny.sphere.util.Common
import snake.funny.sphere.util.EmojiConstants
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class MainActivity : AppCompatActivity(), OnTagLongPressedListener, OnTagTapListener {

    lateinit var binding: ActivityMainBinding
    var listContent: ArrayList<String>? = null

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

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener(View.OnClickListener {
            startActivity(Intent(this@MainActivity, AddListContentActivity::class.java))
        })

        if (Common.listContent != null) {
            listContent = Common.listContent
            binding.layoutEmpty.visibility = View.GONE
            binding.tagView.visibility = View.VISIBLE
            binding.layoutSetting.visibility = View.VISIBLE
        } else {
            binding.layoutEmpty.visibility = View.VISIBLE
            binding.tagView.visibility = View.GONE
            binding.layoutSetting.visibility = View.GONE
        }

        binding.btnCreateList.clicks().throttleFirst(1, TimeUnit.SECONDS).subscribe() {
            startActivity(Intent(this@MainActivity, AddListContentActivity::class.java))
        }

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
//        Snackbar
//            .make(binding.tagView, "onTap: " + (tagItem as TextTagItem).text, Snackbar.LENGTH_SHORT)
//            .show()
        showDialogResult(tagItem)
    }

    private fun showDialogResult(tagItem: TagItem) {
        val resultBinding: DialogResultBinding = DialogResultBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setView(resultBinding.root)

        val dialog = builder.create()

        resultBinding.tvIcon.text = (tagItem as TextTagItem).text
        val index = Random.nextInt(listContent!!.size - 1)
        resultBinding.tvContent.text = listContent!![index]

        resultBinding.btnContinue.clicks().throttleFirst(1, TimeUnit.SECONDS).subscribe() {
            dialog.dismiss()
        }

        resultBinding.btnRemove.clicks().throttleFirst(1, TimeUnit.SECONDS).subscribe() {
            listContent!!.removeAt(index)
            Toast.makeText(this@MainActivity, "Deleted Content!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        val party = Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100),
            position = Position.Relative(0.5, 0.3)
        )

        resultBinding.konfettiView.start(party)

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

}