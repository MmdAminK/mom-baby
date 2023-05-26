package com.app.mombaby.ui.fragments.mainPage.user.reminder

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.ComponentName
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ScrollView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.mombaby.R
import com.app.mombaby.databinding.ReminderBinding
import com.app.mombaby.models.reminder.ReminderReqBody
import com.app.mombaby.ui.adapters.ViewPagerAdapter
import com.app.mombaby.utils.models.DataState
import com.app.mombaby.utils.utilities.DateParser.convertToGrgDate
import com.app.mombaby.utils.utilities.ViewUtils.makeSnackBar
import com.app.mombaby.utils.utilities.ViewUtils.onTextChangeListener
import com.app.mombaby.utils.utilities.ViewUtils.showInternetWarningToast
import com.app.mombaby.utils.utilities.alarm.AlarmReceiver
import com.app.mombaby.viewModels.ReminderEvent
import com.app.mombaby.viewModels.ReminderViewModel
import com.app.mombaby.views.AppProgressBar
import com.google.android.material.tabs.TabLayoutMediator
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
class Reminder : Fragment() {

    var isReminderDialogOpen: Boolean = false

    @Inject
    lateinit var progressBar: AppProgressBar

    companion object {
        val tabMap = mapOf(
            0 to "قرص ها",
            1 to "آزمایشات",
            2 to "معاینات"
        )
        val periodMap = mapOf(
            "daily" to "روزانه",
            "weekly" to "هفتگی",
            "monthly" to "ماهانه",
            "yearly" to "سالانه"
        )
        val repeatMap = mapOf(
            "5 دقیقه بعد" to "5 minutes",
            "10 دقیقه بعد" to "10 minutes",
            "30 دقیقه بعد" to "30 minutes",
            "1 ساعت بعد" to "1 hours"
        )
        val repeatList = repeatMap.keys.toTypedArray()
        val periodList = periodMap.values.toTypedArray()
    }

