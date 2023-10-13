package com.bigbigdw.manavarasetting.main.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemBookInfo(
    @SerialName("writer")
    var writer: String = "",
    @SerialName("title")
    var title: String = "",
    @SerialName("bookImg")
    var bookImg: String = "",
    @SerialName("bookCode")
    var bookCode: String = "",
    @SerialName("type")
    var type: String = "",
    @SerialName("intro")
    var intro: String = "",
    @SerialName("cntPageRead")
    var cntPageRead: String = "",
    @SerialName("cntFavorite")
    var cntFavorite: String = "",
    @SerialName("cntRecom")
    var cntRecom: String = "",
    @SerialName("cntTotalComment")
    var cntTotalComment: String = "",
    @SerialName("cntChapter")
    var cntChapter: String = "",
    @SerialName("total")
    var total:  Int = 0,
    @SerialName("totalCount")
    var totalCount:  Int = 0,
    @SerialName("totalWeek")
    var totalWeek:  Int = 0,
    @SerialName("totalWeekCount")
    var totalWeekCount:  Int = 0,
    @SerialName("totalMonth")
    var totalMonth:  Int = 0,
    @SerialName("totalMonthCount")
    var totalMonthCount:  Int = 0,
    @SerialName("currentDiff")
    var currentDiff:  Int = 0,
    @SerialName("number")
    var number: Int = 0,
    @SerialName("point")
    var point: Int = 0,
    @SerialName("keyword")
    var keyword: ArrayList<String> = ArrayList(),
    @SerialName("genre")
    var genre: String = "",
    )
@Serializable
data class ItemBestInfo (
    @SerialName("number")
    var number: Int = 0,
    @SerialName("point")
    var point: Int = 0,
    @SerialName("cntPageRead")
    var cntPageRead: String = "",
    @SerialName("cntFavorite")
    var cntFavorite: String = "",
    @SerialName("cntRecom")
    var cntRecom: String = "",
    @SerialName("cntTotalComment")
    var cntTotalComment: String = "",
    @SerialName("total")
    var total:  Int = 0,
    @SerialName("totalCount")
    var totalCount:  Int = 0,
    @SerialName("bookCode")
    var bookCode: String = "",
    @SerialName("currentDiff")
    var currentDiff:  Int = 0,
)
@Serializable
class ItemBestKeyword(
    @SerialName("title")
    var title: String = "",
    @SerialName("value")
    var value: String = ""
)