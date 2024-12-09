package com.example.haruka_journal_buddy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class PromptAdapter(private val itemList: List<SavedEntry>) : RecyclerView.Adapter<PromptAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val titleDate: TextView = itemView.findViewById(R.id.date_text)
        val titleHeader: TextView = itemView.findViewById(R.id.title_header)
        val titleDesc: TextView = itemView.findViewById(R.id.title_desc)

        init{
            itemView.setOnClickListener {
                if(bindingAdapterPosition != RecyclerView.NO_POSITION)
                    listener.onItemClick(bindingAdapterPosition)
            }
        }
    }

    private lateinit var promptListener: onItemClickListener

    interface onItemClickListener{ fun onItemClick(position: Int) }
    fun setOnItemClickListener(listener: onItemClickListener){ promptListener = listener }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.prompt_list_item, parent, false),
            promptListener
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentItem = itemList[position]
        viewHolder.titleDate.text = currentItem.date
        viewHolder.titleHeader.text = currentItem.heading
        viewHolder.titleDesc.text = currentItem.desc
    }

    override fun getItemCount() : Int { return itemList.size }
}