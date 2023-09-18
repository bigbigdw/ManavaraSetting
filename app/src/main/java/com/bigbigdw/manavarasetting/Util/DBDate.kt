package com.bigbigdw.manavarasetting.Util

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

object DBDate {
    @SuppressLint("SimpleDateFormat", "WeekBasedYear")
    fun dateMMDD(): String {
        val currentTime: Date = Calendar.getInstance().time
        val format = SimpleDateFormat("YYYYMMdd")
        return format.format(currentTime).toString()
    }

    fun month(): String {
        return Calendar.getInstance().get(Calendar.MONTH).toString()
    }

    fun dayInt(): Int {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
    }

    fun week(): String {
        return Calendar.getInstance().get(Calendar.WEEK_OF_MONTH).toString()
    }

}