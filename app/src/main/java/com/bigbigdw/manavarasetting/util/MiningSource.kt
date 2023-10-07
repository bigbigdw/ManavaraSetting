package com.bigbigdw.manavarasetting.util

import android.content.Context
import android.util.Log
import com.bigbigdw.manavarasetting.main.model.ItemBookInfo
import com.bigbigdw.manavarasetting.util.DBDate.dateMMDD
import com.bigbigdw.moavara.Retrofit.JoaraBestListResult
import com.bigbigdw.moavara.Retrofit.RetrofitDataListener
import com.bigbigdw.moavara.Retrofit.RetrofitJoara
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.JsonArray
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.nio.charset.Charset

object MiningSource {

    fun getItemAPI(mContext: Context?): MutableMap<String?, Any> {

        val Param: MutableMap<String?, Any> = java.util.HashMap()

        mContext ?: return Param

        Param["api_key"] = "android_iK3303O982ab8e2391dp9498"
        Param["ver"] = "2.8.5"
        Param["device"] = "android"
        Param["deviceuid"] = "fp9v-165SxOz0bpxl7JxZW%3AAPA91bH7o-VcJ6LKjIDZ44LGUJiymDpM-ks7CJ3nnnQBr1zDTKsc4vaa1OPRIZ6jPEVrT5q4hJ6Q5mdhxB4GMIB9XQL5Lbd4JWlBauQQ8REZpc0y6fbjpE7V1mq1xTP4CUMTtQqvtABJ"
        Param["devicetoken"] = "NULL"

        return Param
    }
    fun miningNaverSeriesComic(pageCount: Int, genre: String){
        Thread {
            try {
                val doc: Document = Jsoup.connect("https://series.naver.com/comic/top100List.series?rankingTypeCode=DAILY&categoryCode=${genre}&page=${pageCount}").post()
                val naverSeries: Elements = doc.select(".comic_top_lst li")
                val NaverRef: MutableMap<String?, Any> = HashMap()

                for (i in naverSeries.indices) {

                    NaverRef["writerName"] = naverSeries[i].select(".comic_cont .info .ellipsis .author").first()?.text() ?: ""
                    NaverRef["subject"] = naverSeries.select(".comic_cont h3 a")[i].text()
                    NaverRef["bookImg"] = naverSeries.select("a img")[i].absUrl("src")
                    NaverRef["bookCode"] = naverSeries.select(".comic_cont a")[i].absUrl("href").replace("https://series.naver.com/comic/detail.series?productNo=", "")
                    NaverRef["info1"] = naverSeries.select(".comic_cont .info .score_num")[i].text()
                    NaverRef["info2"] = naverSeries[i].select(".comic_cont .info .ellipsis")[1]?.text() ?: ""
                    NaverRef["intro"] = naverSeries.select(".comic_cont .dsc")[i].text()
                    NaverRef["current"] = (naverSeries.size * (pageCount - 1)) + i

                    NaverRef["date"] = dateMMDD()
                    NaverRef["type"] = "NAVER_SERIES"

                    miningValue(
                        ref = NaverRef,
                        num = (naverSeries.size * (pageCount - 1)) + i,
                        platform = "NAVER_SERIES",
                        genre = getNaverSeriesGenre(genre),
                        type = "COMIC"
                    )
                }

                Log.d("MINING_TEST", "miningNaverSeriesComic")

            } catch (exception: Exception) {
                Log.d("EXCEPTION!!!!", "NAVER TODAY")
            }
        }.start()

    }

    fun miningNaverSeriesNovel(pageCount: Int, genre: String){

        Thread {
            try {
                val doc: Document = Jsoup.connect("https://series.naver.com/novel/top100List.series?rankingTypeCode=DAILY&categoryCode=${genre}&page=${pageCount}").post()
                val naverSeries: Elements = doc.select(".comic_top_lst li")
                val NaverRef: MutableMap<String?, Any> = HashMap()

                for (i in naverSeries.indices) {

                    NaverRef["writerName"] = naverSeries[i].select(".comic_cont .info .ellipsis .author").first()?.text() ?: ""
                    NaverRef["subject"] = naverSeries.select(".comic_cont h3 a")[i].text()
                    NaverRef["bookImg"] = naverSeries.select("a img")[i].absUrl("src")
                    NaverRef["bookCode"] = naverSeries.select(".comic_cont a")[i].absUrl("href").replace("https://series.naver.com/novel/detail.series?productNo=", "")
                    NaverRef["info1"] = naverSeries.select(".comic_cont .info .score_num")[i].text()
                    NaverRef["info2"] = naverSeries[i].select(".comic_cont .info .ellipsis")[1]?.text() ?: ""
                    NaverRef["intro"] = naverSeries.select(".comic_cont .dsc")[i].text()
                    NaverRef["current"] = (naverSeries.size * (pageCount - 1)) + i

                    NaverRef["date"] = dateMMDD()
                    NaverRef["type"] = "NAVER_SERIES"

                    miningValue(
                        ref = NaverRef,
                        num = (naverSeries.size * (pageCount - 1)) + i,
                        platform = "NAVER_SERIES",
                        genre = getNaverSeriesGenre(genre),
                        type = "NOVEL"
                    )
                }

            } catch (exception: Exception) {
                Log.d("EXCEPTION!!!!", "NAVER TODAY")
            }
        }.start()
    }

    fun mining(genre: String, platform: String, type: String, genreDir: String, context: Context){

        try {

            val storage = Firebase.storage
            val storageRef = storage.reference

            val yesterdayFileRef =
                storageRef.child("${platform}/${type}/${genreDir}/DAY/${DBDate.dateYesterday()}.json")

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
                        genre = genre,
                        platform = platform,
                        type = type,
                        yesterDayItemMap = yesterDayItemMap,
                        context = context
                    )
                }

            }.addOnFailureListener {
                runBlocking {
                    doMining(
                        genre = genre,
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
        genre: String,
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
                    Jsoup.connect("https://series.naver.com/comic/top100List.series?rankingTypeCode=DAILY&categoryCode=${genre}&page=${pageCount + 1}")
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
                            ((naverSeries.size * (pageCount - 1)) + i)
                        }

                    ref["date"] = dateMMDD()
                    ref["type"] = platform

                    miningValue(
                        ref = ref,
                        num = (naverSeries.size * (pageCount - 1)) + i,
                        platform = platform,
                        genre = getNaverSeriesGenre(genre),
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
        genre: String,
        platform: String,
        type: String,
        yesterDatItemMap: MutableMap<String, ItemBookInfo>,
        callBack: (JsonArray, JsonArray) -> Unit
    ){
        Thread{

            val itemBookInfoList = JsonArray()
            val itemBestInfoList = JsonArray()

            for(pageCount in 1..5) {
                val doc: Document = Jsoup.connect("https://series.naver.com/novel/top100List.series?rankingTypeCode=DAILY&categoryCode=${genre}&page=${pageCount}").post()
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
                            ((naverSeries.size * (pageCount - 1)) + i)
                        }

                    ref["date"] = dateMMDD()
                    ref["type"] = platform

                    miningValue(
                        ref = ref,
                        num = (naverSeries.size * (pageCount - 1)) + i,
                        platform = platform,
                        genre = getNaverSeriesGenre(genre),
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
        genre: String,
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
        param["category"] = getJoaraGenre(genre)
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
                                    number
                                }

                            ref["date"] = dateMMDD()
                            ref["type"] = platform

                            miningValue(
                                ref = ref,
                                num = number,
                                platform = platform,
                                genre = getJoaraGenre(genre),
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
}