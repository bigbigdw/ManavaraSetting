package com.bigbigdw.manavarasetting.retrofit.result

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class OneStoreBookResult {
    @SerializedName("params")
    @Expose
    var params: OneStoreBookResultParam? = null
}

class OneStoreBookResultParam {
    @SerializedName("productList")
    @Expose
    var productList: List<OnestoreBookItem>? = null
}

class OnestoreBookItem {
    @SerializedName("prodId")
    @Expose
    var prodId: String = ""

    @SerializedName("prodNm")
    @Expose
    var prodNm: String = ""

    @SerializedName("artistNm")
    @Expose
    var artistNm: String = ""

    @SerializedName("totalCount")
    @Expose
    var totalCount: String = ""

    @SerializedName("avgScore")
    @Expose
    var avgScore: String = ""

    @SerializedName("commentCount")
    @Expose
    var commentCount: String = ""

    @SerializedName("thumbnailImageUrl")
    @Expose
    var thumbnailImageUrl: String = ""

}

class BestMoonpiaResult {
    @SerializedName("api")
    @Expose
    val api: BestMoonpiaContents? = null
}

class BestMoonpiaContents {
    @SerializedName("items")
    @Expose
    val items: List<BestMoonpiaContentsItems>? = null
}

class BestMoonpiaContentsItems {
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
    val nsrData: BestMoonpiaNsrData? = null
}

class BestMoonpiaNsrData {
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

class BestToksodaResult {
    @SerializedName("resultList")
    @Expose
    val resultList: List<BestToksodaToksodaResultList>? = null
}

class BestToksodaToksodaResultList {
    @SerializedName("athrnm")
    @Expose
    var athrnm: String = ""

    @SerializedName("brcd")
    @Expose
    var brcd: String = ""

    @SerializedName("wrknm")
    @Expose
    var wrknm: String = ""

    @SerializedName("whlEpsdCnt")
    @Expose
    var whlEpsdCnt: String = ""

    @SerializedName("lnIntro")
    @Expose
    var lnIntro: String = ""

    @SerializedName("lgctgrNm")
    @Expose
    var lgctgrNm: String = ""

    @SerializedName("inqrCnt")
    @Expose
    var inqrCnt: String = ""

    @SerializedName("imgPath")
    @Expose
    var imgPath: String = ""

    @SerializedName("intrstCnt")
    @Expose
    var intrstCnt: String = ""

    @SerializedName("goodAllCnt")
    @Expose
    var goodAllCnt: String = ""
}

