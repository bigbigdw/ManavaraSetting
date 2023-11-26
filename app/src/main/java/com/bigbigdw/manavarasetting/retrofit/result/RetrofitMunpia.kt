package com.bigbigdw.manavarasetting.retrofit.result

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BestMunpiaResult {
    @SerializedName("api")
    @Expose
    val api: BestMunpiaContents? = null
}

class BestMunpiaContents {
    @SerializedName("items")
    @Expose
    val items: List<BestMunpiaContentsItems>? = null
}

class BestMunpiaContentsItems {
    @SerializedName("author")
    @Expose
    var author: String = ""

    @SerializedName("nvStory")
    @Expose
    var nvStory: String = ""

    @SerializedName("nvGnMainTitle")
    @Expose
    var nvGnMainTitle: String = ""

    @SerializedName("nvSrl")
    @Expose
    var nvSrl: String = ""

    @SerializedName("nvCover")
    @Expose
    var nvCover: String = ""

    @SerializedName("nvTitle")
    @Expose
    var nvTitle: String = ""

    @SerializedName("nsrData")
    @Expose
    val nsrData: BestMunpiaNsrData? = null
}

class BestMunpiaNsrData {
    @SerializedName("hit")
    @Expose
    var hit: String = ""

    @SerializedName("hour")
    @Expose
    var hour: String = ""

    @SerializedName("number")
    @Expose
    var number: String = ""

    @SerializedName("prefer")
    @Expose
    var prefer: String = ""
}