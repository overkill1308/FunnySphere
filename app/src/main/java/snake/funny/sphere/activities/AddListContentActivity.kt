package snake.funny.sphere.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.view.clicks
import snake.funny.sphere.R
import snake.funny.sphere.adapters.AddContentAdapter
import snake.funny.sphere.databinding.ActivityAddListContentBinding
import snake.funny.sphere.models.ContentModel
import java.util.concurrent.TimeUnit

class AddListContentActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddListContentBinding
    var listContent: ArrayList<ContentModel>? = null
    var addContentAdapter: AddContentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddListContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listContent = ArrayList()
        listContent!!.add(ContentModel("Content 1", "Content 1"))

        addContentAdapter = AddContentAdapter(listContent!!)
        binding.rvContent.layoutManager = LinearLayoutManager(this@AddListContentActivity)
        binding.rvContent.adapter = addContentAdapter

        binding.btnAddContent.clicks().throttleFirst(1, TimeUnit.SECONDS).subscribe() {
            addContentAdapter!!.addContent()
        }
    }
}