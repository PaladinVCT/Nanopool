package by.lebedev.nanopoolmonitoring.utils

import java.text.SimpleDateFormat
import java.util.*

enum class DatePatterns(override val value: String):StorableValueEnum<String>{
    HOUR_AND_MINUTES_PATTERN("HH:mm"),
    MONTH_DAY_HOUR_AND_MINUTES_PATTERN("dd MMM HH:mm:ss")
}

enum class TimeFrame {
    SECONDS,
    MINUTE,
    HOUR,
    DAY,
    WEEK,
    MONTH,
    INFINITE
}

fun Long.toFormattedDate(pattern:String):String{
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    formatter.timeZone = TimeZone.getDefault()
    return formatter.format(Date(this))
}