package com.example.bitfitp1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SleepAdapter(
    private val context: SleepFragment,
    private val sleeps: MutableList<DisplaySleep>,
) : RecyclerView.Adapter<SleepAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_sleep_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sleep = sleeps[position]
        holder.bind(sleep)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val date = itemView.findViewById<TextView>(R.id.sleep_date)
        private val notes  = itemView.findViewById<TextView>(R.id.sleep_notes)
        private val hours = itemView.findViewById<TextView>(R.id.sleep_hours)
        private val feeling = itemView.findViewById<TextView>(R.id.sleep_feeling)


        fun bind(sleep: DisplaySleep) {
            date.text = sleep.date.toString()
            notes.text = sleep.notes.toString()
            hours.text = sleep.hours.toString()
            feeling.text = sleep.feeling.toString()
        }
    }
    override fun getItemCount(): Int {
        return sleeps.size
    }
}
