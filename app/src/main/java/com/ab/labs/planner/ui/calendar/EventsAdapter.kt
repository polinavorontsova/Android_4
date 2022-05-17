package com.ab.labs.planner.ui.calendar

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ab.labs.planner.data.entity.Event


class EventsAdapter : ListAdapter<Event, EventViewHolder>(REPO_COMPARATOR) {

    var shotClickListener: ((Event, v: View) -> Unit)? = null
    var deleteClickListener: ((Event) -> Unit)? = null
    var editClickListener: ((Event) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { event ->
            shotClickListener?.apply {
                holder.itemView.setOnClickListener { invoke(event, it) }
            }
            deleteClickListener?.apply {
                holder.deleteImage.setOnClickListener { invoke(event) }
            }
            editClickListener?.apply {
                holder.editImage.setOnClickListener { invoke(event) }
            }
            holder.bind(event)
        }
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Event>() {
            override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean =
                oldItem == newItem
        }
    }
}
