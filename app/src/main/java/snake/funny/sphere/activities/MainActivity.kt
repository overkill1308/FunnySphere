package snake.funny.sphere.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jakewharton.rxbinding4.view.clicks
import snake.funny.sphere.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnHistory.clicks().throttleFirst(1, TimeUnit.SECONDS).subscribe() {
            startActivity(Intent(this@MainActivity, HistoryActivity::class.java))
        }

        binding.layoutEmoji.clicks().throttleFirst(1, TimeUnit.SECONDS).subscribe() {
            startActivity(Intent(this@MainActivity, AddListContentActivity::class.java))
        }

        binding.layoutTag.clicks().throttleFirst(1, TimeUnit.SECONDS).subscribe() {
            startActivity(Intent(this@MainActivity, AddListContentActivity::class.java))
        }

    }
}