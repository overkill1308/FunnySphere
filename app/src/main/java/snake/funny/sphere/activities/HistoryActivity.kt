package snake.funny.sphere.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding4.view.clicks
import snake.funny.sphere.databinding.ActivityHistoryBinding
import java.util.concurrent.TimeUnit

class HistoryActivity : AppCompatActivity() {

    lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.clicks().throttleFirst(1, TimeUnit.SECONDS).subscribe() {
            onBackPressed()
        }

        binding.btnCreateList.clicks().throttleFirst(1, TimeUnit.SECONDS).subscribe() {
            onBackPressed()
        }

    }

//    override fun onResume() {
//        super.onResume()
//        if (Common.listContent != null) {
//            listContent = Common.listContent
//            binding.layoutEmpty.visibility = View.GONE
//            binding.tagView.visibility = View.VISIBLE
//            binding.layoutSetting.visibility = View.VISIBLE
//        } else {
//            binding.layoutEmpty.visibility = View.VISIBLE
//            binding.tagView.visibility = View.GONE
//            binding.layoutSetting.visibility = View.GONE
//        }
//    }

}