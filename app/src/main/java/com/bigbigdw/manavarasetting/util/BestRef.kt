package com.bigbigdw.manavarasetting.util

import com.bigbigdw.manavarasetting.main.model.BestItemData
import com.bigbigdw.manavarasetting.main.model.BestListAnalyze
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object BestRef {

    private val mRootRef = FirebaseDatabase.getInstance().reference

    fun setBookMonthlyBest(type: String, genre: String, bookCode: String): DatabaseReference {
        return setBestRef(type, genre).child("TROPHY_MONTH").child(bookCode).child(DBDate.datedd())
    }
    fun setBookMonthlyBestTotal(type: String, genre: String, bookCode: String): DatabaseReference {
        return setBestRef(type, genre).child("TROPHY_MONTH_TOTAL").child(bookCode)
    }
    fun setBookWeeklyBest(type: String, genre: String, bookCode: String): DatabaseReference {
        return setBestRef(type, genre).child("TROPHY_WEEK").child(bookCode).child(DBDate.getDayOfWeekAsNumber().toString())
    }
    fun setBookWeeklyBestTotal(type: String, genre: String, bookCode: String): DatabaseReference {
        return setBestRef(type, genre).child("TROPHY_WEEK_TOTAL").child(bookCode)
    }

    fun setBookWeeklyBestYesterday(type: String, genre: String, bookCode: String): DatabaseReference {
        return setBestRef(type, genre).child("TROPHY_WEEK").child(bookCode).child((DBDate.getDayOfWeekAsNumber() - 1).toString())
    }

    fun setBestTrophy(type: String, genre: String, bookCode: String): DatabaseReference {
        return setBestRef(type, genre).child("TROPHY").child(bookCode).child(DBDate.dateYYYY()).child(DBDate.dateMM()).child(DBDate.datedd())
    }

    fun setBookDailyBest(platform: String, num: Int, genre: String): DatabaseReference {
        return  setBestRef(platform, genre).child("DAY").child(num.toString())
    }

    fun setBookCode(type: String, genre: String, bookCode: String): DatabaseReference {
        return setBestRef(type, genre).child("BOOKCODE").child(bookCode)
    }

    fun setBookListDataBestAnalyze(ref: MutableMap<String?, Any>): BestListAnalyze {
        return BestListAnalyze(
            ref["current"] as Int,
            ref["info1"] as String,
            ref["current"] as Int,
        )
    }

    fun setBestRef(platform: String, genre: String): DatabaseReference {
        return mRootRef.child("BEST").child(platform).child(genre)
    }

    fun setBookListDataBest(ref: MutableMap<String?, Any>): BestItemData {
        return BestItemData(
            ref["writerName"] as String,
            ref["subject"] as String,
            ref["bookImg"] as String,
            ref["bookCode"] as String,
            ref["current"] as Int,
            ref["type"] as String,
            ref["info1"] as String,
            ref["info2"] as String,
            ref["info3"] as String,
        )
    }
}