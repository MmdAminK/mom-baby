package com.app.mombaby.utils.utilities

import org.joda.time.DateTime
import saman.zamani.persiandate.PersianDate
import java.util.Calendar.*

object DateParser {

    fun changeSeparatorSign(date: String, sign: String = "/", splitRegex: String = "-"): String {
        return date.split(splitRegex).joinToString(sign)
    }

    fun convertToPersianDate(date: String, splitRegex: String = "-", sign: String = "/"): String {
        val arr = date.split(splitRegex)
        val date = PersianDate().initGrgDate(
            arr[0].toInt(),
            arr[1].toInt(),
            arr[2].toInt()
        )
        return String().plus(date.shYear).plus(sign)
            .plus(date.shMonth).plus(sign).plus(date.shDay)
    }

    fun convertToGrgDate(date: DateTime, sign: String = "-"): String {
        val dateTime = date.toGregorianCalendar()
        return String().plus(dateTime.get(YEAR)).plus(sign)
            .plus(dateTime.get(MONTH) + 1).plus(sign).plus(dateTime.get(DAY_OF_MONTH))
    }
}