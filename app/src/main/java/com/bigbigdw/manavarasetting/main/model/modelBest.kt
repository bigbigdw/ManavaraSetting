package com.bigbigdw.manavarasetting.main.model

data class BestItemData (
    var writer: String = "",
    var title: String = "",
    var bookImg: String = "",
    var bookCode: String = "",
    var number: Int = 0,
    var type: String = "",
)

data class BestListAnalyze (
    var number: Int = 0,
    var date: String = "",
    var numberDiff: Int = 0,
    var trophyCount: Int = 0,
)