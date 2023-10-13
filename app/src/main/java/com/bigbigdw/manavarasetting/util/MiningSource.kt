package com.bigbigdw.manavarasetting.util

import android.content.Context
import android.util.Log
import com.bigbigdw.manavarasetting.main.model.ItemBestInfo
import com.bigbigdw.manavarasetting.main.model.ItemBookInfo
import com.bigbigdw.manavarasetting.util.DBDate.dateMMDD
import com.bigbigdw.manavarasetting.retrofit.result.BestMoonpiaResult
import com.bigbigdw.moavara.Retrofit.BestResultKakaoStageNovel
import com.bigbigdw.manavarasetting.retrofit.result.BestToksodaResult
import com.bigbigdw.moavara.Retrofit.JoaraBestListResult
import com.bigbigdw.manavarasetting.retrofit.result.OneStoreBookResult
import com.bigbigdw.moavara.Retrofit.RetrofitDataListener
import com.bigbigdw.moavara.Retrofit.RetrofitJoara
import com.bigbigdw.moavara.Retrofit.RetrofitKaKao
import com.bigbigdw.manavarasetting.retrofit.RetrofitMoonPia
import com.bigbigdw.manavarasetting.retrofit.RetrofitOnestore
import com.bigbigdw.manavarasetting.retrofit.RetrofitRidi
import com.bigbigdw.moavara.Retrofit.RetrofitToksoda
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.JsonArray
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.nio.charset.Charset

object MiningSource {

    private fun getItemAPI(mContext: Context?): MutableMap<String?, Any> {

        val Param: MutableMap<String?, Any> = java.util.HashMap()

        mContext ?: return Param

        Param["api_key"] = "android_iK3303O982ab8e2391dp9498"
        Param["ver"] = "2.8.5"
        Param["device"] = "android"
        Param["deviceuid"] = "fp9v-165SxOz0bpxl7JxZW%3AAPA91bH7o-VcJ6LKjIDZ44LGUJiymDpM-ks7CJ3nnnQBr1zDTKsc4vaa1OPRIZ6jPEVrT5q4hJ6Q5mdhxB4GMIB9XQL5Lbd4JWlBauQQ8REZpc0y6fbjpE7V1mq1xTP4CUMTtQqvtABJ"
        Param["devicetoken"] = "NULL"

        return Param
    }

    fun mining(platform: String, type: String, context: Context){

        try {

            val storage = Firebase.storage
            val storageRef = storage.reference

            val yesterdayFileRef =
                storageRef.child("${platform}/${type}/DAY/${DBDate.dateYesterday()}.json")

            val yesterdayFile = yesterdayFileRef.getBytes(1024 * 1024)

            yesterdayFile.addOnSuccessListener { yesterdayBytes ->
                val yesterdayJson = Json { ignoreUnknownKeys = true }
                val yesterdayItemList = yesterdayJson.decodeFromString<List<ItemBookInfo>>(
                    String(
                        yesterdayBytes,
                        Charset.forName("UTF-8")
                    )
                )

                val yesterDayItemMap = mutableMapOf<String, ItemBookInfo>()

                for (item in yesterdayItemList) {
                    yesterDayItemMap[item.bookCode] = item
                }
                runBlocking {
                    doMining(
                        platform = platform,
                        type = type,
                        yesterDayItemMap = yesterDayItemMap,
                        context = context
                    )
                }

            }.addOnFailureListener {
                runBlocking {
                    doMining(
                        platform = platform,
                        type = type,
                        yesterDayItemMap = mutableMapOf(),
                        context = context
                    )
                }

            }
        } catch (exception: Exception) {
            Log.d("DO_MINING", "mining")
        }
    }

