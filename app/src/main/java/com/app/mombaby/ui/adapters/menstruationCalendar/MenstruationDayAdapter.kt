package com.app.mombaby.ui.adapters.menstruationCalendar

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.mombaby.R
import com.app.mombaby.databinding.DayItemBinding
import com.app.mombaby.models.Date

class MenstruationDayAdapter(
    private val dates: ArrayList<Date>,
    private val firstFriday: Boolean = false
) :
    RecyclerView.Adapter<MenstruationDayAdapter.ViewHolder>() {

    lateinit var binding: DayItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            DayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dates[position], firstFriday)
    }

    override fun getItemCount(): Int {
        return dates.size
    }

    class ViewHolder(val binding: DayItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(date: Date?, firstFriDay: Boolean) {
            if (firstFriDay && adapterPosition == 0) binding.root.visibility = View.INVISIBLE
            if (date!!.ovulation) setCalendarIconStyle("#0BA700", R.drawable.ic_sperm)
            if (date.period) setCalendarIconStyle("#F26A8A", R.drawable.ic_blood)
            if (date.period && date.ovulation) {
                setCalendarIconStyle("#0BA700", R.drawable.ic_blood_sperm)
            }
            binding.day.text = date.day.toString()
            binding.executePendingBindings()
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        private fun setCalendarIconStyle(color: String, drawable: Int) {
            binding.apply {
                dayBack.setColorFilter(Color.parseColor(color))
                dayImage.setImageDrawable(root.context.resources.getDrawable(drawable, null))
            }
        }
    }
}
