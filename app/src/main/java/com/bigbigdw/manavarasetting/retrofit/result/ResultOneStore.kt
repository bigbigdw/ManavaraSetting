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
class OnestoreBookDetail {
    var params: OnestoreBookDetailContents = OnestoreBookDetailContents()
}

class OnestoreBookDetailContents {
    @SerializedName("artistNm")
    @Expose
    var artistNm: String = ""

    @SerializedName("favoriteCount")
    @Expose
    var favoriteCount: String = ""

    @SerializedName("menuNm")
    @Expose
    var menuNm: String = ""

    @SerializedName("orgFilePos")
    @Expose
    var orgFilePos: String = ""

    @SerializedName("pageViewTotal")
    @Expose
    var pageViewTotal: String = ""

    @SerializedName("ratingAvgScore")
    @Expose
    var ratingAvgScore: String = ""

    @SerializedName("prodNm")
    @Expose
    var prodNm: String = ""

    @SerializedName("tagList")
    @Expose
    var tagList: ArrayList<OnestoreBookDetailKeywords> = ArrayList()

    @SerializedName("commentCount")
    @Expose
    var commentCount: String = ""
}

class OnestoreBookDetailKeywords {
    @SerializedName("tagNm")
    @Expose
    var tagNm: String = ""
}

