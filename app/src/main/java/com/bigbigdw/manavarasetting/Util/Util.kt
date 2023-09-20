package com.bigbigdw.manavarasetting.Util

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


@SuppressLint("SimpleDateFormat")
fun dateMMDDHHMM(): String {
    val currentTime: Date = Calendar.getInstance().time
    val format = SimpleDateFormat("YYYYMMddHHmm")
    return format.format(currentTime).toString()
}

val NaverSeriesGenre = arrayListOf(
    "ALL",
    "99",
    "93",
    "90",
    "88",
    "107",
)

fun getNaverSeriesGenre(genre : String) : String {
    if(genre == "ALL"){
        return "ALL"
    } else if(genre == "99"){
        return "MELO"
    } else if(genre == "93"){
        return "DRAMA"
    } else if(genre == "90"){
        return "YOUNG"
    } else if(genre == "88"){
        return "ACTION"
    }else if(genre == "107"){
        return "BL"
    } else {
        return "없음"
    }
}