    fun miningNaverSeriesComic(
        platform: String,
        type: String,
        totalBookItem: MutableMap<Int, ItemBookInfo>,
        totalBestItem: MutableMap<Int, ItemBestInfo>,
        yesterDayItemMap: MutableMap<String, ItemBookInfo>,
        callBack: (MutableMap<Int, ItemBookInfo>, MutableMap<Int, ItemBestInfo>) -> Unit,
    ){
        Thread{
            try{

                for(pageCount in 1..5) {
                    val doc: Document =
                        Jsoup.connect("https://series.naver.com/comic/top100List.series?rankingTypeCode=DAILY&categoryCode=ALL&page=${pageCount + 1}")
                            .post()
                    val naverSeries: Elements = doc.select(".comic_top_lst li")
                    val ref: MutableMap<String?, Any> = HashMap()


                    for (i in naverSeries.indices) {
                        val bookCode = naverSeries.select(".comic_cont a")[i].absUrl("href").replace("https://series.naver.com/comic/detail.series?productNo=", "")
                        val point = (naverSeries.size * 5) - ((naverSeries.size * (pageCount - 1)) + i)
                        val number = ((naverSeries.size * (pageCount - 1)) + i)

                        val yesterDayItem = checkMiningTrophyValue(yesterDayItemMap[bookCode] ?: ItemBookInfo())

                        ref["genre"] = ""

                        ref["writerName"] = naverSeries[i].select(".comic_cont .info .ellipsis .author").first()?.text() ?: ""
                        ref["subject"] = naverSeries.select(".comic_cont h3 a")[i].text()
                        ref["bookImg"] = naverSeries.select("a img")[i].absUrl("src")
                        ref["bookCode"] = bookCode
                        ref["cntRecom"] = naverSeries.select(".comic_cont .info .score_num")[i].text()
                        ref["cntChapter"] = naverSeries[i].select(".comic_cont .info .ellipsis")[1]?.text() ?: ""
                        ref["intro"] = naverSeries.select(".comic_cont .dsc")[i].text()
                        ref["number"] = number
                        ref["point"] = point

                        ref["total"] = yesterDayItem.point + point
                        ref["totalCount"] = yesterDayItem.totalCount + 1
                        ref["totalWeek"] = yesterDayItem.totalWeek + point
                        ref["totalWeekCount"] = yesterDayItem.totalWeekCount + 1
                        ref["totalMonth"] = yesterDayItem.totalMonth + point
                        ref["totalMonthCount"] = yesterDayItem.totalMonthCount + 1
                        ref["currentDiff"] = (yesterDayItemMap[bookCode]?.number ?: 0) - number


                        ref["date"] = dateMMDD()
                        ref["type"] = platform

                        miningValue(
                            ref = ref,
                            num = (naverSeries.size * (pageCount - 1)) + i,
                            platform = platform,
                            type = type
                        )

                        totalBookItem[number] = BestRef.setItemBookInfoRef(ref)
                        totalBestItem[number] = BestRef.setItemBestInfoRef(ref)
                    }

                    callBack.invoke(totalBookItem, totalBestItem)

                }
            } catch (e : Exception){
                Log.d("DO_MINING", "miningNaverSeriesComic $e")
            }
        }.start()
    }

    fun miningNaverSeriesNovel(
        platform: String,
        type: String,
        totalBookItem: MutableMap<Int, ItemBookInfo>,
        totalBestItem: MutableMap<Int, ItemBestInfo>,
        yesterDayItemMap: MutableMap<String, ItemBookInfo>,
        callBack: (MutableMap<Int, ItemBookInfo>, MutableMap<Int, ItemBestInfo>) -> Unit,
    ){
        Thread{
            try {

                for(pageCount in 1..5) {
                    val doc: Document = Jsoup.connect("https://series.naver.com/novel/top100List.series?rankingTypeCode=DAILY&categoryCode=ALL&page=${pageCount}").post()
                    val naverSeries: Elements = doc.select(".comic_top_lst li")
                    val ref: MutableMap<String?, Any> = HashMap()


                    for (i in naverSeries.indices) {
                        val bookCode = naverSeries.select(".comic_cont a")[i].absUrl("href").replace("https://series.naver.com/novel/detail.series?productNo=", "")
                        val point = (naverSeries.size * 5) - ((naverSeries.size * (pageCount - 1)) + i)

                        val yesterDayItem = checkMiningTrophyValue(yesterDayItemMap[bookCode] ?: ItemBookInfo())
                        val number = ((naverSeries.size * (pageCount - 1)) + i)

                        ref["genre"] = ""

                        ref["writerName"] = naverSeries[i].select(".comic_cont .info .ellipsis .author").first()?.text() ?: ""
                        ref["subject"] = naverSeries.select(".comic_cont h3 a")[i].text()
                        ref["bookImg"] = naverSeries.select("a img")[i].absUrl("src")
                        ref["bookCode"] = bookCode
                        ref["cntRecom"] = naverSeries.select(".comic_cont .info .score_num")[i].text()
                        ref["cntChapter"] = naverSeries[i].select(".comic_cont .info .ellipsis")[1]?.text() ?: ""
                        ref["intro"] = naverSeries.select(".comic_cont .dsc")[i].text()
                        ref["number"] = number
                        ref["point"] = point

                        ref["total"] = yesterDayItem.point + point
                        ref["totalCount"] = yesterDayItem.totalCount + 1
                        ref["totalWeek"] = yesterDayItem.totalWeek + point
                        ref["totalWeekCount"] = yesterDayItem.totalWeekCount + 1
                        ref["totalMonth"] = yesterDayItem.totalMonth + point
                        ref["totalMonthCount"] = yesterDayItem.totalMonthCount + 1
                        ref["currentDiff"] = (yesterDayItemMap[bookCode]?.number ?: 0) - number

                        ref["date"] = dateMMDD()
                        ref["type"] = platform

                        miningValue(
                            ref = ref,
                            num = (naverSeries.size * (pageCount - 1)) + i,
                            platform = platform,
                            type = type
                        )

                        totalBookItem[number] = BestRef.setItemBookInfoRef(ref)
                        totalBestItem[number] = BestRef.setItemBestInfoRef(ref)
                    }

                    callBack.invoke(totalBookItem, totalBestItem)

                }
            }catch (e : Exception){
                Log.d("DO_MINING", "miningNaverSeriesNovel $e")
            }
        }.start()
    }

