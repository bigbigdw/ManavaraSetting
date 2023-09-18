package com.bigbigdw.manavarasetting.Util

import com.bigbigdw.manavarasetting.main.model.BestItemData
import com.bigbigdw.manavarasetting.main.model.BestListAnalyze
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object BestRef {

    private val mRootRef = FirebaseDatabase.getInstance().reference

    fun setBookCode(type: String, genre: String, bookCode: String): DatabaseReference {
        return setBestRef(type, genre).child("BOOKCODE").child(bookCode).child(DBDate.dateMMDD())
    }

    fun setBestData(platform: String, num: Int, genre: String): DatabaseReference {
        return  setBestRef(platform, genre).child("Data").child(DBDate.month()).child(DBDate.week()).child(DBDate.dayInt().toString()).child(num.toString())
    }

    fun setBookListDataBestAnalyze(ref: MutableMap<String?, Any>): BestListAnalyze {
        return BestListAnalyze(
            ref["info3"] as String,
            ref["info4"] as String,
            ref["info5"] as String,
            ref["info6"] as String,
            ref["number"] as Int,
            ref["date"] as String,
            0,
            0
        )
    }

    private fun setBestRef(platform: String, genre: String): DatabaseReference {
        return mRootRef.child("BEST").child(platform).child(genre)
    }

    fun setBookListDataBest(ref: MutableMap<String?, Any>): BestItemData {
        return BestItemData(
            ref["writerName"] as String,
            ref["subject"] as String,
            ref["bookImg"] as String,
            ref["bookCode"] as String,
            ref["info1"] as String,
            ref["info2"] as String,
            ref["info3"] as String,
            ref["info4"] as String,
            ref["info5"] as String,
            ref["info6"] as String,
            ref["number"] as Int,
            ref["date"] as String,
            ref["type"] as String
        )
    }
}