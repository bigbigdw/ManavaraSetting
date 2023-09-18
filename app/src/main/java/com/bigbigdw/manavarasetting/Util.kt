package com.bigbigdw.manavarasetting

import android.icu.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


fun DateMMDDHHMM(): String {
    val currentTime: Date = Calendar.getInstance().time
    val format = SimpleDateFormat("YYYYMMddHHmm")
    return format.format(currentTime).toString()
}