    fun miningJoara(
        context: Context,
        mining: String,
        platform: String,
        type: String,
        yesterDayItemMap: MutableMap<String, ItemBookInfo>,
        callBack: (JsonArray, JsonArray) -> Unit
    ){
        try {
            val ref: MutableMap<String?, Any> = HashMap()
            val apiJoara = RetrofitJoara()
            val param = getItemAPI(context)

            param["page"] = 1
            param["best"] = "today"
            param["store"] = mining
            param["category"] = "0"
            param["offset"] = "100"

            val itemBookInfoList = JsonArray()
            val itemBestInfoList = JsonArray()

            apiJoara.getJoaraBookBest(
                param,
                object : RetrofitDataListener<JoaraBestListResult> {
                    override fun onSuccess(data: JoaraBestListResult) {

                        val books = data.bookLists

                        if (books != null) {

                            for (i in books.indices) {

                                val bookCode = books[i].bookCode
                                val point = books.size - i

                                val yesterDayItem =
                                    checkMiningTrophyValue(yesterDayItemMap[bookCode] ?: ItemBookInfo())
                                val number = i

                                ref["genre"] = books[i].category_ko_name

                                ref["writerName"] = books[i].writerName
                                ref["subject"] = books[i].subject
                                ref["bookImg"] = books[i].bookImg.replace("http://", "https://")
                                ref["bookCode"] = bookCode
                                ref["intro"] = books[i].intro
                                ref["cntChapter"] = "총 ${books[i].cntChapter}화"
                                ref["cntPageRead"] = books[i].cntPageRead
                                ref["cntFavorite"] = books[i].cntFavorite
                                ref["cntRecom"] = books[i].cntRecom
                                ref["cntTotalComment"] = books[i].cntTotalComment
                                ref["number"] = number
                                ref["point"] = point

                                ref["total"] = yesterDayItem.point + point
                                ref["totalCount"] = yesterDayItem.totalCount + 1
                                ref["totalWeek"] = yesterDayItem.totalWeek + point
                                ref["totalWeekCount"] = yesterDayItem.totalWeekCount + 1
                                ref["totalMonth"] = yesterDayItem.totalMonth + point
                                ref["totalMonthCount"] = yesterDayItem.totalMonthCount + 1
                                ref["currentDiff"] = (yesterDayItemMap[bookCode]?.number ?: 0) - number

                                ref["date"] = dateMMDD()
                                ref["type"] = platform

                                miningValue(
                                    ref = ref,
                                    num = i,
                                    platform = platform,
                                    type = type
                                )

                                itemBookInfoList.add(convertItemBook(BestRef.setItemBookInfoRef(ref)))
                                itemBestInfoList.add(convertItemBest(BestRef.setItemBestInfoRef(ref)))
                            }

                            callBack.invoke(itemBookInfoList, itemBestInfoList)
                        }
                    }
                })
        }catch (e : Exception){
            Log.d("DO_MINING", "miningJoara $e")
        }
    }

