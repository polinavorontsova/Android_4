package com.ab.labs.planner.ui.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ab.labs.R
import com.ab.labs.planner.core.stringToBitmap
import com.ab.labs.planner.data.entity.Note

class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val imageView: ImageView = view.findViewById(R.id.noteImage)
    private val tittleView: TextView = view.findViewById(R.id.tittleView)
    private val textView: TextView = view.findViewById(R.id.textView)

    fun bind(note: Note) {
        tittleView.text = note.tittle
        textView.text = note.text

        if (note.icon.isNotEmpty()) {
            imageView.isVisible = true
            imageView.setImageBitmap(stringToBitmap(note.icon))
        } else {
            imageView.isVisible = false
            //imageView.setImageResource(R.drawable.ic_image_outline_24)
        }
    }

    companion object {
        fun create(parent: ViewGroup): NoteViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.note_item, parent, false)
            return NoteViewHolder(view)
        }
    }
}
