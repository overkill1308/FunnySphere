package snake.funny.sphere.activities

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.view.clicks
import snake.funny.sphere.R
import snake.funny.sphere.adapters.AddContentAdapter
import snake.funny.sphere.databinding.ActivityAddListContentBinding
import snake.funny.sphere.databinding.AddContentDialogBinding
import snake.funny.sphere.databinding.RemoveDialogBinding
import snake.funny.sphere.databinding.RenameDialogBinding
import snake.funny.sphere.listeners.OnOptionListener
import snake.funny.sphere.models.ContentModel
import java.io.File
import java.util.concurrent.TimeUnit

class AddListContentActivity : AppCompatActivity(), OnOptionListener {

    lateinit var binding: ActivityAddListContentBinding
    var listContent: ArrayList<ContentModel>? = null
    var addContentAdapter: AddContentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddListContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listContent = ArrayList()
        listContent!!.add(ContentModel(1, "Content 1"))
        listContent!!.add(ContentModel(2, "Content 2"))
        listContent!!.add(ContentModel(3, "Content 3"))
        listContent!!.add(ContentModel(4, "Content 4"))

        addContentAdapter = AddContentAdapter(listContent!!, this@AddListContentActivity)
        binding.rvContent.layoutManager = LinearLayoutManager(this@AddListContentActivity)
        binding.rvContent.adapter = addContentAdapter

        binding.btnAddContent.clicks().throttleFirst(1, TimeUnit.SECONDS).subscribe() {
            showDialogAdd()
        }
    }

    override fun optionClicked(view: View?, position: Int) {
        showPopupMenu(view, position)
    }

    private fun showPopupMenu(view: View?, position: Int) {
        val popupMenu = PopupMenu(this@AddListContentActivity, view)
        popupMenu.inflate(R.menu.menu_option)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_delete -> {
                    showDialogRemove(position)
                    true
                }
                R.id.action_edit -> {
                    showDialogRename(position)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    @SuppressLint("CheckResult")
    private fun showDialogRemove(position: Int) {
        val buider = AlertDialog.Builder(this@AddListContentActivity)
        val dialogBinding = RemoveDialogBinding.inflate(layoutInflater)
        buider.setView(dialogBinding.root)

        val alertDialog = buider.create()

        dialogBinding.tvOk.clicks().throttleFirst(1, TimeUnit.SECONDS).subscribe() {
            listContent!!.removeAt(position)
            addContentAdapter!!.notifyDataSetChanged()
//            if (listContent!!.size > 0) {
//                binding.ivEmpty.hide()
//            } else {
//                binding.ivEmpty.show()
//            }
            Toast.makeText(this@AddListContentActivity, "Deleted item!", Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
        }

        dialogBinding.tvCancel.clicks().throttleFirst(1, TimeUnit.SECONDS).subscribe() {
            alertDialog.dismiss()
        }

        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

    @SuppressLint("CheckResult")
    private fun showDialogRename(position: Int) {
        val buider = AlertDialog.Builder(this@AddListContentActivity)
        val dialogBinding = RenameDialogBinding.inflate(layoutInflater)
        buider.setView(dialogBinding.root)

        val alertDialog = buider.create()

        dialogBinding.tvOk.clicks().throttleFirst(1, TimeUnit.SECONDS).subscribe() {
            val content = dialogBinding.edtContent.text.toString()
            if (content.isNotEmpty()) {
                    listContent!![position].content = content
                    Toast.makeText(this@AddListContentActivity, "Edit content success!", Toast.LENGTH_SHORT)
                        .show()
                    alertDialog.dismiss()
                addContentAdapter!!.notifyDataSetChanged()
            } else {
                Toast.makeText(this@AddListContentActivity, "Content can't be empty!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        dialogBinding.tvCancel.clicks().throttleFirst(1, TimeUnit.SECONDS).subscribe() {
            alertDialog.dismiss()
        }

        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

    @SuppressLint("CheckResult")
    private fun showDialogAdd() {
        val buider = AlertDialog.Builder(this@AddListContentActivity)
        val dialogBinding = AddContentDialogBinding.inflate(layoutInflater)
        buider.setView(dialogBinding.root)

        val alertDialog = buider.create()

        dialogBinding.tvOk.clicks().throttleFirst(1, TimeUnit.SECONDS).subscribe() {
            val content = dialogBinding.edtContent.text.toString()
            if (content.isNotEmpty()) {
                listContent!!.add(ContentModel((listContent!!.size + 1), content))
                Toast.makeText(this@AddListContentActivity, "Add content success!", Toast.LENGTH_SHORT)
                    .show()
                alertDialog.dismiss()
                addContentAdapter!!.notifyDataSetChanged()
            } else {
                Toast.makeText(this@AddListContentActivity, "Content can't be empty!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        dialogBinding.tvCancel.clicks().throttleFirst(1, TimeUnit.SECONDS).subscribe() {
            alertDialog.dismiss()
        }

        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

}