    fun miningNaver(
        platform: String,
        mining: String,
        platformType: String,
        type: String,
        yesterDayItemMap: MutableMap<String, ItemBookInfo>,
        callBack: (JsonArray, JsonArray) -> Unit
    ){
        Thread{
            try{
                val itemBookInfoList = JsonArray()
                val itemBestInfoList = JsonArray()

                val doc: Document = Jsoup.connect("https://novel.naver.com/${mining}/ranking?genre=999&periodType=DAILY").post()
                val naverSeries: Elements = if(platformType == "FREE"){
                    doc.select(".ranking_wrap_left .ranking_list li")
                } else {
                    doc.select(".ranking_wrap_right .ranking_list li")
                }
                val ref: MutableMap<String?, Any> = HashMap()

                for (i in naverSeries.indices) {
                    val bookCode = naverSeries.select("a")[i].absUrl("href").replace("https://novel.naver.com/webnovel/list?novelId=", "")
                    val point = naverSeries.size - i
                    val number = i

                    val yesterDayItem = checkMiningTrophyValue(yesterDayItemMap[bookCode] ?: ItemBookInfo())

                    ref["writerName"] = naverSeries.select(".info_group .author")[i].text()
                    ref["subject"] = naverSeries.select(".title_group .title")[i].text()
                    ref["bookImg"] = naverSeries.select("div img")[i].absUrl("src")
                    ref["bookCode"] = bookCode
                    ref["cntRecom"] = naverSeries.select(".score_area")[i].text().replace("별점", "")
                    ref["cntPageRead"] = naverSeries[i].select(".info_group .count").next().first()!!.text()
                    ref["cntFavorite"] = naverSeries.select(".meta_data_group .count")[i].text()
                    ref["cntChapter"] = naverSeries[i].select(".info_group .count").first()!!.text()
                    ref["number"] = number
                    ref["point"] = point

                    ref["total"] = yesterDayItem.point + point
                    ref["totalCount"] = yesterDayItem.totalCount + 1
                    ref["totalWeek"] = yesterDayItem.totalWeek + point
                    ref["totalWeekCount"] = yesterDayItem.totalWeekCount + 1
                    ref["totalMonth"] = yesterDayItem.totalMonth + point
                    ref["totalMonthCount"] = yesterDayItem.totalMonthCount + 1
                    ref["currentDiff"] = (yesterDayItemMap[bookCode]?.number ?: 0) - number

                    ref["date"] = dateMMDD()
                    ref["type"] = platform

                    miningValue(
                        ref = ref,
                        num = i,
                        platform = platform,
                        type = type
                    )

                    itemBookInfoList.add(convertItemBook(BestRef.setItemBookInfoRef(ref)))
                    itemBestInfoList.add(convertItemBest(BestRef.setItemBestInfoRef(ref)))
                }

                callBack.invoke(itemBookInfoList, itemBestInfoList)
            }catch (e : Exception){
                Log.d("DO_MINING", "miningNaver $e")
            }

        }.start()
    }

    fun miningOnestory(
        platform: String,
        platformType: String,
        type: String,
        yesterDayItemMap: MutableMap<String, ItemBookInfo>,
        totalBookItem: MutableMap<Int, ItemBookInfo>,
        totalBestItem: MutableMap<Int, ItemBestInfo>,
        callBack: (MutableMap<Int, ItemBookInfo>, MutableMap<Int, ItemBestInfo>) -> Unit,
    ) {

        for(page in 1..4){
            try {
                val ref: MutableMap<String?, Any> = HashMap()

                val apiOneStory = RetrofitOnestore()
                val param: MutableMap<String?, Any> = HashMap()
                param["menuId"] = platformType
                param["startKey"] = when (page) {
                    1 -> {
                        ""
                    }
                    2 -> {
                        "61/0"
                    }
                    3 -> {
                        "125/0"
                    }
                    else -> {
                        "183/0"
                    }
                }

                apiOneStory.getBestOneStore(
                    param,
                    object : RetrofitDataListener<OneStoreBookResult> {
                        override fun onSuccess(data: OneStoreBookResult) {

                            val productList = data.params?.productList

                            if (productList != null) {
                                for (i in productList.indices) {

                                    val bookCode = productList[i].prodId
                                    val point = (productList.size * 4) - ((productList.size * (page - 1)) + i)
                                    val number = ((productList.size * (page - 1)) + i)

                                    val yesterDayItem = checkMiningTrophyValue(yesterDayItemMap[bookCode] ?: ItemBookInfo())

                                    ref["genre"] = ""

                                    ref["writerName"] = productList[i].artistNm
                                    ref["subject"] = productList[i].prodNm
                                    ref["bookImg"] =
                                        "https://img.onestore.co.kr/thumbnails/img_sac/224_320_F10_95/" + productList[i].thumbnailImageUrl
                                    ref["bookCode"] = bookCode
                                    ref["cntPageRead"] = productList[i].totalCount
                                    ref["cntRecom"] = productList[i].avgScore
                                    ref["cntTotalComment"] = productList[i].commentCount

                                    ref["number"] = number
                                    ref["point"] = point

                                    ref["total"] = yesterDayItem.point + point
                                    ref["totalCount"] = yesterDayItem.totalCount + 1
                                    ref["totalWeek"] = yesterDayItem.totalWeek + point
                                    ref["totalWeekCount"] = yesterDayItem.totalWeekCount + 1
                                    ref["totalMonth"] = yesterDayItem.totalMonth + point
                                    ref["totalMonthCount"] = yesterDayItem.totalMonthCount + 1
                                    ref["currentDiff"] = (yesterDayItemMap[bookCode]?.number ?: 0) - number

                                    ref["date"] = dateMMDD()
                                    ref["type"] = platform

                                    miningValue(
                                        ref = ref,
                                        num = number,
                                        platform = platform,
                                        type = type
                                    )

                                    totalBookItem[number] = BestRef.setItemBookInfoRef(ref)
                                    totalBestItem[number] = BestRef.setItemBestInfoRef(ref)

                                }
                            }

                            callBack.invoke(totalBookItem, totalBestItem)
                        }
                    })
            } catch (exception: Exception) {
                Log.d("EXCEPTION", "ONESTORE")
            }
        }
    }

