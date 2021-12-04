package com.luciane.ccta.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class DateTimeFormatter {
    companion object{
        fun getFormatedDateTimeFromMilliseconds(timestamp: Long): String{
            val dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timestamp),
                ZoneId.systemDefault()
            )
            val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT)
            return dateTime.format(formatter)
        }


        fun getCurrentFormatedDateTimeDayMonthYearHourMinute(): String{
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT)
            return currentDateTime.format(formatter)
        }

        fun getTimeFromMilliseconds(time : Long) : String{

            val dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault())

            val turn = if(dateTime.hour < 12) "AM" else "PM"

            return "${dateTime.hour} : ${dateTime.minute} $turn"
        }
    }
}