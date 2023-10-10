package com.bigbigdw.manavarasetting.util

import com.bigbigdw.manavarasetting.main.model.ItemBookInfo
import com.bigbigdw.manavarasetting.main.model.ItemBestInfo
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object BestRef {

    private val mRootRef = FirebaseDatabase.getInstance().reference

    fun setBookMonthlyBest(
        platform: String,
        bookCode: String,
        type: String
    ): DatabaseReference {
        return setBestRef(platform = platform, type = type)
            .child("TROPHY_MONTH")
            .child(bookCode).child(DBDate.datedd())
    }

    fun setBookMonthlyBestTotal(platform: String, bookCode: String, type: String): DatabaseReference {
        return setBestRef(platform = platform, type = type).child("TROPHY_MONTH_TOTAL").child(bookCode)
    }

    fun setBookWeeklyBest(platform: String, bookCode: String, type: String): DatabaseReference {
        return setBestRef(platform = platform, type = type).child("TROPHY_WEEK").child(bookCode)
            .child(DBDate.getDayOfWeekAsNumber().toString())
    }

    fun setBookWeeklyBestTotal(platform: String, bookCode: String, type: String): DatabaseReference {
        return setBestRef(platform = platform, type = type).child("TROPHY_WEEK_TOTAL").child(bookCode)
    }

    fun setBestTrophy(platform: String, bookCode: String, type: String): DatabaseReference {
        return setBestRef(platform = platform, type = type).child("TROPHY").child(bookCode).child(DBDate.dateYYYY())
            .child(DBDate.dateMM()).child(DBDate.datedd())
    }

    fun setBookDailyBest(platform: String, num: Int, type: String): DatabaseReference {
        return setBestRef(platform = platform, type = type).child("DAY").child(num.toString())
    }

    fun setBookCode(platform: String, bookCode: String, type: String): DatabaseReference {
        return mRootRef.child("BOOK").child(type).child(platform).child(bookCode)
    }

    fun setBestData(platform: String, bookCode: String, type: String): DatabaseReference {
        return mRootRef.child("DATA").child(type).child(platform).child(bookCode).child(DBDate.year()).child(DBDate.month()).child(DBDate.datedd())
    }

    fun setItemBestInfoRef(ref: MutableMap<String?, Any>): ItemBestInfo {
        return ItemBestInfo(
            number = ref["number"] as Int,
            point = ref["point"] as Int,
            cntPageRead = ref["cntPageRead"] as String? ?: "",
            cntFavorite = ref["cntFavorite"] as String? ?: "",
            cntRecom = ref["cntRecom"] as String? ?: "",
            cntTotalComment = ref["cntTotalComment"] as String? ?: "",
            total = ref["total"] as Int,
            totalCount = ref["totalCount"] as Int,
            bookCode = ref["bookCode"] as String,
            currentDiff = ref["currentDiff"] as Int,
        )
    }

    fun setBestRef(platform: String, type: String): DatabaseReference {
        return mRootRef.child("BEST").child(type).child(platform)
    }

    fun setItemBookInfoRef(ref: MutableMap<String?, Any>): ItemBookInfo {
        return ItemBookInfo(
            writer = ref["writerName"] as String,
            title = ref["subject"] as String,
            bookImg = ref["bookImg"] as String,
            bookCode = ref["bookCode"] as String,
            number = ref["number"] as Int,
            point = ref["point"] as Int,
            type = ref["type"] as String,
            intro = ref["intro"] as String? ?: "",
            cntPageRead = ref["cntPageRead"] as String? ?: "",
            cntFavorite = ref["cntFavorite"] as String? ?: "",
            cntRecom = ref["cntRecom"] as String? ?: "",
            cntChapter = ref["cntChapter"] as String? ?: "",
            cntTotalComment = ref["cntTotalComment"] as String? ?: "",
            total = ref["total"] as Int,
            totalCount = ref["totalCount"] as Int,
            totalWeek = ref["totalWeek"] as Int,
            totalWeekCount = ref["totalWeekCount"] as Int,
            totalMonth = ref["totalMonth"] as Int,
            totalMonthCount = ref["totalMonthCount"] as Int,
            currentDiff = ref["currentDiff"] as Int,
        )
    }
}