    fun miningOnestoryPass(
        platform: String,
        platformType: String,
        type: String,
        yesterDayItemMap: MutableMap<String, ItemBookInfo>,
        totalBookItem: MutableMap<Int, ItemBookInfo>,
        totalBestItem: MutableMap<Int, ItemBestInfo>,
        callBack: (MutableMap<Int, ItemBookInfo>, MutableMap<Int, ItemBestInfo>) -> Unit,
    ) {

        for(page in 1..4){
            try {
                val ref: MutableMap<String?, Any> = HashMap()

                val apiOneStory = RetrofitOnestore()
                val param: MutableMap<String?, Any> = HashMap()
                param["menuId"] = platformType
                param["freepassGrpCd"] = "PD013333"

                param["startKey"] = when (page) {
                    1 -> {
                        ""
                    }
                    2 -> {
                        "367/0"
                    }
                    3 -> {
                        "1439/0"
                    }
                    else -> {
                        "1957/0"
                    }
                }

                apiOneStory.getBestOneStorePass(
                    param,
                    object : RetrofitDataListener<OneStoreBookResult> {
                        override fun onSuccess(data: OneStoreBookResult) {

                            val productList = data.params?.productList

                            if (productList != null) {
                                for (i in productList.indices) {

                                    val bookCode = productList[i].prodId
                                    val point = (productList.size * 4) - ((productList.size * (page - 1)) + i)
                                    val number = ((productList.size * (page - 1)) + i)

                                    val yesterDayItem = checkMiningTrophyValue(yesterDayItemMap[bookCode] ?: ItemBookInfo())

                                    ref["genre"] = ""

                                    ref["writerName"] = productList[i].artistNm
                                    ref["subject"] = productList[i].prodNm
                                    ref["bookImg"] =
                                        "https://img.onestore.co.kr/thumbnails/img_sac/224_320_F10_95/" + productList[i].thumbnailImageUrl
                                    ref["bookCode"] = bookCode
                                    ref["cntPageRead"] = productList[i].totalCount
                                    ref["cntRecom"] = productList[i].avgScore
                                    ref["cntTotalComment"] = productList[i].commentCount

                                    ref["number"] = number
                                    ref["point"] = point

                                    ref["total"] = yesterDayItem.point + point
                                    ref["totalCount"] = yesterDayItem.totalCount + 1
                                    ref["totalWeek"] = yesterDayItem.totalWeek + point
                                    ref["totalWeekCount"] = yesterDayItem.totalWeekCount + 1
                                    ref["totalMonth"] = yesterDayItem.totalMonth + point
                                    ref["totalMonthCount"] = yesterDayItem.totalMonthCount + 1
                                    ref["currentDiff"] = (yesterDayItemMap[bookCode]?.number ?: 0) - number

                                    ref["date"] = dateMMDD()
                                    ref["type"] = platform

                                    miningValue(
                                        ref = ref,
                                        num = number,
                                        platform = platform,
                                        type = type
                                    )

                                    totalBookItem[number] = BestRef.setItemBookInfoRef(ref)
                                    totalBestItem[number] = BestRef.setItemBestInfoRef(ref)

                                }
                            }

                            callBack.invoke(totalBookItem, totalBestItem)
                        }
                    })
            } catch (exception: Exception) {
                Log.d("EXCEPTION", "ONESTORE")
            }
        }
    }

