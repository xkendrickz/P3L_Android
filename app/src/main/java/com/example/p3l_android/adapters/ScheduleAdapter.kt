package com.example.p3l_android.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.p3l_android.R
import com.example.p3l_android.models.ScheduleItem

class ScheduleAdapter : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {
    private var scheduleList: List<ScheduleItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_schedule, parent, false)
        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val scheduleItem = scheduleList[position]

        // Bind the data to the UI components in the item layout
        holder.titleTextView.text = scheduleItem.title
        holder.descriptionTextView.text = scheduleItem.description
        // Add more bindings for other properties if needed
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }

    fun setData(data: List<ScheduleItem>) {
        scheduleList = data
        notifyDataSetChanged()
    }

    inner class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        // Add more views if needed
    }
}