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
        return mRootRef.child("TROPHY").child(type).child(platform).child(bookCode).child(DBDate.dateMMDD())

    }

    fun setBookDailyBest(platform: String, num: Int, type: String): DatabaseReference {
        return setBestRef(platform = platform, type = type).child("DAY").child(num.toString())
    }

    fun setBookCode(platform: String, bookCode: String, type: String): DatabaseReference {
        return mRootRef.child("BOOK").child(type).child(platform).child(bookCode)
    }

    fun setBestData(platform: String, bookCode: String, type: String): DatabaseReference {
        return mRootRef.child("DATA").child(type).child(platform).child(bookCode).child(DBDate.dateMMDD())
    }

    fun setBestGenre(platform: String, genreDate: String, type: String): DatabaseReference {
        return mRootRef.child("BEST").child(type).child(platform).child(genreDate)
    }

    fun setBestRef(platform: String, type: String): DatabaseReference {
        return mRootRef.child("BEST").child(type).child(platform)
    }

    fun setGenreRef(platform: String, type: String): DatabaseReference {
        return mRootRef.child("GENRE").child(type).child(platform)
    }

    fun setKeywordRef(platform: String, type: String): DatabaseReference {
        return mRootRef.child("KEYWORD").child(type).child(platform)
    }
}