    fun miningKakaoStage(
        platform: String,
        type: String,
        yesterDayItemMap: MutableMap<String, ItemBookInfo>,
        callBack: (JsonArray, JsonArray) -> Unit
    ) {
        val ref: MutableMap<String?, Any> = HashMap()

        val apiKakao = RetrofitKaKao()
        val param: MutableMap<String?, Any> = HashMap()

        param["adult"] = "false"
        param["dateRange"] = "YESTERDAY"
        param["genreIds"] = "7,1,2,3,4,5,6"
        param["recentHours"] = "72"

        val itemBookInfoList = JsonArray()
        val itemBestInfoList = JsonArray()

        apiKakao.getBestKakaoStage(
            param,
            object : RetrofitDataListener<List<BestResultKakaoStageNovel>> {
                override fun onSuccess(data: List<BestResultKakaoStageNovel>) {

                    data.let {

                        val list = it

                        for (i in list.indices) {

                            val novel = list[i].novel
                            val bookCode = novel?.stageSeriesNumber ?: ""
                            val point = list.size - i
                            val number = i

                            val yesterDayItem = checkMiningTrophyValue(yesterDayItemMap[bookCode] ?: ItemBookInfo())

                            ref["genre"] = list[i].novel?.subGenre?.name ?: ""
                            ref["keyword"] = ArrayList<String>()

                            ref["writerName"] = novel!!.nickname!!.name
                            ref["subject"] = novel.title
                            ref["bookImg"] = novel.thumbnail!!.url
                            ref["bookCode"] = bookCode
                            ref["intro"] =novel.synopsis
                            ref["cntChapter"] = "총 ${novel.publishedEpisodeCount}화"
                            ref["cntPageRead"] = novel.visitorCount
                            ref["cntFavorite"] = novel.favoriteCount
                            ref["cntRecom"] = novel.episodeLikeCount
                            ref["number"] = number
                            ref["point"] = point

                            ref["total"] = yesterDayItem.point + point
                            ref["totalCount"] = yesterDayItem.totalCount + 1
                            ref["totalWeek"] = yesterDayItem.totalWeek + point
                            ref["totalWeekCount"] = yesterDayItem.totalWeekCount + 1
                            ref["totalMonth"] = yesterDayItem.totalMonth + point
                            ref["totalMonthCount"] = yesterDayItem.totalMonthCount + 1
                            ref["currentDiff"] = (yesterDayItemMap[bookCode]?.number ?: 0) - number

                            ref["date"] = dateMMDD()
                            ref["type"] = platform

                            miningValue(
                                ref = ref,
                                num = number,
                                platform = platform,
                                type = type
                            )

                            itemBookInfoList.add(convertItemBook(BestRef.setItemBookInfoRef(ref)))
                            itemBestInfoList.add(convertItemBest(BestRef.setItemBestInfoRef(ref)))
                        }

                        callBack.invoke(itemBookInfoList, itemBestInfoList)

                    }
                }
            })
    }

