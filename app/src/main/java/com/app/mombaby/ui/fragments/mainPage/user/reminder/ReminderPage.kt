package com.app.mombaby.ui.fragments.mainPage.user.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.mombaby.R
import com.app.mombaby.databinding.ReminderBinding
import com.app.mombaby.databinding.ReminderPageBinding
import com.app.mombaby.databinding.RemoveDialogBinding
import com.app.mombaby.models.reminder.Reminder
import com.app.mombaby.models.reminder.ReminderReqBody
import com.app.mombaby.ui.adapters.ReminderAdapter
import com.app.mombaby.ui.fragments.mainPage.user.reminder.Reminder.Companion.periodMap
import com.app.mombaby.ui.fragments.mainPage.user.reminder.Reminder.Companion.repeatMap
import com.app.mombaby.utils.models.DataState
import com.app.mombaby.utils.utilities.DateParser
import com.app.mombaby.utils.utilities.ViewUtils.showInternetWarningToast
import com.app.mombaby.utils.utilities.alarm.AlarmReceiver
import com.app.mombaby.viewModels.ReminderEvent
import com.app.mombaby.viewModels.ReminderViewModel
import com.app.mombaby.views.AppProgressBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.joda.time.Chronology
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.chrono.PersianChronologyKhayyam
import java.util.*
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
@AndroidEntryPoint
class ReminderPage(val type: String, private val reminderBinding: ReminderBinding) : Fragment() {

    @Inject
    lateinit var progressBar: AppProgressBar

    var reminders = ArrayList<Reminder?>()
    lateinit var binding: ReminderPageBinding
    private lateinit var reminderAdapter: ReminderAdapter
    val viewModel: ReminderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.reminder_page, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reminderAdapter = ReminderAdapter(reminders)
        val perChr: Chronology =
            PersianChronologyKhayyam.getInstance(DateTimeZone.forID("Asia/Tehran"))

        reminderAdapter.setOnEditClick { position ->

            reminderBinding.reminderDialogCalendar.clearSelectedDate()
            reminderBinding.reminderDialoSubmit.text = "ثبت تغییرات"
            val date = reminders[position]?.reminderDate!!.split("/")
            reminderBinding.reminderAdd.performClick()
            reminderBinding.reminderDialogTitleEditText.setText(reminders[position]?.title)

            val dateTime = DateTime(date[0].toInt(), date[1].toInt(), date[2].toInt(), 1, 1, perChr)

            Handler(Looper.myLooper()!!).postDelayed({
                reminderBinding.reminderDialogCalendar.scrollToDateTime(
                    DateTime(date[0].toInt(), date[1].toInt(), date[2].toInt(), 1, 1, perChr)
                )
            }, 1000)

            reminderBinding.reminderDialogRepeatCompleteText.setText(
                repeatMap.filter { it.value == reminders[position]?.repeatReminder }.keys.firstOrNull(),
                false
            )
            reminderBinding.reminderDialogPeriodCompleteText.setText(
                reminders[position]?.reminderPeriod, false
            )

            reminders[position]?.reminderTime?.toIntOrNull()?.let {
                reminderBinding.reminderDialogHour.value = it
            }
            reminderBinding.reminderDialogDay.value = reminders[position]?.reminderInterval!!

            val now = DateTime.now(perChr)
            reminderBinding.reminderDialoSubmit.setOnClickListener {
                val calendarDate = reminderBinding.reminderDialogCalendar.selectedDate
                calendarDate?.let {
                    if (calendarDate.isAfterNow ||
                        (calendarDate.dayOfMonth == now.dayOfMonth && calendarDate.monthOfYear == now.monthOfYear && calendarDate.year == now.year)
                    ) {
                        val reminder = ReminderReqBody(
                            id = reminders[position]?.id,
                            title = reminderBinding.reminderDialogTitleEditText.text.toString(),
                            type = (reminderBinding.reminderTabLayout.selectedTabPosition + 1).toString(),
                            reminder_date = DateParser.convertToGrgDate(calendarDate),
                            reminder_time = reminderBinding.reminderDialogHour.value.toString(),
                            repeat_reminder = repeatMap[reminderBinding.reminderDialogRepeatCompleteText.text.toString()],
                            reminder_period = periodMap.filter { it.value == reminderBinding.reminderDialogPeriodCompleteText.text.toString() }.keys.firstOrNull()
                                .toString(),
                            reminder_interval = reminderBinding.reminderDialogDay.value
                        )
                        viewModel.setStateEvent(ReminderEvent.EditReminder(reminder))
                        viewModel.dataStateEditReminder.observe(viewLifecycleOwner) {
                            when (it) {
                                is DataState.Error -> {
                                    progressBar.hide()
                                    binding.root.showInternetWarningToast()
                                }
                                DataState.Loading -> {
                                    progressBar.show()
                                }
                                is DataState.RetrofitError -> {
                                    progressBar.hide()
                                    binding.root.showInternetWarningToast()
                                }
                                is DataState.Success -> {
                                    progressBar.hide()
                                    removeAlarm(reminders[position]?.id!!)
                                    setAlarm(
                                        reminder, calendarDate.toGregorianCalendar(),
                                        reminders[position]?.id!!
                                    )
                                    reminderBinding.dialogBack.performClick()
                                    viewModel.setStateEvent(ReminderEvent.Reminders)
                                }
                            }
                        }
                    } else
                        Toast.makeText(
                            requireContext(),
                            "لطفا تاریخ از امروز و بعد از آن را وارد کنید",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                }
            }
        }
        reminderAdapter.setOnRemoveClick { position ->
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            val dialogBinding: RemoveDialogBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.remove_dialog,
                null,
                false
            )
            builder.setView(dialogBinding.root)

            val dialog: AlertDialog = builder.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialogBinding.message.text = "با حذف این یادآوری موافقی ؟"
            dialogBinding.btnYes.setOnClickListener {
                viewModel.setStateEvent(ReminderEvent.RemoveReminder(reminders[position]?.id!!))
                viewModel.dataStateRemoveReminder.observe(viewLifecycleOwner) {
                    when (it) {
                        is DataState.Error -> {
                            binding.root.showInternetWarningToast()
                            progressBar.hide()
                        }
                        DataState.Loading -> {
                            progressBar.show()
                        }
                        is DataState.RetrofitError -> {
                            binding.root.showInternetWarningToast()
                            progressBar.hide()
                        }
                        is DataState.Success -> {
                            progressBar.hide()
                            Handler(Looper.getMainLooper()).post {
                                removeAlarm(reminders[position]?.id!!)
                                removeAlarm(-1 * reminders[position]?.id!!)
                                reminderBinding.dialogBack.performClick()
                                viewModel.setStateEvent(ReminderEvent.Reminders)
                            }
                        }
                    }
                }
                dialog.cancel()
            }
            dialogBinding.btnNo.setOnClickListener { dialog.cancel() }
            dialog.show()
        }

