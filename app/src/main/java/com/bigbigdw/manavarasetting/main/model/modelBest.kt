package com.bigbigdw.manavarasetting.main.model

data class BestItemData (
    var writer: String = "",
    var title: String = "",
    var bookImg: String = "",
    var bookCode: String = "",
    var info1: String = "",
    var info2: String = "",
    var info3: String = "",
    var info4: String = "",
    var info5: String = "",
    var info6: String = "",
    var number: Int = 0,
    var date: String = "",
    var type: String = "",
    var memo: String = "",
)

data class BestListAnalyze (
    var info1: String = "",
    var info2: String = "",
    var info3: String = "",
    var info4: String = "",
    var number: Int = 0,
    var date: String = "",
    var numberDiff: Int = 0,
    var trophyCount: Int = 0,
)