    fun miningMunpia(
        platform: String,
        platformType: String,
        type: String,
        yesterDayItemMap: MutableMap<String, ItemBookInfo>,
        totalBookItem: MutableMap<Int, ItemBookInfo>,
        totalBestItem: MutableMap<Int, ItemBestInfo>,
        callBack: (MutableMap<Int, ItemBookInfo>, MutableMap<Int, ItemBestInfo>) -> Unit,
    ) {

        for(page in 1..4){
            val ref: MutableMap<String?, Any> = HashMap()

            val apiMoonPia = RetrofitMoonPia()
            val param: MutableMap<String?, Any> = HashMap()

            param["section"] = platformType
            param["exclusive"] = ""
            param["outAdult"] = "true"
            param["offset"] = when (page) {
                1 -> {
                    ""
                }
                2 -> {
                    "28"
                }
                3 -> {
                    "53"
                }
                else -> {
                    "78"
                }
            }

            apiMoonPia.postMoonPiaBest(
                param,
                object : RetrofitDataListener<BestMoonpiaResult> {
                    override fun onSuccess(data: BestMoonpiaResult) {

                        data.api?.items.let {

                            if (it != null) {
                                for (i in it.indices) {

                                    val bookCode = it[i].nvSrl
                                    val point = (it.size * 4) - ((it.size * (page - 1)) + i)
                                    val number = ((it.size * (page - 1)) + i)

                                    val yesterDayItem = checkMiningTrophyValue(yesterDayItemMap[bookCode] ?: ItemBookInfo())

                                    ref["genre"] = it[i].nvGnMainTitle
                                    ref["keyword"] = ArrayList<String>()

                                    ref["writerName"] = it[i].author
                                    ref["subject"] = it[i].nvTitle
                                    ref["bookImg"] = "https://cdn1.munpia.com${it[i].nvCover}"
                                    ref["bookCode"] = it[i].nvSrl
                                    ref["intro"] = it[i].nvStory
                                    ref["cntRecom"] = it[i].nsrData?.hit!!
                                    ref["cntPageRead"] = it[i].nsrData?.number!!
                                    ref["cntFavorite"] = it[i].nsrData?.prefer!!

                                    ref["number"] = number
                                    ref["point"] = point
                                    ref["total"] = yesterDayItem.point + point
                                    ref["totalCount"] = yesterDayItem.totalCount + 1
                                    ref["totalWeek"] = yesterDayItem.totalWeek + point
                                    ref["totalWeekCount"] = yesterDayItem.totalWeekCount + 1
                                    ref["totalMonth"] = yesterDayItem.totalMonth + point
                                    ref["totalMonthCount"] = yesterDayItem.totalMonthCount + 1
                                    ref["currentDiff"] = (yesterDayItemMap[bookCode]?.number ?: 0) - number

                                    ref["date"] = dateMMDD()
                                    ref["type"] = platform

                                    miningValue(
                                        ref = ref,
                                        num = number,
                                        platform = platform,
                                        type = type
                                    )

                                    totalBookItem[number] = BestRef.setItemBookInfoRef(ref)
                                    totalBestItem[number] = BestRef.setItemBestInfoRef(ref)
                                }

                                callBack.invoke(totalBookItem, totalBestItem)
                            }
                        }

                    }
                })
        }
    }

    fun miningToksoda(
        platform: String,
        platformType: String,
        type: String,
        yesterDayItemMap: MutableMap<String, ItemBookInfo>,
        totalBookItem: MutableMap<Int, ItemBookInfo>,
        totalBestItem: MutableMap<Int, ItemBestInfo>,
        callBack: (MutableMap<Int, ItemBookInfo>, MutableMap<Int, ItemBestInfo>) -> Unit,
    ) {

        for(page in 1..5){
            val ref: MutableMap<String?, Any> = HashMap()

            val apiToksoda = RetrofitToksoda()
            val param: MutableMap<String?, Any> = HashMap()

            param["page"] = page
            param["lgctgrCd"] = "all"
            param["over19Yn"] = "N"
            param["sumtalkYn"] = "N"
            param["rookieYn"] = "N"
            param["statsClsfCd"] = "00073"
            param["freePblserlYn"] = platformType
            param["_"] = "1696853385623"

            apiToksoda.getBestList(
                param,
                object : RetrofitDataListener<BestToksodaResult> {
                    override fun onSuccess(data: BestToksodaResult) {

                        data.resultList?.let { it ->
                            for (i in it.indices) {

                                val bookCode = it[i].brcd
                                val point = (it.size * 5) - ((it.size * (page - 1)) + i)
                                val number = ((it.size * (page - 1)) + i)

                                val yesterDayItem = checkMiningTrophyValue(yesterDayItemMap[bookCode] ?: ItemBookInfo())

                                ref["genre"] = it[i].lgctgrNm
                                ref["keyword"] = ArrayList<String>()
                                ref["writerName"] = it[i].athrnm
                                ref["subject"] = it[i].wrknm
                                ref["bookImg"] = "https:${it[i].imgPath}"
                                ref["bookCode"] = it[i].brcd
                                ref["intro"] = it[i].lnIntro
                                ref["cntRecom"] = it[i].intrstCnt
                                ref["cntPageRead"] = it[i].goodAllCnt
                                ref["cntFavorite"] = it[i].goodAllCnt
                                ref["cntChapter"] = "총 ${it[i].whlEpsdCnt}화"

                                ref["number"] = number
                                ref["point"] = point

                                ref["total"] = yesterDayItem.point + point
                                ref["totalCount"] = yesterDayItem.totalCount + 1
                                ref["totalWeek"] = yesterDayItem.totalWeek + point
                                ref["totalWeekCount"] = yesterDayItem.totalWeekCount + 1
                                ref["totalMonth"] = yesterDayItem.totalMonth + point
                                ref["totalMonthCount"] = yesterDayItem.totalMonthCount + 1
                                ref["currentDiff"] = (yesterDayItemMap[bookCode]?.number ?: 0) - number

                                ref["date"] = dateMMDD()
                                ref["type"] = platform

                                miningValue(
                                    ref = ref,
                                    num = number,
                                    platform = platform,
                                    type = type
                                )

                                totalBookItem[number] = BestRef.setItemBookInfoRef(ref)
                                totalBestItem[number] = BestRef.setItemBestInfoRef(ref)
                            }

                            callBack.invoke(totalBookItem, totalBestItem)
                        }
                    }
                })
        }
    }

