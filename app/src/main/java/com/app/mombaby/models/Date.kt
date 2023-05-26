package com.app.mombaby.models

import org.joda.time.Chronology
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.chrono.PersianChronologyKhayyam

data class Date(
    var date: String = "",
    var ovulation: Boolean = false,
    var period: Boolean = false,
    val day: Int = if (date.split("/").isNotEmpty()) date.split("/")[2].toInt() else -1,
    val month: Int = if (date.split("/").isNotEmpty()) date.split("/")[1].toInt() else -1,
    val year: Int = if (date.split("/").isNotEmpty()) date.split("/")[0].toInt() else -1,
    val perChr: Chronology =
        PersianChronologyKhayyam.getInstance(DateTimeZone.forID("Asia/Tehran")),
    val weekIndex: Int = DateTime(year, month, day, 1, 1, perChr).dayOfWeek - 1,
    val week: String = weekMap[weekIndex].toString(),
    val monthName: String = monthMap[month].toString()
) {
    companion object {
        val weekMap = mapOf(
            0 to "شنبه",
            1 to "یکشنبه",
            2 to "دوشنبه",
            3 to "سه شنبه",
            4 to "چارشنبه",
            5 to "پنجشنبه",
            6 to "جمعه",
        )
        val monthMap = mapOf(
            1 to "فروردین",
            2 to "اردیبهشت",
            3 to "خرداد",
            4 to "تیر",
            5 to "مرداد",
            6 to "شهریور",
            7 to "مهر",
            8 to "آبان",
            9 to "آذر",
            10 to "دی",
            11 to "بهمن",
            12 to "اسفند",
        )
    }

}