    lateinit var binding: ReminderBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    val fragments = ArrayList<ReminderPage>()
    val viewModel: ReminderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.reminder, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isReminderDialogOpen) onDialogBackClick() else findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        requireContext().packageManager.setComponentEnabledSetting(
            ComponentName(requireContext(), AlarmReceiver::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    @SuppressLint("BatteryLife")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent()
            val packageName: String = requireActivity().packageName
            val pm = requireContext().getSystemService(POWER_SERVICE) as PowerManager?
            if (!pm!!.isIgnoringBatteryOptimizations(packageName)) {
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
                binding.root.makeSnackBar(
                    "برنامه برای اعلان یادآوری نیاز به اجرا در پس زمینه را دارد." +
                            "به همین منطور بر روی دکمه allow ضربه بزنید"
                )
            }
        }
        binding.reminderAdd.setOnClickListener {
            binding.reminderDialogScroll.fullScroll(ScrollView.FOCUS_UP)
            isReminderDialogOpen = true
            binding.reminderMotion.transitionToEnd()
        }

        binding.reminderDialogDay.smoothScrollToPosition(0)
        binding.reminderDialogDay.value = 0

        binding.reminderActionBar.onBackClickListener { findNavController().popBackStack() }
        createReminderPagerAdapter()

        TabLayoutMediator(
            binding.reminderTabLayout, binding.reminderViewPager
        ) { tab, position -> tab.text = tabMap.getValue(position) }.attach()

        binding.dialogBack.setOnClickListener { onDialogBackClick() }

        binding.reminderTabLayout.apply { selectTab(getTabAt(0)) }
        viewPagerAdapter.notifyDataSetChanged()
        val adapter = ArrayAdapter(requireContext(), R.layout.auto_complete_item, periodList)
        binding.reminderDialogPeriodCompleteText.setAdapter(adapter)

        binding.reminderDialogPeriodCompleteText.onTextChangeListener {
            if (it == "روزانه") {
                binding.reminderDialogDay.smoothScrollToPosition(0)
                binding.reminderDialogDay.value = 0
                binding.reminderDialogDay.visibility = View.VISIBLE
                binding.reminderDialogDayText.visibility = View.VISIBLE
            } else {
                binding.reminderDialogDay.visibility = View.GONE
                binding.reminderDialogDayText.visibility = View.GONE
            }
        }

        val repeatAdapter = ArrayAdapter(requireContext(), R.layout.auto_complete_item, repeatList)
        binding.reminderDialogRepeatCompleteText.setAdapter(repeatAdapter)

        val perChr: Chronology =
            PersianChronologyKhayyam.getInstance(DateTimeZone.forID("Asia/Tehran"))
        val now = DateTime.now(perChr)

        binding.apply {
            reminderDialoSubmit.setOnClickListener {
                val selectedDate = binding.reminderDialogCalendar.selectedDate
                if (TextUtils.isEmpty(binding.reminderDialogTitleEditText.text.toString()) ||
                    reminderDialogPeriodCompleteText.text.toString().isEmpty() ||
                    reminderDialogRepeatCompleteText.text.toString()
                        .isEmpty() || selectedDate == null
                )
                    binding.root.makeSnackBar("لطفا اطلاعات را وارد کنید")
                else if (
                    (selectedDate.dayOfMonth == now.dayOfMonth && selectedDate.monthOfYear == now.monthOfYear &&
                            selectedDate.year == now.year) || selectedDate.isAfterNow
                )
                    addReminder(selectedDate)
                else
                    Toast.makeText(
                        requireContext(),
                        "لطفا تاریخ از امروز و قبل از آن را وارد کنید",
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }
    }

    private fun createReminderPagerAdapter() {
        viewPagerAdapter = ViewPagerAdapter(requireActivity())
        val pillFragment = ReminderPage("1", binding)
        val testFragment = ReminderPage("2", binding)
        val visitFragment = ReminderPage("3", binding)
        fragments.add(pillFragment)
        fragments.add(testFragment)
        fragments.add(visitFragment)
        viewPagerAdapter.addFragment(pillFragment)
        viewPagerAdapter.addFragment(testFragment)
        viewPagerAdapter.addFragment(visitFragment)
        binding.reminderViewPager.adapter = viewPagerAdapter
    }

    private fun onDialogBackClick() {
        isReminderDialogOpen = false
        clearDialogInputs()
        binding.reminderDialoSubmit.text = "ثبت یادآور جدید"
        binding.reminderMotion.transitionToStart()
    }

    private fun addReminder(date: DateTime) {
        binding.apply {
            binding.reminderDialogCalendar.scrollToDateTime(binding.reminderDialogCalendar.initDate())
            val reminder = ReminderReqBody(
                title = reminderDialogTitleEditText.text.toString(),
                type = (reminderTabLayout.selectedTabPosition + 1).toString(),
                reminder_date = convertToGrgDate(date),
                reminder_time = reminderDialogHour.value.toString(),
                repeat_reminder = repeatMap[reminderDialogRepeatCompleteText.text.toString()],
                reminder_period = periodMap.filter { it.value == reminderDialogPeriodCompleteText.text.toString() }.keys.firstOrNull()
                    .toString(),
                reminder_interval = reminderDialogDay.value
            )
            viewModel.setStateEvent(ReminderEvent.AddReminder(reminder))
            viewModel.dataStateAddReminder.observe(viewLifecycleOwner) {
                when (it) {
                    is DataState.Success -> {
                        progressBar.hide()
                        setAlarm(reminder, date.toGregorianCalendar(), it.data?.reminderId!!)
                        binding.reminderMotion.transitionToStart()
                        clearDialogInputs()
                        fragments[reminderTabLayout.selectedTabPosition].updateReminders()
                    }
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
                }
            }
        }
        binding.reminderDialogRepeatCompleteText.setText("", false)
        binding.reminderDialogPeriodCompleteText.setText("", false)
    }

    private fun clearDialogInputs() {
        binding.reminderDialogTitleEditText.text?.clear()
        binding.reminderDialogPeriodCompleteText.text?.clear()
        binding.reminderDialogRepeatCompleteText.text?.clear()
        binding.reminderDialogCalendar.clearSelectedDate()
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
            timeInMillis = System.currentTimeMillis()
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
            "daily" -> binding.reminderDialogDay.value.toLong() * AlarmManager.INTERVAL_DAY
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