    fun miningRidi(
        mining: String,
        platform: String,
        type: String,
        yesterDayItemMap: MutableMap<String, ItemBookInfo>,
        totalBookItem: MutableMap<Int, ItemBookInfo>,
        totalBestItem: MutableMap<Int, ItemBestInfo>,
        callBack: (MutableMap<Int, ItemBookInfo>, MutableMap<Int, ItemBestInfo>) -> Unit,
    ) {
        try {
            val ref: MutableMap<String?, Any> = HashMap()
            val apiRidi = RetrofitRidi()
            val param: MutableMap<String?, Any> = HashMap()

            param["tab"] = "books"
            param["category_id"] = mining
            param["platform"] = "web"
            param["offset"] = "0"
            param["limit"] = "100"
            param["order_by"] = "popular"

            apiRidi.getRidiRomance(
                value = mining,
                map = param,
                object : RetrofitDataListener<String> {
                    override fun onSuccess(data: String) {
                        val baseJSONObject = JSONObject(data)
                        val productList = baseJSONObject.optJSONObject("data")?.optJSONArray("items")

                        if (productList != null) {
                            for (i in 0 until productList.length()) {

                                val jsonObject = productList.getJSONObject(i).optJSONObject("book")

                                if (jsonObject != null) {
                                    val bookCode = jsonObject.optString("bookId")
                                    val point = productList.length() - i
                                    val number = i
                                    val yesterDayItem = checkMiningTrophyValue(yesterDayItemMap[bookCode] ?: ItemBookInfo())
                                    val ratings = jsonObject.optJSONArray("ratings")
                                    var ratingCount = 0F
                                    var ratePoints = 0F

                                    if (ratings != null) {
                                        for (j in 0 until ratings.length()) {
                                            val rate = ratings.getJSONObject(j)
                                            ratingCount += rate.optInt("count")
                                            ratePoints += rate.optInt("count") * rate.optInt("rating")
                                        }
                                    }

                                    ref["genre"] = JSONObject(jsonObject.optJSONArray("categories")?.get(0).toString()).optString("name") ?: ""

                                    ref["writerName"] = JSONObject(jsonObject.optJSONArray("authors")?.get(0).toString()).optString("name") ?: ""
                                    ref["subject"] = jsonObject.optString("title")
                                    ref["bookImg"] = jsonObject.optJSONObject("cover")?.optString("xxlarge") ?: ""
                                    ref["bookCode"] = bookCode
                                    ref["intro"] = jsonObject.optJSONObject("introduction")?.optString("description") ?: ""
                                    ref["cntChapter"] = "총 ${jsonObject.optJSONObject("serial")?.optString("total") ?: ""}화"
                                    ref["cntRecom"] = if (ratingCount == 0F) {
                                        "0"
                                    } else {
                                        String.format("%.1f", ratePoints / ratingCount)
                                    }
                                    ref["cntPageRead"] = ratingCount.toInt().toString()
                                    ref["number"] = number
                                    ref["point"] = point
                                    ref["total"] = yesterDayItem.point + point
                                    ref["totalCount"] = yesterDayItem.totalCount + 1
                                    ref["totalWeek"] = yesterDayItem.totalWeek + point
                                    ref["totalWeekCount"] = yesterDayItem.totalWeekCount + 1
                                    ref["totalMonth"] = yesterDayItem.totalMonth + point
                                    ref["totalMonthCount"] = yesterDayItem.totalMonthCount + 1
                                    ref["currentDiff"] = (yesterDayItemMap[bookCode]?.number ?: 0) - number
                                    ref["date"] = dateMMDD()
                                    ref["type"] = platform

                                    miningValue(
                                        ref = ref,
                                        num = number,
                                        platform = platform,
                                        type = type
                                    )

                                    totalBookItem[number] = BestRef.setItemBookInfoRef(ref)
                                    totalBestItem[number] = BestRef.setItemBestInfoRef(ref)
                                }
                            }
                        }
                        callBack.invoke(totalBookItem, totalBestItem)
                    }
                })
        } catch (exception: Exception) {
            Log.d("DO_MINING", "RIDI")
        }
    }
}