        binding.reminderPageRecyclerView.layoutManager =
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
        binding.reminderPageRecyclerView.adapter = reminderAdapter

        viewModel.setStateEvent(ReminderEvent.Reminders)
        viewModel.dataState.observe(
            viewLifecycleOwner
        ) { dataState ->
            when (dataState) {
                is DataState.Loading -> {
                    progressBar.show()
                }
                is DataState.Success -> {
                    progressBar.hide()
                    Handler(Looper.getMainLooper()).post {
                        reminders.clear()
                        reminders.addAll(dataState.data!!.reminders.filter { it?.type == this.type } as ArrayList<Reminder?>)
                        reminderAdapter.notifyDataSetChanged()
                        binding.reminderPageRecyclerView.smoothScrollToPosition(0)
                        if (reminders.size == 0)
                            binding.noReminder.visibility = View.VISIBLE
                        else
                            binding.noReminder.visibility = View.GONE
                    }
                }
                is DataState.Error -> {
                    binding.root.showInternetWarningToast()
                    progressBar.hide()
                }
                is DataState.RetrofitError -> {
                    binding.root.showInternetWarningToast()
                    progressBar.hide()
                }
            }
        }

    }

    fun updateReminders() {
        viewModel.setStateEvent(ReminderEvent.Reminders)
    }


    private fun removeAlarm(id: Int) {
        val alarmMgr = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(requireActivity(), AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(
                requireActivity(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        alarmMgr.cancel(alarmIntent)
    }

    private fun setAlarm(reminder: ReminderReqBody, date: GregorianCalendar, id: Int) {
        val calendar = createDateForAlarm(date, reminder.reminder_time!!.toInt())
        createAlarm(calendar, reminder, id)

        val nextCalendar = createDateForAlarm(
            date, reminder.reminder_time.toInt(), alarmRepeat(reminder.repeat_reminder!!)
        )
        createAlarm(nextCalendar, reminder, -1 * id)
    }

    private fun createAlarm(calendar: Calendar, reminder: ReminderReqBody, id: Int) {
        val alarmMgr = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(requireActivity(), AlarmReceiver::class.java).let { intent ->
            intent.putExtra("title", reminder.title)
            intent.putExtra("id", reminder.id)
            PendingIntent.getBroadcast(requireActivity(), id, intent, FLAG_UPDATE_CURRENT)
        }
        alarmMgr.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis, alarmInterval(reminder.reminder_period!!), alarmIntent
        )
    }

    private fun createDateForAlarm(
        date: GregorianCalendar, hour: Int, minute: Int = 0
    ): Calendar {
        val calendar = Calendar.getInstance()
        calendar.apply {
            set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH))
            set(Calendar.MONTH, date.get(Calendar.MONTH))
            set(Calendar.YEAR, date.get(Calendar.YEAR))
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        return calendar
    }

    private fun alarmInterval(reminderPeriod: String): Long {
        return when (reminderPeriod) {
            "daily" -> reminderBinding.reminderDialogDay.value.toLong() * AlarmManager.INTERVAL_DAY
            "weekly" -> 7 * AlarmManager.INTERVAL_DAY
            "monthly" -> 30 * AlarmManager.INTERVAL_DAY
            "yearly" -> 365 * AlarmManager.INTERVAL_DAY
            else -> 0
        }
    }

    private fun alarmRepeat(reminderRepeat: String): Int {
        return when (reminderRepeat) {
            "5 دقیقه بعد", "5 minutes" -> 5
            "10 دقیقه بعد", "10 minutes" -> 10
            "30 دقیقه بعد", "30 minutes" -> 30
            "1 ساعت بعد", "1 hours" -> 59
            else -> 0
        }
    }
}
