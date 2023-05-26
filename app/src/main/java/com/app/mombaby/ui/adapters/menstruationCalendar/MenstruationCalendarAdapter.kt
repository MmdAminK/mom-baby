package com.app.mombaby.ui.adapters.menstruationCalendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.mombaby.databinding.MenstruationCalendarBinding
import com.app.mombaby.models.Date

class MenstruationCalendarAdapter(
    private val datesList: ArrayList<ArrayList<ArrayList<Date>>>,
    private val firstFriday: ArrayList<Date?>? = null
) :
    RecyclerView.Adapter<MenstruationCalendarAdapter.ViewHolder>() {
    lateinit var binding: MenstruationCalendarBinding
    private val numberOfMonthShown = 3

    companion object {
        val daysOfWeekRange = 0..6
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            MenstruationCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recyclerMap: Map<Int, RecyclerView> = mapOf(
            0 to binding.recyclerViewWeekSaturday,
            1 to binding.recyclerViewWeekSunday,
            2 to binding.recyclerViewWeekMonday,
            3 to binding.recyclerViewWeekTuesday,
            4 to binding.recyclerViewWeekWednesday,
            5 to binding.recyclerViewWeekThursday,
            6 to binding.recyclerViewWeekFriday,
        )
        try {
            holder.bind(datesList[position], firstFriday!!, recyclerMap)
        } catch (e: Exception) {
        }

    }

    override fun getItemCount(): Int {
        return numberOfMonthShown
    }

    class ViewHolder(val binding: MenstruationCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val fakeDate: String = "1/11/2"
        fun bind(
            dates: ArrayList<ArrayList<Date>>?,
            firstFriDay: ArrayList<Date?>,
            recyclerMap: Map<Int, RecyclerView>
        ) {
            if (adapterPosition == 0) binding.labelGroup.visibility = View.VISIBLE
            binding.monthName.text = dates?.get(0)?.get(0)?.monthName

            for (dayOfWeek in daysOfWeekRange) {
                val handlerDate = ArrayList<Date>()
                handlerDate.addAll(dates?.get(dayOfWeek)!!)
                val firstFridayFlag = if (handlerDate[0].day > firstFriDay[adapterPosition]!!.day) {
                    handlerDate.add(0, Date(fakeDate))
                    true
                } else false
                setDayAdapter(recyclerMap[dayOfWeek]!!, handlerDate, firstFridayFlag)
            }
            binding.executePendingBindings()
        }

        private fun setDayAdapter(
            recyclerView: RecyclerView, dates: ArrayList<Date>, flag: Boolean
        ) {
            recyclerView.layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
            recyclerView.adapter = MenstruationDayAdapter(dates, flag)
        }

    }
}
