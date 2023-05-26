package com.app.mombaby.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import com.app.mombaby.R
import com.app.mombaby.databinding.AppPersianCalendarBinding
import com.mohamadian.persianhorizontalexpcalendar.PersianHorizontalExpCalendar
import com.mohamadian.persianhorizontalexpcalendar.enums.PersianViewPagerType
import com.mohamadian.persianhorizontalexpcalendar.model.CustomGradientDrawable
import org.joda.time.Chronology
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.chrono.PersianChronologyKhayyam

//Todo : refactor this class
@SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
class AppPersianCalendar : CardView {

    private var currentDate: DateTime? = null
    var selectedDate: DateTime? = null

    private var binding: AppPersianCalendarBinding =
        AppPersianCalendarBinding.inflate(LayoutInflater.from(context), this, true)

    interface EvenListener {
        fun onDateSelectedListener(dateTime: DateTime)
    }

    private var evenListener: EvenListener? = null

    fun setEventListener(evenListener: EvenListener) {
        this.evenListener = evenListener
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr
    )

    init {
        currentDate = binding.appPersianCalendar.INIT_DATE

        this.cardElevation = 0f
        val perChr: Chronology =
            PersianChronologyKhayyam.getInstance(DateTimeZone.forID("Asia/Tehran"))
        binding.appPersianCalendar.setCenterContainerBackground(
            resources.getDrawable(R.drawable.fade_gradiant_vertical, null)
        )

        binding.appPersianCalendarNextMonth.setOnClickListener {
            currentDate?.let {
                binding.appPersianCalendar.scrollToDate(it.plusMonths(1))
            }
        }
        binding.appPersianCalendarPreviousMonth.setOnClickListener {
            currentDate?.let {
                binding.appPersianCalendar.scrollToDate(it.minusMonths(1))
            }
        }
        binding.appPersianCalendarMonth.text = monthNameAndYear(currentDate!!)

        binding.appPersianCalendar
            .setPersianHorizontalExpCalListener(object :
                PersianHorizontalExpCalendar.PersianHorizontalExpCalListener {
                @SuppressLint("SetTextI18n")
                override fun onCalendarScroll(dateTime: DateTime) {
                    binding.appPersianCalendarMonth.text = monthNameAndYear(dateTime)
                }

                override fun onDateSelected(dateTime: DateTime) {
                    selectedDate?.let {
                        binding.appPersianCalendar.markDate(
                            selectedDate, makeCustomGradiant(Color.WHITE, Color.BLACK)
                        )
                    }
                    selectedDate = dateTime
                    currentDate = dateTime
                    binding.appPersianCalendar.markDate(
                        dateTime, makeCustomGradiant("#C52184", Color.WHITE)
                    )
                    updateMarks()
                    evenListener?.onDateSelectedListener(dateTime)
                }

                override fun onChangeViewPager(persianViewPagerType: PersianViewPagerType) {
                }
            })

        binding.appPersianCalendar
            .setMarkSelectedDateCustomGradientDrawable(makeCustomGradiant("#C52184", Color.WHITE))
        createDayBackground(perChr)
        updateMarks()


    }

    private fun createDayBackground(perChr: Chronology) {
        for (day in -400..400)
            binding.appPersianCalendar.markDate(
                DateTime(perChr).plusDays(day), makeCustomGradiant(Color.WHITE, Color.BLACK)
            )
    }

    private fun updateMarks() {
        try {
            binding.appPersianCalendar.updateMarks()
        } catch (e: Exception) {
            Log.wtf("AppPersianCalendar", "updateMasks", e)
        }
    }

    private fun setTextAppearance() {
        binding.appPersianCalendar.setDayLabelsTypeface(
            Typeface.createFromAsset(
                resources.assets,
                "iran_sans_light.ttf"
            )
        )
        binding.appPersianCalendar.setDaysTypeface(
            Typeface.createFromAsset(
                resources.assets,
                "iran_sans_bold.ttf"
            )
        )
        binding.appPersianCalendar.setDaysTextSize(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 7f, resources.displayMetrics
            ).toInt()
        )
    }

    private fun makeCustomGradiant(color: Any, textColor: Int): CustomGradientDrawable {
        return CustomGradientDrawable(
            GradientDrawable.OVAL,
            if (color is String) Color.parseColor(color) else (color as Int)
        )
            .setViewLayoutGravity(Gravity.CENTER).setTextColor(textColor)
    }

    fun monthNameAndYear(dateTime: DateTime): String {
        return "${binding.appPersianCalendar.getMonthString_RTL(dateTime.monthOfYear)} ${dateTime.year}"
    }

    fun initDate(): DateTime = binding.appPersianCalendar.INIT_DATE
    fun scrollToDateTime(dateTime: DateTime) {
        binding.appPersianCalendar.scrollToDate(dateTime)
        binding.appPersianCalendar.invalidate()
        selectedDate = dateTime
        currentDate = dateTime
        binding.appPersianCalendar.markDate(
            dateTime, makeCustomGradiant("#C52184", Color.WHITE)
        )
        updateMarks()
    }

    fun clearSelectedDate() {

        selectedDate?.let {
            binding.appPersianCalendar.markDate(
                selectedDate, makeCustomGradiant(Color.WHITE, Color.BLACK)
            )
            selectedDate = null
            currentDate = null
            updateMarks()

        }
    }

}