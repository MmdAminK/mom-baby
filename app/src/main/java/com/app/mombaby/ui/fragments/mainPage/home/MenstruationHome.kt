package com.app.mombaby.ui.fragments.mainPage.home

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.app.mombaby.R
import com.app.mombaby.databinding.MenstruationHomeBinding
import com.app.mombaby.models.Date
import com.app.mombaby.ui.activities.AppMainPage.Companion.targetSeq
import com.app.mombaby.ui.adapters.ShowBlogsAdapter
import com.app.mombaby.ui.adapters.listeners.OnItemClickListener
import com.app.mombaby.ui.adapters.menstruationCalendar.MenstruationCalendarAdapter
import com.app.mombaby.utils.models.CacheKeys.homeTapTarget
import com.app.mombaby.utils.models.CacheKeys.menstruationDialogTapTarget
import com.app.mombaby.utils.models.CacheKeys.testsTapTarget
import com.app.mombaby.utils.models.CacheKeys.toolsTapTarget
import com.app.mombaby.utils.models.CacheKeys.userProfileTapTarget
import com.app.mombaby.utils.models.DataState
import com.app.mombaby.utils.utilities.AppAlertDialog
import com.app.mombaby.utils.utilities.CommonUtils.onBackPressed
import com.app.mombaby.utils.utilities.DateParser
import com.app.mombaby.utils.utilities.TapTargetBuilder.createTapTarget
import com.app.mombaby.utils.utilities.ViewUtils.showInternetWarningToast
import com.app.mombaby.viewModels.MenstruationEvent
import com.app.mombaby.viewModels.MenstruationHomeViewModel
import com.app.mombaby.viewModels.ShowBlogsEvent
import com.app.mombaby.viewModels.ShowBlogsViewModel
import com.app.mombaby.views.AppProgressBar
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.joda.time.DateTime
import org.joda.time.Days
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MenstruationHome : Fragment() {

    lateinit var binding: MenstruationHomeBinding

    val viewModel: MenstruationHomeViewModel by viewModels()

    val showBlogsViewModel: ShowBlogsViewModel by viewModels()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var progressBar: AppProgressBar
    private val angleMap = mutableMapOf<Int, Double>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.menstruation_home, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressed {
            val dialog = AppAlertDialog(requireContext())
            dialog.setMessage("آیا می خواهید از برنامه خارج شوید؟")
            dialog.setYesButtonAction {
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_HOME)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            dialog.setNoButtonAction { }
            dialog.showDialog()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!sharedPreferences.getBoolean(menstruationDialogTapTarget, false)) {
            TapTargetSequence(requireActivity()).target(
                requireActivity().createTapTarget(
                    5,
                    binding.menstruationShowDialog,
                    "تنظیمات",
                    "تنظیمات مربوط به قاعدگی"
                )
            )
                .listener(object : TapTargetSequence.Listener {
                    override fun onSequenceFinish() {
                    }

                    override fun onSequenceCanceled(lastTarget: TapTarget?) {
                    }

                    override fun onSequenceStep(lastTarget: TapTarget?, targetClicked: Boolean) {
                        sharedPreferences.edit().putBoolean(menstruationDialogTapTarget, true)
                            .apply()
                        appMainTargetSeq()
                    }
                })
                .start()
        } else {
            appMainTargetSeq()
        }

        binding.hello.text = "وقت بخیر " + sharedPreferences.getString("userFirstName", "")

        binding.circular.isEnabled = false
        binding.circular.isFocusable = false

        binding.menstruationAllArticles.setOnClickListener {
            val bundle = bundleOf(
                "title" to "مقالات قاعدگی",
                "week" to -1
            )
            findNavController().navigate(R.id.action_menstruationHome_to_showBlogs, bundle)
        }
        binding.menstruationDialogSubmit.setOnClickListener {
            binding.menstruationDialogCalendar.selectedDate?.let {
                if (it.isBeforeNow || it.isEqualNow) {
                    val date =
                        binding.menstruationDialogCalendar.selectedDate!!.toDateTimeISO().year.toString() + "-" +
                                binding.menstruationDialogCalendar.selectedDate!!.toDateTimeISO().monthOfYear + "-" +
                                binding.menstruationDialogCalendar.selectedDate!!.toDateTimeISO().dayOfMonth

                    viewModel.setStateEvent(
                        MenstruationEvent.UpdateUSerInfo(
                            date,
                            binding.menstruationDialogBetweenPeriod.value,
                            binding.menstruationDialogPeriodTime.value
                        )
                    )

                    viewModel.dataStateUpdateUSerInfo.observe(viewLifecycleOwner, { dataState ->
                        when (dataState) {
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
                                viewModel._dataState.value = null
                                viewModel.setStateEvent(MenstruationEvent.MenstruationInfo)
                                viewModel.setStateEvent(MenstruationEvent.PeriodOvulation)
                                binding.menstruationMotion.transitionToStart()
                            }
                        }
                    })
                } else {
                    Toast.makeText(
                        requireContext(),
                        "لطفا تاریخ از امروز و قبل از آن را وارد کنید",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } ?: run {
                Toast.makeText(requireContext(), "لطفا تاریخ را وارد کنید", Toast.LENGTH_SHORT)
                    .show()
            }

        }
        binding.menstruationStatus.setOnClickListener { binding.circular.performClick() }
        viewModel.setStateEvent(MenstruationEvent.MenstruationInfo)
        viewModel.dataState.observe(viewLifecycleOwner, { dataState ->
            when (dataState) {
                is DataState.Error -> {
                    binding.root.showInternetWarningToast()
                    progressBar.hide()
                }
                is DataState.Loading -> {
                    progressBar.show()
                }
                is DataState.RetrofitError -> {
                    binding.root.showInternetWarningToast()
                    progressBar.hide()
                }
                is DataState.Success -> {
                    progressBar.hide()
                    Log.d("TAG", "tgrtbtbrtb: ${dataState.data}")
                    binding.menstruationDialogBack.visibility = View.VISIBLE
                    val data = dataState.data
                    Handler(Looper.getMainLooper()).post {
                        data!!.nextOvulationDay?.let {
                            val first = data.firstOvulationDate!!.split("-")
                            val second = data.secondOvulationDate!!.split("-")
                            val date1 = DateTime(
                                first[0].toInt(), first[1].toInt(), first[2].toInt(), 1, 1
                            )
                            val date2 = DateTime(
                                second[0].toInt(), second[1].toInt(), second[2].toInt(), 1, 1
                            )
                            val betweenDays = Days.daysBetween(date1, date2).days
                            binding.nextOvulationDay.text =
                                data.nextOvulationDay?.toString() + " روز دیگر"

                            binding.nextPeriodDay.text =
                                "تقریبا " + data.nextPeriodDay?.toString() + " روز دیگر"

                            binding.menstruationStatus.text =
                                data.status
                            binding.nextPeriodDayCircle.text =
                                data.nextPeriodDay?.toString()

                            createMapAngle(betweenDays)
                            binding.circular.setOnSliderMovedListener {
                                binding.circular.setAngle(angleMap[data.nextPeriodDay]!!)
                            }
                            binding.circular.setAngle(angleMap[data.nextPeriodDay]!!)
                            binding.circular.invalidate()

                        } ?: run {
                            binding.menstruationShowDialog.performClick()
                            binding.menstruationDialogBack.visibility = View.GONE
                        }

                        with(binding.menstruationHomeRecyclerView) {
                            layoutManager = LinearLayoutManager(
                                requireContext(), VERTICAL, false
                            )
                            val articles = data.articles
                            val adapter = ShowBlogsAdapter(articles)
                            adapter.onItemClickListener(object : OnItemClickListener {
                                override fun onItemClick(position: Int) {
                                    showBlogsViewModel.setStateEvent(
                                        ShowBlogsEvent.SingleArticle(articles[position].id)
                                    )
                                    showBlogsViewModel.dataStateSingleArticle.observe(
                                        viewLifecycleOwner,
                                        {
                                            when (it) {
                                                is DataState.Error -> {
                                                    binding.root.showInternetWarningToast()
                                                }
                                                DataState.Loading -> {
                                                }
                                                is DataState.RetrofitError -> {
                                                    binding.root.showInternetWarningToast()
                                                }
                                                is DataState.Success -> {
                                                    val bundle =
                                                        bundleOf("blog" to it.data?.blog)
                                                    findNavController().navigate(
                                                        R.id.action_menstruationHome_to_blogItemDetail,
                                                        bundle
                                                    )
                                                }
                                            }
                                        })
                                }

                            })
                            this.adapter = adapter
                        }
                    }
                }
            }
        })

        viewModel.setStateEvent(MenstruationEvent.PeriodOvulation)
        viewModel.dataStatePeriodOvulation.observe(viewLifecycleOwner, { dataState ->
            when (dataState) {
                is DataState.Error -> {
                    binding.root.showInternetWarningToast()
                }
                DataState.Loading -> {
                }
                is DataState.RetrofitError -> {
                    binding.root.showInternetWarningToast()
                }
                is DataState.Success -> {
                    dataState.data?.let { it ->

                        Log.d("TAG", "onViewCreated: ${it}")

                        val set = mutableSetOf<Int>()
                        if (dataState.data.ovulation.isNotEmpty() && dataState.data.periods.isNotEmpty()) {
                            binding.menstruationCalendarCard.visibility = View.VISIBLE

                            Log.d("TAG", "onViewCreated: ${dataState.data.periods}")
                            Log.d("TAG", "onViewCreated: ${dataState.data.ovulation.size}")
                            val allDates = mutableListOf<Date>()
                            val duplicatedDates = mutableListOf<Date>()
                            val periodDates = mutableListOf<Date>()
                            val ovulationDates = mutableListOf<Date>()

                            dataState.data.periods.forEach {
                                val dateStr = DateParser.convertToPersianDate(it)
                                if (set.size < 3)
                                    set.add(dateStr.split("/")[1].toInt())
                                allDates.add(Date(dateStr, false, period = true))
                            }

                            dataState.data.ovulation.forEach {
                                val dateStr = DateParser.convertToPersianDate(it)
                                val newDate = Date(dateStr, true, period = false)
                                var flag = false
                                for (date in allDates)
                                    if ((date.day == newDate.day) && (date.month == newDate.month)) {
                                        date.ovulation = true
                                        date.period = true
                                        flag = true
                                    }
                                if (!flag)
                                    ovulationDates.add(newDate)
                            }
                            allDates.addAll(ovulationDates)

//                            val fMonth = set.first()
//                            var calMonth = fMonth + 1
//                            if (fMonth == 12)
//                                calMonth = 1
//                            set.add(calMonth)
//                            set.add(calMonth + 1)

//                           allDates.sortedBy { it.date }

                            val datesByMonth = mutableListOf<ArrayList<Date>>()
                            set.forEach { month ->
                                datesByMonth.add(allDates.filter { it.month == month } as ArrayList)
                            }
                            val dateList = ArrayList<Date>()

                            for (dates in datesByMonth) {
                                addRemainDates(
                                    dates, when (dates[0].month) {
                                        in 1..6 -> 31
                                        in 7..11 -> 30
                                        12 -> 29
                                        else -> 31
                                    }
                                )
                                dateList.addAll(dates)
                            }
                            val firstFriDay = ArrayList<Date?>()
                            val verticalDate = ArrayList<ArrayList<ArrayList<Date>>>()
                            for (dates in datesByMonth) {
                                firstFriDay.add(dates.firstOrNull { it.weekIndex == 6 })
                                val arr = ArrayList<ArrayList<Date>>()
                                for (i in 0..6)
                                    arr.addAll(listOf((dates.filter { it.weekIndex == i }) as ArrayList))
                                verticalDate.addAll(listOf(arr))
                            }

                            binding.menstruationCalendarRecyclerView.apply {
                                layoutManager =
                                    LinearLayoutManager(requireContext(), VERTICAL, false)
                                adapter = MenstruationCalendarAdapter(verticalDate, firstFriDay)
                                scrollToPosition(0)
                            }
                        } else {
                            binding.menstruationCalendarCard.visibility = View.GONE
                        }
                    }
                }
            } ?: run {
                binding.menstruationCalendarCard.visibility = View.GONE
            }
        })
    }

    private fun appMainTargetSeq() {
        if (sharedPreferences.getBoolean(homeTapTarget, false))
            if (sharedPreferences.getBoolean(toolsTapTarget, false))
                if (sharedPreferences.getBoolean(testsTapTarget, false))
                    if (sharedPreferences.getBoolean(userProfileTapTarget, false))
                        targetSeq.cancel()
                    else
                        targetSeq.startAt(3)
                else
                    targetSeq.startAt(2)
            else
                targetSeq.startAt(1)
        else
            targetSeq.startAt(0)
    }

    private fun createMapAngle(maxDay: Int) {
        val con = 6.32 / maxDay
        for (i in 0..maxDay)
            angleMap[i] = 1.55 - (((maxDay) - i) * con)
        angleMap[maxDay + 1] = 1.55
    }

    fun addRemainDates(dates: ArrayList<Date>, maxDay: Int) {
        val year = dates[0].year
        val month = dates[0].month
        var flag = false
        val dateHandler = ArrayList<Date>()
        for (i in 1..maxDay) {
            for (date in dates) {
                if (date.day == i) {
                    flag = false
                    break
                } else {
                    flag = true
                }
            }

            if (flag)
                dateHandler.add(
                    Date(
                        date = "$year/$month/$i",
                        ovulation = false,
                        period = false
                    )
                )
        }
        dates.addAll(dateHandler)
        dates.sortBy { it.day }
    }
}