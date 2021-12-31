package com.example.contentprovider.adapter

import android.database.Cursor
import android.view.LayoutInflater
import com.example.contentprovider.sharedata.NotesDataBaseHelp.Companion.DESCRIPTION_NOTES

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.contentprovider.R
import com.example.contentprovider.sharedata.NotesDataBaseHelp.Companion.TITLE_NOTES

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

     private var cursor: Cursor? = null
     private lateinit var listenerRemove: OnRemoveItem
     private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NotesViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        cursor?.moveToPosition(position)
        holder.bindViews(cursor)

        holder.btRemove.setOnClickListener {
            cursor?.moveToPosition(position)
            listenerRemove.onRemoveItem(cursor)

        }

        holder.itemView.setOnClickListener {
            cursor?.moveToPosition(position)
            listener.onItemClick(cursor)
        }

    }

    fun setCursor(cursor: Cursor?) {
        this.cursor = cursor
        notifyDataSetChanged()
    }

    override fun getItemCount() = cursor?.count ?: 0

    class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var title: TextView = itemView.findViewById(R.id.note_title)
        private var description: TextView = itemView.findViewById(R.id.note_description)
        var btRemove: Button = itemView.findViewById(R.id.bt_remove)


        fun bindViews(cursor: Cursor?) {
            with(cursor) {
                title.text = this?.getString(getColumnIndexOrThrow(TITLE_NOTES))
                description.text =  this?.getString(getColumnIndexOrThrow(DESCRIPTION_NOTES))

            }
        }

    }

    fun setRemoveItem(listener: OnRemoveItem) {
        this.listenerRemove = listener
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}