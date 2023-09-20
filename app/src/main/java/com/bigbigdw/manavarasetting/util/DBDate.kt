package com.bigbigdw.manavarasetting.util

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

object DBDate {

    @SuppressLint("SimpleDateFormat", "WeekBasedYear")
    fun dateYYYY(): String {
        val currentTime: Date = Calendar.getInstance().time
        val format = SimpleDateFormat("YYYY")
        return format.format(currentTime).toString()
    }

    @SuppressLint("SimpleDateFormat")
    fun dateMM(): String {
        val currentTime: Date = Calendar.getInstance().time
        val format = SimpleDateFormat("MM")
        return format.format(currentTime).toString()
    }

    @SuppressLint("SimpleDateFormat")
    fun datedd(): String {
        val currentTime: Date = Calendar.getInstance().time
        val format = SimpleDateFormat("dd")
        return format.format(currentTime).toString()
    }

    @SuppressLint("SimpleDateFormat", "WeekBasedYear")
    fun dateMMDD(): String {
        val currentTime: Date = Calendar.getInstance().time
        val format = SimpleDateFormat("YYYYMMdd")
        return format.format(currentTime).toString()
    }

    @SuppressLint("SimpleDateFormat")
    fun dateMMDDHHMM(): String {
        val currentTime: Date = Calendar.getInstance().time
        val format = SimpleDateFormat("YYYYMMddHHmm")
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