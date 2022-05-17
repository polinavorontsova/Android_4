package com.ab.labs.planner.ui.notes

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ab.labs.planner.data.entity.Note


class NotesAdapter() : RecyclerView.Adapter<NoteViewHolder>() {

    var notes: ArrayList<Note> = arrayListOf()
    var shotClickListener: ((Note, v: View) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        shotClickListener?.apply {
            holder.itemView.setOnClickListener { invoke(note, it) }
        }
        holder.bind(note)
    }

    override fun getItemCount() = notes.size

    fun setData(newItems: ArrayList<Note>) {
        val diffResult = DiffUtil.calculateDiff(BaseDiffCallback(notes, newItems))
        notes.clear()
        notes.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class BaseDiffCallback(
        private val oldList: List<Note>, private val newList: List<Note>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldPosition: Int, newPosition: Int) =
            oldList[oldPosition] == newList[newPosition]
    }

}

