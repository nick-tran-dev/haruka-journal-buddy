package com.example.haruka_journal_buddy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

//class PromptAdapter(private val itemList: List<String>) : RecyclerView.Adapter<PromptAdapter.ViewHolder>()

class PromptAdapter(private val itemList: List<SavedEntry>) : RecyclerView.Adapter<PromptAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val titleImage: ShapeableImageView = itemView.findViewById(R.id.title_image)
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

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        promptListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.prompt_list_item, parent, false),
            promptListener
        )
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        //viewHolder.textView.text = itemList[position]

        val currentItem = itemList[position]
        viewHolder.titleImage.setImageResource(currentItem.titleImage)
        viewHolder.titleHeader.text = currentItem.heading
        viewHolder.titleDesc.text = currentItem.desc
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() : Int { return itemList.size }
}