package com.bigbigdw.manavarasetting.retrofit.result

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class JoaraBestListResult {
    @SerializedName("status")
    @Expose
    val status: String = ""

    @SerializedName("books")
    @Expose
    val bookLists: List<JoaraBestListValue>? = null
}

//조아라 베스트
class JoaraBestListValue {
    @SerializedName("writer_name")
    @Expose
    var writerName: String = ""

    @SerializedName("category_ko_name")
    @Expose
    var category_ko_name: String = ""

    @SerializedName("subject")
    @Expose
    var subject: String = ""

    @SerializedName("book_img")
    @Expose
    var bookImg: String = ""

    @SerializedName("intro")
    @Expose
    var intro: String = ""

    @SerializedName("book_code")
    @Expose
    var bookCode: String = ""

    @SerializedName("cnt_chapter")
    @Expose
    var cntChapter: String = ""

    @SerializedName("cnt_favorite")
    @Expose
    var cntFavorite: String = ""

    @SerializedName("cnt_recom")
    @Expose
    var cntRecom: String = ""

    @SerializedName("cnt_total_comment")
    @Expose
    var cntTotalComment: String = ""


    @SerializedName("cnt_page_read")
    @Expose
    var cntPageRead: String = ""

    @SerializedName("chapter")
    @Expose
    var chapter : List<JoaraBestChapter>? = null

    @SerializedName("keyword")
    @Expose
    lateinit var keyword : ArrayList<String>
}

class JoaraBestChapter {
    @SerializedName("cnt_comment")
    @Expose
    val cnt_comment: String = ""

    @SerializedName("cnt_page_read")
    @Expose
    val cnt_page_read: String = ""

    @SerializedName("cnt_recom")
    @Expose
    val cnt_recom: String = ""

    @SerializedName("sortno")
    @Expose
    val sortno: String = ""
}

class JoaraBestDetailResult {
    @SerializedName("status")
    @Expose
    val status: String = ""

    @SerializedName("book")
    @Expose
    val book: JoaraBestListValue? = null
}

