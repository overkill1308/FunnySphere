package snake.funny.sphere.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import snake.funny.sphere.R
import snake.funny.sphere.models.ContentModel

class AddContentAdapter(var contentList: ArrayList<ContentModel>) : RecyclerView.Adapter<AddContentAdapter.ViewHolder>() {

    var context: Context? = null
    var rootView: View? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_edit_text_content, parent, false)
        context = parent.context
        rootView = (context as Activity).window.decorView.findViewById(R.id.rootView)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contentModel = contentList[position]
        holder.tvTitleContent.text = contentModel.title
        holder.edtContent.hint = contentModel.content
    }

    override fun getItemCount(): Int {
        return contentList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitleContent = itemView.findViewById<TextView>(R.id.tvTitleContent)
        val edtContent = itemView.findViewById<EditText>(R.id.edtContent)
    }

    fun addContent() {
        val title = "Content ${contentList.size + 1}"
        val content = ContentModel(title, title)
        contentList.add(content)
        notifyDataSetChanged()
    }

    public fun refreshList(currentList: ArrayList<ContentModel>) {
        currentList.clear()
        currentList.addAll(contentList)
        notifyDataSetChanged()
    }

}