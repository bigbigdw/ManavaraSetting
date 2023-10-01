package com.bigbigdw.manavarasetting.util

import com.bigbigdw.manavarasetting.main.model.ItemBookInfo
import com.bigbigdw.manavarasetting.main.model.ItemBestInfo
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object BestRef {

    private val mRootRef = FirebaseDatabase.getInstance().reference

    fun setBookMonthlyBest(
        platform: String,
        genre: String,
        bookCode: String,
        type: String
    ): DatabaseReference {
        return setBestRef(platform = platform, genre = genre, type = type)
            .child("TROPHY_MONTH")
            .child(bookCode).child(DBDate.datedd())
    }

    fun setBookMonthlyBestTotal(platform: String, genre: String, bookCode: String, type: String): DatabaseReference {
        return setBestRef(platform = platform, genre = genre, type = type).child("TROPHY_MONTH_TOTAL").child(bookCode)
    }

    fun setBookWeeklyBest(platform: String, genre: String, bookCode: String, type: String): DatabaseReference {
        return setBestRef(platform = platform, genre = genre, type = type).child("TROPHY_WEEK").child(bookCode)
            .child(DBDate.getDayOfWeekAsNumber().toString())
    }

    fun setBookWeeklyBestTotal(platform: String, genre: String, bookCode: String, type: String): DatabaseReference {
        return setBestRef(platform = platform, genre = genre, type = type).child("TROPHY_WEEK_TOTAL").child(bookCode)
    }

    fun setBestTrophy(platform: String, genre: String, bookCode: String, type: String): DatabaseReference {
        return setBestRef(platform = platform, genre = genre, type = type).child("TROPHY").child(bookCode).child(DBDate.dateYYYY())
            .child(DBDate.dateMM()).child(DBDate.datedd())
    }

    fun setBookDailyBest(platform: String, num: Int, genre: String, type: String): DatabaseReference {
        return setBestRef(platform = platform, genre = genre, type = type).child("DAY").child(num.toString())
    }

    fun setBookCode(platform: String, genre: String, bookCode: String, type: String): DatabaseReference {
        return mRootRef.child("BOOK").child(type).child(platform).child(genre).child(bookCode)
    }

    fun setBestData(platform: String, bookCode: String, genre: String, type: String): DatabaseReference {
        return mRootRef.child("DATA").child(type).child(platform).child(genre).child(bookCode).child(DBDate.year()).child(DBDate.month()).child(DBDate.datedd())
    }

    fun setBookListDataBestAnalyze(ref: MutableMap<String?, Any>): ItemBestInfo {
        return ItemBestInfo(
            number = ref["current"] as Int,
            info1 = ref["info1"] as String,
            total = ref["current"] as Int,
            totalCount = ref["current"] as Int,
            bookCode = ref["bookCode"] as String,
        )
    }

    fun setBestRef(platform: String, genre: String, type: String): DatabaseReference {
        return mRootRef.child("BEST").child(type).child(platform).child(genre)
    }

    fun setBookListDataBest(ref: MutableMap<String?, Any>): ItemBookInfo {
        return ItemBookInfo(
            writer = ref["writerName"] as String,
            title = ref["subject"] as String,
            bookImg = ref["bookImg"] as String,
            bookCode = ref["bookCode"] as String,
            current = ref["current"] as Int,
            type = ref["type"] as String,
            info1 = ref["info1"] as String,
            info2 = ref["info2"] as String,
            info3 = ref["info3"] as String,
        )
    }
}