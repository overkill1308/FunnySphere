package snake.funny.sphere.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import snake.funny.sphere.R
import snake.funny.sphere.listeners.OnOptionListener
import snake.funny.sphere.models.ContentModel


class AddContentAdapter(var contentList: ArrayList<ContentModel>, var onOptionListener: OnOptionListener) : RecyclerView.Adapter<AddContentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_edit_text_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contentModel = contentList[position]
        holder.tvNumber.text = contentModel.number.toString()
        holder.tvContent.text = contentModel.content
        holder.imgOption.setOnClickListener(View.OnClickListener {
            onOptionListener.optionClicked(it, position)
        })
    }

    override fun getItemCount(): Int {
        return contentList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNumber = itemView.findViewById<TextView>(R.id.tvNumber)
        val tvContent = itemView.findViewById<TextView>(R.id.tvContent)
        val imgOption = itemView.findViewById<ImageView>(R.id.ivOption)
    }

    fun refreshList(currentList: ArrayList<ContentModel>) {
        currentList.clear()
        currentList.addAll(contentList)
        notifyDataSetChanged()
    }

}