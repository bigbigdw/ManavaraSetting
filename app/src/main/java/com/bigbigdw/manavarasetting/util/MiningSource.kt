package com.bigbigdw.manavarasetting.util

import android.content.Context
import android.util.Log
import com.bigbigdw.manavarasetting.main.model.ItemBookInfo
import com.bigbigdw.manavarasetting.util.DBDate.dateMMDD
import com.bigbigdw.moavara.Retrofit.JoaraBestListResult
import com.bigbigdw.moavara.Retrofit.RetrofitDataListener
import com.bigbigdw.moavara.Retrofit.RetrofitJoara
import com.bigbigdw.moavara.Retrofit.RetrofitRidi
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
            Log.d("EXCEPTION!!!!", "NAVER TODAY")
        }
    }

    fun miningNaverSeriesComic(
        platform: String,
        type: String,
        yesterDatItemMap: MutableMap<String, ItemBookInfo>,
        callBack: (JsonArray, JsonArray) -> Unit
    ){
        Thread{

            val itemBookInfoList = JsonArray()
            val itemBestInfoList = JsonArray()

            for(pageCount in 1..5) {
                val doc: Document =
                    Jsoup.connect("https://series.naver.com/comic/top100List.series?rankingTypeCode=DAILY&categoryCode=ALL&page=${pageCount + 1}")
                        .post()
                val naverSeries: Elements = doc.select(".comic_top_lst li")
                val ref: MutableMap<String?, Any> = HashMap()


                for (i in naverSeries.indices) {
                    val bookCode = naverSeries.select(".comic_cont a")[i].absUrl("href").replace("https://series.naver.com/comic/detail.series?productNo=", "")
                    val point = (naverSeries.size * 5) - ((naverSeries.size * (pageCount - 1)) + i)

                    val yesterDayItem = checkMiningTrophyValue(yesterDatItemMap[bookCode] ?: ItemBookInfo())

                    ref["writerName"] = naverSeries[i].select(".comic_cont .info .ellipsis .author").first()?.text() ?: ""
                    ref["subject"] = naverSeries.select(".comic_cont h3 a")[i].text()
                    ref["bookImg"] = naverSeries.select("a img")[i].absUrl("src")
                    ref["bookCode"] = bookCode
                    ref["cntRecom"] = naverSeries.select(".comic_cont .info .score_num")[i].text()
                    ref["cntChapter"] = naverSeries[i].select(".comic_cont .info .ellipsis")[1]?.text() ?: ""
                    ref["intro"] = naverSeries.select(".comic_cont .dsc")[i].text()
                    ref["number"] = ((naverSeries.size * (pageCount - 1)) + i)
                    ref["point"] = point

                    ref["total"] = yesterDayItem.point + point
                    ref["totalCount"] = yesterDayItem.totalCount + 1
                    ref["totalWeek"] = yesterDayItem.totalWeek + point
                    ref["totalWeekCount"] = yesterDayItem.totalWeekCount + 1
                    ref["totalMonth"] = yesterDayItem.totalMonth + point
                    ref["totalMonthCount"] = yesterDayItem.totalMonthCount + 1
                    ref["currentDiff"] =
                        if (yesterDatItemMap[bookCode]?.currentDiff != null) {
                            (yesterDatItemMap[bookCode]?.currentDiff ?: 0) - ((naverSeries.size * (pageCount - 1)) + i)
                        } else {
                            0
                        }

                    ref["date"] = dateMMDD()
                    ref["type"] = platform

                    miningValue(
                        ref = ref,
                        num = (naverSeries.size * (pageCount - 1)) + i,
                        platform = platform,
                        type = type
                    )

                    itemBookInfoList.add(convertItemBook(BestRef.setItemBookInfoRef(ref)))
                    itemBestInfoList.add(convertItemBest(BestRef.setItemBestInfoRef(ref)))
                }

                callBack.invoke(itemBookInfoList, itemBestInfoList)

            }

        }.start()
    }

    fun miningNaverSeriesNovel(
        platform: String,
        type: String,
        yesterDatItemMap: MutableMap<String, ItemBookInfo>,
        callBack: (JsonArray, JsonArray) -> Unit
    ){
        Thread{

            val itemBookInfoList = JsonArray()
            val itemBestInfoList = JsonArray()

            for(pageCount in 1..5) {
                val doc: Document = Jsoup.connect("https://series.naver.com/novel/top100List.series?rankingTypeCode=DAILY&categoryCode=ALL&page=${pageCount}").post()
                val naverSeries: Elements = doc.select(".comic_top_lst li")
                val ref: MutableMap<String?, Any> = HashMap()


                for (i in naverSeries.indices) {
                    val bookCode = naverSeries.select(".comic_cont a")[i].absUrl("href").replace("https://series.naver.com/novel/detail.series?productNo=", "")
                    val point = (naverSeries.size * 5) - ((naverSeries.size * (pageCount - 1)) + i)

                    val yesterDayItem = checkMiningTrophyValue(yesterDatItemMap[bookCode] ?: ItemBookInfo())

                    ref["writerName"] = naverSeries[i].select(".comic_cont .info .ellipsis .author").first()?.text() ?: ""
                    ref["subject"] = naverSeries.select(".comic_cont h3 a")[i].text()
                    ref["bookImg"] = naverSeries.select("a img")[i].absUrl("src")
                    ref["bookCode"] = bookCode
                    ref["cntRecom"] = naverSeries.select(".comic_cont .info .score_num")[i].text()
                    ref["cntChapter"] = naverSeries[i].select(".comic_cont .info .ellipsis")[1]?.text() ?: ""
                    ref["intro"] = naverSeries.select(".comic_cont .dsc")[i].text()
                    ref["number"] = ((naverSeries.size * (pageCount - 1)) + i)
                    ref["point"] = point

                    ref["total"] = yesterDayItem.point + point
                    ref["totalCount"] = yesterDayItem.totalCount + 1
                    ref["totalWeek"] = yesterDayItem.totalWeek + point
                    ref["totalWeekCount"] = yesterDayItem.totalWeekCount + 1
                    ref["totalMonth"] = yesterDayItem.totalMonth + point
                    ref["totalMonthCount"] = yesterDayItem.totalMonthCount + 1
                    ref["currentDiff"] =
                        if (yesterDatItemMap[bookCode]?.currentDiff != null) {
                            (yesterDatItemMap[bookCode]?.currentDiff ?: 0) - ((naverSeries.size * (pageCount - 1)) + i)
                        } else {
                            0
                        }

                    ref["date"] = dateMMDD()
                    ref["type"] = platform

                    miningValue(
                        ref = ref,
                        num = (naverSeries.size * (pageCount - 1)) + i,
                        platform = platform,
                        type = type
                    )

                    itemBookInfoList.add(convertItemBook(BestRef.setItemBookInfoRef(ref)))
                    itemBestInfoList.add(convertItemBest(BestRef.setItemBestInfoRef(ref)))
                }

                callBack.invoke(itemBookInfoList, itemBestInfoList)

            }

        }.start()
    }

    fun miningJoara(
        context: Context,
        mining: String,
        platform: String,
        type: String,
        yesterDatItemMap: MutableMap<String, ItemBookInfo>,
        callBack: (JsonArray, JsonArray) -> Unit
    ){
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
                            val number = i

                            val yesterDayItem = checkMiningTrophyValue(yesterDatItemMap[bookCode] ?: ItemBookInfo())

                            ref["keyword"] = books[i].keyword

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
                            ref["currentDiff"] =
                                if (yesterDatItemMap[bookCode]?.currentDiff != null) {
                                    (yesterDatItemMap[bookCode]?.currentDiff ?: 0) - number
                                } else {
                                    0
                                }

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

    fun miningNaver(
        platform: String,
        mining: String,
        type: String,
        yesterDatItemMap: MutableMap<String, ItemBookInfo>,
        callBack: (JsonArray, JsonArray) -> Unit
    ){
        Thread{

            val itemBookInfoList = JsonArray()
            val itemBestInfoList = JsonArray()

            val doc: Document = Jsoup.connect("https://novel.naver.com/${mining}/ranking?genre=101&periodType=DAILY").post()
            val naverSeries: Elements = doc.select(".ranking_wrap_left .ranking_list li")
            val ref: MutableMap<String?, Any> = HashMap()

            for (i in naverSeries.indices) {
                val bookCode = naverSeries.select("a")[i].absUrl("href").replace("https://novel.naver.com/${mining}/list?novelId=", "")
                val point = naverSeries.size - i

                val yesterDayItem = checkMiningTrophyValue(yesterDatItemMap[bookCode] ?: ItemBookInfo())

                ref["writerName"] = naverSeries.select(".info_group .author")[i].text()
                ref["subject"] = naverSeries.select(".title_group .title")[i].text()
                ref["bookImg"] = naverSeries.select("div img")[i].absUrl("src")
                ref["bookCode"] = bookCode
                ref["cntRecom"] = naverSeries.select(".score_area")[i].text().replace("별점", "")
                ref["cntPageRead"] = naverSeries[i].select(".info_group .count").next().first()!!.text()
                ref["cntFavorite"] = naverSeries.select(".meta_data_group .count")[i].text()
                ref["cntChapter"] = naverSeries[i].select(".info_group .count").first()!!.text()
                ref["number"] = i
                ref["point"] = point

                ref["total"] = yesterDayItem.point + point
                ref["totalCount"] = yesterDayItem.totalCount + 1
                ref["totalWeek"] = yesterDayItem.totalWeek + point
                ref["totalWeekCount"] = yesterDayItem.totalWeekCount + 1
                ref["totalMonth"] = yesterDayItem.totalMonth + point
                ref["totalMonthCount"] = yesterDayItem.totalMonthCount + 1
                ref["currentDiff"] =
                    if (yesterDatItemMap[bookCode]?.currentDiff != null) {
                        (yesterDatItemMap[bookCode]?.currentDiff ?: 0) - i
                    } else {
                        0
                    }

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

        }.start()
    }

    fun miningRidi(
        mining: String,
        platform: String,
        type: String,
        yesterDatItemMap: MutableMap<String, ItemBookInfo>,
        callBack: (JsonArray, JsonArray) -> Unit
    ) {
        try {
            val ref: MutableMap<String?, Any> = HashMap()

            val apiRidi = RetrofitRidi()
            val param: MutableMap<String?, Any> = HashMap()
            param["tab"] = "bestsellers"
            param["category"] = mining

            val itemBookInfoList = JsonArray()
            val itemBestInfoList = JsonArray()

            apiRidi.getRidiRomance(
                value = mining,
                map = param,
                object : RetrofitDataListener<String> {
                    override fun onSuccess(data: String) {

                        val baseJSONObject = JSONObject(data)

                        val result = baseJSONObject.optJSONObject("pageProps")
                            ?.optJSONObject("dehydratedState")?.optJSONArray("queries")
                            ?.get(3)

                        val productList = JSONObject(result.toString()).optJSONObject("state")
                            ?.optJSONArray("data")

                        if (productList != null) {
                            for (i in 0 until productList.length()) {

                                val jsonObject = productList.getJSONObject(i).optJSONObject("book")

                                if (jsonObject != null) {

                                    val bookCode = jsonObject.optString("bookId")
                                    val point = productList.length() - i
                                    val number = i

                                    val yesterDayItem = checkMiningTrophyValue(yesterDatItemMap[bookCode] ?: ItemBookInfo())

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
                                    ref["currentDiff"] =
                                        if (yesterDatItemMap[bookCode]?.currentDiff != null) {
                                            (yesterDatItemMap[bookCode]?.currentDiff ?: 0) - number
                                        } else {
                                            0
                                        }

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


                            }
                        }

                        callBack.invoke(itemBookInfoList, itemBestInfoList)
                    }
                })
        } catch (exception: Exception) {
            Log.d("RIDI EXCEPTION", "RIDI")
        }
    }

//    private fun getOneStoreBest(context : Context, genre: String) {
//        try {
//            val OneStoryRef: MutableMap<String?, Any> = HashMap()
//
//            val apiOneStory = RetrofitOnestore()
//            val param: MutableMap<String?, Any> = HashMap()
//            param["menuId"] = Genre.setOneStoreGenre(genre)
//
//            apiOneStory.getBestOneStore(
//                param,
//                object : RetrofitDataListener<OneStoreBookResult> {
//                    override fun onSuccess(data: OneStoreBookResult) {
//
//                        val productList = data.params?.productList
//
//                        if (productList != null) {
//                            for (i in productList.indices) {
//
//                                OneStoryRef["writerName"] = productList[i].artistNm
//                                OneStoryRef["subject"] = productList[i].prodNm
//                                OneStoryRef["bookImg"] =
//                                    "https://img.onestore.co.kr/thumbnails/img_sac/224_320_F10_95/" + productList[i].thumbnailImageUrl
//                                OneStoryRef["bookCode"] = productList[i].prodId
//                                OneStoryRef["info1"] = " "
//                                OneStoryRef["info2"] = " "
//                                OneStoryRef["info3"] = productList[i].totalCount
//                                OneStoryRef["info4"] = productList[i].avgScore
//                                OneStoryRef["info5"] = productList[i].commentCount
//                                OneStoryRef["info6"] = ""
//                                OneStoryRef["number"] = i
//                                OneStoryRef["date"] = DBDate.DateMMDD()
//                                OneStoryRef["type"] = "OneStore"
//
//                                miningValue(
//                                    OneStoryRef,
//                                    i,
//                                    "OneStore",
//                                    genre
//                                )
//
//                            }
//                        }
//
//                        val bestDaoToday = Room.databaseBuilder(
//                            context,
//                            DBBest::class.java,
//                            "Today_OneStore_${genre}"
//                        ).allowMainThreadQueries().build()
//
//                        val bestDaoWeek = Room.databaseBuilder(
//                            context,
//                            DBBest::class.java,
//                            "Week_OneStore_${genre}"
//                        ).allowMainThreadQueries().build()
//
//                        val bestDaoMonth = Room.databaseBuilder(
//                            context,
//                            DBBest::class.java,
//                            "Month_OneStore_${genre}"
//                        ).allowMainThreadQueries().build()
//
//                        bestDaoToday.bestDao().initAll()
//                        bestDaoWeek.bestDao().initAll()
//                        bestDaoMonth.bestDao().initAll()
//
//                        File(
//                            File("/storage/self/primary/MOAVARA"),
//                            "Today_OneStore_${genre}.json"
//                        ).delete()
//                        File(
//                            File("/storage/self/primary/MOAVARA"),
//                            "Week_OneStore_${genre}.json"
//                        ).delete()
//                        File(
//                            File("/storage/self/primary/MOAVARA"),
//                            "Month_OneStore_${genre}.json"
//                        ).delete()
//                    }
//                })
//        } catch (exception: SocketTimeoutException) {
//            Log.d("EXCEPTION", "ONESTORE")
//        }
//    }
//    private fun getKakaoStageBest(context : Context, genre: String) {
//        val KakaoRef: MutableMap<String?, Any> = HashMap()
//
//        val apiKakao = RetrofitKaKao()
//        val param: MutableMap<String?, Any> = HashMap()
//
//        param["adult"] = "false"
//        param["dateRange"] = "YESTERDAY"
//        param["genreIds"] = Genre.setKakaoStageGenre(genre)
//        param["recentHours"] = "72"
//
//        apiKakao.getBestKakaoStage(
//            param,
//            object : RetrofitDataListener<List<BestResultKakaoStageNovel>> {
//                override fun onSuccess(data: List<BestResultKakaoStageNovel>) {
//
//                    data.let {
//
//                        val list = it
//                        val books = ArrayList<BestItemData>()
//
//                        for (i in list.indices) {
//                            val novel = list[i].novel
//                            KakaoRef["genre"] = list[i].novel?.subGenre?.name ?: ""
//                            KakaoRef["keyword"] = ArrayList<String>()
//
//                            KakaoRef["writerName"] = novel!!.nickname!!.name
//                            KakaoRef["subject"] = novel.title
//                            KakaoRef["bookImg"] = novel.thumbnail!!.url
//                            KakaoRef["bookCode"] = novel.stageSeriesNumber
//                            KakaoRef["info1"] = novel.synopsis
//                            KakaoRef["info2"] = "총 ${novel.publishedEpisodeCount}화"
//                            KakaoRef["info3"] = novel.viewCount
//                            KakaoRef["info4"] = novel.visitorCount
//                            KakaoRef["info5"] = novel.episodeLikeCount
//                            KakaoRef["info6"] = novel.favoriteCount
//                            KakaoRef["number"] = i
//                            KakaoRef["date"] = DBDate.DateMMDD()
//                            KakaoRef["type"] = "Kakao_Stage"
//
//                            miningValue(KakaoRef, i, "Kakao_Stage", genre)
//
//                            miningDataValue(
//                                KakaoRef,
//                                "Kakao_Stage",
//                                genre
//                            )
//
//                            miningDataValue(
//                                KakaoRef,
//                                "Kakao_Stage",
//                                genre
//                            )
//
//                            books.add(BestRef.setBookListDataBest(KakaoRef))
//                        }
//
//                        val bestDaoToday = Room.databaseBuilder(
//                            context,
//                            DBBest::class.java,
//                            "Today_Kakao_Stage_${genre}"
//                        ).allowMainThreadQueries().build()
//
//                        val bestDaoWeek = Room.databaseBuilder(
//                            context,
//                            DBBest::class.java,
//                            "Week_Kakao_Stage_${genre}"
//                        ).allowMainThreadQueries().build()
//
//                        val bestDaoMonth = Room.databaseBuilder(
//                            context,
//                            DBBest::class.java,
//                            "Month_Kakao_Stage_${genre}"
//                        ).allowMainThreadQueries().build()
//
//                        bestDaoToday.bestDao().initAll()
//                        bestDaoWeek.bestDao().initAll()
//                        bestDaoMonth.bestDao().initAll()
//
//                    }
//
//                }
//            })
//    }

//    fun getKakaoBest(context : Context, genre: String) {
//        val apiKakao = RetrofitKaKao()
//        val param: MutableMap<String?, Any> = HashMap()
//        val KakaoRef: MutableMap<String?, Any> = HashMap()
//
//        Log.d("KAKAO 0", "KAKAO")
//
//        param["subcategory_uid"] = Genre.setKakaoPage(genre)
//
//        apiKakao.getBestKakao2(
//            param,
//            object : RetrofitDataListener<BestKakao2Result> {
//                override fun onSuccess(data: BestKakao2Result) {
//
//                    val list = data.pageProps?.initialState?.json?.pagewebLayout?.entities?.items
//                    val array = JSONArray()
//
//                    for (item in list!!.keySet()) {
//                        array.put(item)
//                    }
//
//                    for (i in 0 until array.length()) {
//
//                        val res = list.getAsJsonObject(array[i].toString())
//
//                        KakaoRef["genre"] = res.get("row2").asJsonArray[0].asString
//                        KakaoRef["keyword"] = ArrayList<String>()
//
//                        KakaoRef["writerName"] = res.get("row2").asJsonArray[2].asString
//                        KakaoRef["subject"] = res.get("row1").asString
//                        KakaoRef["bookImg"] = "https:${res.get("thumbnail").asString}"
//                        KakaoRef["bookCode"] = res.get("scheme").toString().replace("kakaopage://open/content?series_id=","")
//                        KakaoRef["info1"] = res.get("row2").asJsonArray[0].asString
//                        KakaoRef["info2"] = StrToInt(res.get("row2").asJsonArray[1].asString)
//                        KakaoRef["info3"] = ""
//                        KakaoRef["info4"] = ""
//                        KakaoRef["info5"] = ""
//                        KakaoRef["info6"] = ""
//                        KakaoRef["number"] = i
//                        KakaoRef["date"] = DBDate.DateMMDD()
//                        KakaoRef["type"] = "Kakao"
//
//                        miningValue(
//                            KakaoRef,
//                            i,
//                            "Kakao",
//                            genre
//                        )
//
//                        miningDataValue(
//                            KakaoRef,
//                            "Kakao",
//                            genre
//                        )
//                    }
//
//                    val bestDaoToday = Room.databaseBuilder(
//                        context,
//                        DBBest::class.java,
//                        "Today_Kakao_${genre}"
//                    ).allowMainThreadQueries().build()
//
//                    val bestDaoWeek = Room.databaseBuilder(
//                        context,
//                        DBBest::class.java,
//                        "Week_Kakao_${genre}"
//                    ).allowMainThreadQueries().build()
//
//                    val bestDaoMonth = Room.databaseBuilder(
//                        context,
//                        DBBest::class.java,
//                        "Month_Kakao_${genre}"
//                    ).allowMainThreadQueries().build()
//
//                    bestDaoToday.bestDao().initAll()
//                    bestDaoWeek.bestDao().initAll()
//                    bestDaoMonth.bestDao().initAll()
//
//                }
//            })
//    }

//    private fun getMoonpiaBest(context : Context) {
//        val MoonpiaRef: MutableMap<String?, Any> = HashMap()
//
//        val apiMoonPia = RetrofitMoonPia()
//        val param: MutableMap<String?, Any> = HashMap()
//
//        param["section"] = "today"
//        param["exclusive"] = ""
//        param["outAdult"] = "true"
//        param["offset"] = 25
//
//        apiMoonPia.postMoonPiaBest(
//            param,
//            object : RetrofitDataListener<BestMoonpiaResult> {
//                override fun onSuccess(data: BestMoonpiaResult) {
//
//                    data.api?.items.let {
//
//                        if (it != null) {
//                            for (i in it.indices) {
//                                MoonpiaRef["genre"] = it[i].nvGnMainTitle
//                                MoonpiaRef["keyword"] = ArrayList<String>()
//
//                                MoonpiaRef["writerName"] = it[i].author
//                                MoonpiaRef["subject"] = it[i].nvTitle
//                                MoonpiaRef["bookImg"] =
//                                    "https://cdn1.munpia.com${it[i].nvCover}"
//                                MoonpiaRef["bookCode"] = it[i].nvSrl
//                                MoonpiaRef["info1"] = it[i].nvStory
//                                MoonpiaRef["info2"] = it[i].nvGnMainTitle
//                                MoonpiaRef["info3"] = it[i].nsrData?.hit!!
//                                MoonpiaRef["info4"] = it[i].nsrData?.number!!
//                                MoonpiaRef["info5"] = it[i].nsrData?.prefer!!
//                                MoonpiaRef["info6"] = it[i].nsrData?.hour!!
//                                MoonpiaRef["number"] = i
//                                MoonpiaRef["date"] = DBDate.DateMMDD()
//                                MoonpiaRef["type"] = "Munpia"
//
//                                miningValue(
//                                    MoonpiaRef,
//                                    i,
//                                    "Munpia",
//                                    ""
//                                )
//
//                                miningDataValue(
//                                    MoonpiaRef,
//                                    "Munpia",
//                                    ""
//                                )
//                            }
//
//                            val bestDaoToday = Room.databaseBuilder(
//                                context,
//                                DBBest::class.java,
//                                "Today_Munpia"
//                            ).allowMainThreadQueries().build()
//
//                            val bestDaoWeek = Room.databaseBuilder(
//                                context,
//                                DBBest::class.java,
//                                "Week_Munpia"
//                            ).allowMainThreadQueries().build()
//
//                            val bestDaoMonth = Room.databaseBuilder(
//                                context,
//                                DBBest::class.java,
//                                "Month_Munpia"
//                            ).allowMainThreadQueries().build()
//
//                            bestDaoToday.bestDao().initAll()
//                            bestDaoWeek.bestDao().initAll()
//                            bestDaoMonth.bestDao().initAll()
//
//                            File(
//                                File("/storage/self/primary/MOAVARA"),
//                                "Today_Munpia.json"
//                            ).delete()
//                            File(File("/storage/self/primary/MOAVARA"), "Week_Munpia.json").delete()
//                            File(
//                                File("/storage/self/primary/MOAVARA"),
//                                "Month_Munpia.json"
//                            ).delete()
//                        }
//                    }
//
//                }
//            })
//    }

//    private fun getToksodaBest(context : Context, genre: String) {
//        val ToksodaRef: MutableMap<String?, Any> = HashMap()
//
//        val apiToksoda = RetrofitToksoda()
//        val param: MutableMap<String?, Any> = HashMap()
//
//        param["page"] = 1
//        param["lgctgrCd"] = Genre.setToksodaGenre(genre)
//        param["mdctgrCd"] = "all"
//        param["rookieYn"] = "N"
//        param["over19Yn"] = "N"
//        param["type"] = "NEW"
//        param["freePblserlYn"] = "00431"
//        param["_"] = "1657262989944"
//
//        apiToksoda.getBestList(
//            param,
//            object : RetrofitDataListener<BestToksodaResult> {
//                override fun onSuccess(data: BestToksodaResult) {
//
//                    data.resultList?.let { it ->
//                        for (i in it.indices) {
//                            ToksodaRef["genre"] = it[i].lgctgrNm
//                            ToksodaRef["keyword"] = ArrayList<String>()
//
//                            ToksodaRef["writerName"] = it[i].athrnm
//                            ToksodaRef["subject"] = it[i].wrknm
//                            ToksodaRef["bookImg"] = "https:${it[i].imgPath}"
//                            ToksodaRef["bookCode"] = it[i].brcd
//                            ToksodaRef["info1"] = it[i].lnIntro
//                            ToksodaRef["info2"] = "총 ${it[i].whlEpsdCnt}화"
//                            ToksodaRef["info3"] = it[i].inqrCnt
//                            ToksodaRef["info4"] = it[i].goodAllCnt
//                            ToksodaRef["info5"] = it[i].intrstCnt
//                            ToksodaRef["info6"] = ""
//                            ToksodaRef["number"] = i
//                            ToksodaRef["date"] = DBDate.DateMMDD()
//                            ToksodaRef["type"] = "Toksoda"
//
//                            miningValue(
//                                ToksodaRef,
//                                i,
//                                "Toksoda",
//                                genre
//                            )
//
//                            miningDataValue(
//                                ToksodaRef,
//                                "Toksoda",
//                                genre
//                            )
//                        }
//
//                        val bestDaoToday = Room.databaseBuilder(
//                            context,
//                            DBBest::class.java,
//                            "Today_Toksoda_${genre}"
//                        ).allowMainThreadQueries().build()
//
//                        val bestDaoWeek = Room.databaseBuilder(
//                            context,
//                            DBBest::class.java,
//                            "Week_Toksoda_${genre}"
//                        ).allowMainThreadQueries().build()
//
//                        val bestDaoMonth = Room.databaseBuilder(
//                            context,
//                            DBBest::class.java,
//                            "Month_Toksoda_${genre}"
//                        ).allowMainThreadQueries().build()
//
//                        bestDaoToday.bestDao().initAll()
//                        bestDaoWeek.bestDao().initAll()
//                        bestDaoMonth.bestDao().initAll()
//
//                        File(
//                            File("/storage/self/primary/MOAVARA"),
//                            "Today_Toksoda_${genre}.json"
//                        ).delete()
//                        File(
//                            File("/storage/self/primary/MOAVARA"),
//                            "Week_Toksoda_${genre}.json"
//                        ).delete()
//                        File(
//                            File("/storage/self/primary/MOAVARA"),
//                            "Month_Toksoda_${genre}.json"
//                        ).delete()
//                    }
//                }
//            })
//    }
}