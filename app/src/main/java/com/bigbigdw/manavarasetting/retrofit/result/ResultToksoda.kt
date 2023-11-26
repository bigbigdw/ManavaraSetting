package com.bigbigdw.manavarasetting.retrofit.result

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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

class BestToksodaDetailResult {
    @SerializedName("result")
    @Expose
    val result: BestToksodaDetailResultContents = BestToksodaDetailResultContents()
}

class BestToksodaDetailResultContents {
    @SerializedName("athrnm")
    @Expose
    var athrnm: String = ""

    @SerializedName("cmntCnt")
    @Expose
    var cmntCnt: String = ""

    @SerializedName("goodCnt")
    @Expose
    var goodCnt: String = ""

    @SerializedName("hashTagList")
    @Expose
    val hashTagList: List<BestToksodaDetailHashTagList>? = null

    @SerializedName("imgPath")
    @Expose
    var imgPath: String = ""

    @SerializedName("inqrCnt")
    @Expose
    var inqrCnt: String = ""

    @SerializedName("intrstCnt")
    @Expose
    var intrstCnt: String = ""

    @SerializedName("lnIntro")
    @Expose
    var lnIntro: String = ""

    @SerializedName("wrknm")
    @Expose
    var wrknm: String = ""

    @SerializedName("lgctgrNm")
    @Expose
    var lgctgrNm: String = ""
}

class BestToksodaDetailHashTagList {
    @SerializedName("hashtagNm")
    @Expose
    var hashtagNm: String = ""
}