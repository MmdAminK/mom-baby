package com.app.mombaby.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.mombaby.databinding.ReminderItemBinding
import com.app.mombaby.models.reminder.Reminder
import com.app.mombaby.ui.fragments.mainPage.user.reminder.Reminder.Companion.periodMap
import com.app.mombaby.utils.utilities.DateParser.convertToPersianDate

class ReminderAdapter(val reminders: ArrayList<Reminder?>) :
    RecyclerView.Adapter<ReminderAdapter.ViewHolder>() {


    private lateinit var onRemoveClickFunc: (position: Int) -> Unit

    lateinit var onEditClickFunc: (position: Int) -> Unit
    lateinit var binding: ReminderItemBinding

    fun setOnRemoveClick(onRemoveClickFunc: (position: Int) -> Unit) {
        this.onRemoveClickFunc = onRemoveClickFunc
    }

    fun setOnEditClick(onEditClickFunc: (position: Int) -> Unit) {
        this.onEditClickFunc = onEditClickFunc
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ReminderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onRemoveClickFunc, onEditClickFunc)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(reminders[position], position)
    }

    override fun getItemCount(): Int {
        return reminders.size
    }

    class ViewHolder(
        val binding: ReminderItemBinding,
        val removeFunc: (position: Int) -> Unit,
        val editFunc: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(reminder: Reminder?, position: Int) {
            binding.reminderItemRemove.setOnClickListener { removeFunc(position) }
            binding.reminderItemEdit.setOnClickListener { editFunc(position) }
            reminder?.reminderPeriod = periodMap[reminder?.reminderPeriod]
            reminder?.reminderDate = convertToPersianDate(
                reminder?.reminderDate!!,
                reminder.reminderDate.toString()[4].toString()
            )
            binding.reminder = reminder
            binding.executePendingBindings()
        }

    }
}
