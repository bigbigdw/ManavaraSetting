package com.bigbigdw.manavarasetting.util

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Calendar
import java.util.Date
import java.util.Locale

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

    fun year() : String{
        val currentDate = LocalDate.now()
        val currentYear = currentDate.year

        return currentYear.toString()
    }

    fun month(): String {
        val currentDate = LocalDate.now()
        val currentMonth = currentDate.monthValue
        return currentMonth.toString()
    }


    fun getWeekDates(year: Int, month: Int, weekNumber: Int): List<LocalDate> {
        val firstDayOfMonth = LocalDate.of(year, month, 1)
        val weekFields = WeekFields.of(Locale.getDefault())

        // 해당 월의 첫 번째 주의 시작일을 찾습니다.
        val firstDayOfFirstWeek = firstDayOfMonth
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            .with(weekFields.weekOfYear(), 1)

        // 요청된 주차의 시작일과 마지막일을 계산합니다.
        val startDate = firstDayOfFirstWeek.plusWeeks(weekNumber.toLong() - 1)
        val endDate = startDate.plusDays(6)

        val weekDates = mutableListOf<LocalDate>()
        var currentDate = startDate
        while (!currentDate.isAfter(endDate)) {
            weekDates.add(currentDate)
            currentDate = currentDate.plusDays(1)
        }

        return weekDates
    }

    fun getCurrentWeekNumber(): Int {
        val currentDate = LocalDate.now()
        val weekFields = WeekFields.of(Locale.getDefault())

        // 현재 날짜의 주 번호를 가져옵니다.
        return currentDate.get(weekFields.weekOfMonth())
    }

    fun getDayOfWeekAsNumber(): Int {
        val currentDate = LocalDate.now()
        val dayOfWeek = currentDate.dayOfWeek

        return if(dayOfWeek.value > 6){
            0
        } else {
            dayOfWeek.value
        }
    }

}