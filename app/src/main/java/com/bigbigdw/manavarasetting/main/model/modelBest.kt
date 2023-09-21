package com.bigbigdw.manavarasetting.main.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BestItemData(
    @SerialName("writer")
    var writer: String = "",
    @SerialName("title")
    var title: String = "",
    @SerialName("bookImg")
    var bookImg: String = "",
    @SerialName("bookCode")
    var bookCode: String = "",
    @SerialName("current")
    var current: Int = 0,
    @SerialName("type")
    var type: String = "",
    @SerialName("info1")
    var info1: String = "",
    @SerialName("info2")
    var info2: String = "",
    @SerialName("info3")
    var info3: String = "",
    @SerialName("total")
    var total:  Int = 0,
)

data class BestListAnalyze (
    var number: Int = 0,
    var info1: String = "",
)