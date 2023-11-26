package com.bigbigdw.manavarasetting.retrofit.result

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BestResultKakaoStageNovel {
    @SerializedName("novel")
    @Expose
    val novel: KakaoBestStageResult? = null
}

class KakaoBestStageResult {
    @SerializedName("title")
    @Expose
    var title: String = ""

    @SerializedName("subGenre")
    @Expose
    lateinit var subGenre: KakaoBestStageSubGenre

    @SerializedName("stageSeriesNumber")
    @Expose
    var stageSeriesNumber: String = ""

    @SerializedName("nickname")
    @Expose
    var nickname: KakaoBestStageNickNameResult? = null

    @SerializedName("synopsis")
    @Expose
    var synopsis: String = ""

    @SerializedName("thumbnail")
    @Expose
    var thumbnail: KakaoBestStageThumbnailResult? = null

    @SerializedName("publishedEpisodeCount")
    @Expose
    var publishedEpisodeCount: String = ""

    @SerializedName("viewCount")
    @Expose
    var viewCount: String = ""

    @SerializedName("visitorCount")
    @Expose
    var visitorCount: String = ""

    @SerializedName("episodeLikeCount")
    @Expose
    var episodeLikeCount: String = ""

    @SerializedName("favoriteCount")
    @Expose
    var favoriteCount: String = ""
}

class KakaoBestStageThumbnailResult {
    @SerializedName("url")
    @Expose
    var url: String = ""
}

class KakaoBestStageNickNameResult {
    @SerializedName("name")
    @Expose
    var name: String = ""
}

class KakaoBestStageSubGenre {
    @SerializedName("name")
    @Expose
    var name: String = ""
}
