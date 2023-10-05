package com.bigbigdw.manavarasetting.util

import android.util.Log
import com.bigbigdw.manavarasetting.main.model.ItemBookInfo
import com.bigbigdw.manavarasetting.util.DBDate.dateMMDD
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.JsonArray
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.nio.charset.Charset

object MiningSource {
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
                    NaverRef["info3"] = naverSeries.select(".comic_cont .dsc")[i].text()
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
                    NaverRef["info3"] = naverSeries.select(".comic_cont .dsc")[i].text()
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

    fun mining(genre: String, platform: String, type: String){

        try {

            val storage = Firebase.storage
            val storageRef = storage.reference

            val yesterdayFileRef =
                storageRef.child("${platform}/${type}/${genre}/DAY/${DBDate.dateYesterday()}.json")

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

                doMining(
                    genre = genre,
                    platform = platform,
                    type = type,
                    yesterDayItemMap = yesterDayItemMap
                )

            }.addOnFailureListener {

                doMining(
                    genre = genre,
                    platform = platform,
                    type = type,
                    yesterDayItemMap = mutableMapOf()
                )

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
                val NaverRef: MutableMap<String?, Any> = HashMap()


                for (i in naverSeries.indices) {
                    val bookCode = naverSeries.select(".comic_cont a")[i].absUrl("href").replace("https://series.naver.com/comic/detail.series?productNo=", "")
                    val current = (naverSeries.size * (pageCount - 1)) + i

                    val yesterDayItem = checkMiningTrophyValue(yesterDatItemMap[bookCode] ?: ItemBookInfo())

                    NaverRef["writerName"] = naverSeries[i].select(".comic_cont .info .ellipsis .author").first()?.text() ?: ""
                    NaverRef["subject"] = naverSeries.select(".comic_cont h3 a")[i].text()
                    NaverRef["bookImg"] = naverSeries.select("a img")[i].absUrl("src")
                    NaverRef["bookCode"] = bookCode
                    NaverRef["info1"] = naverSeries.select(".comic_cont .info .score_num")[i].text()
                    NaverRef["info2"] = naverSeries[i].select(".comic_cont .info .ellipsis")[1]?.text() ?: ""
                    NaverRef["info3"] = naverSeries.select(".comic_cont .dsc")[i].text()
                    NaverRef["current"] = current

                    NaverRef["total"] = yesterDayItem.current + current
                    NaverRef["totalCount"] = yesterDayItem.totalCount + 1
                    NaverRef["totalWeek"] = yesterDayItem.totalWeek + current
                    NaverRef["totalWeekCount"] = yesterDayItem.totalWeekCount + 1
                    NaverRef["totalMonth"] = yesterDayItem.totalMonth + current
                    NaverRef["totalMonthCount"] = yesterDayItem.totalMonthCount + 1
                    NaverRef["currentDiff"] =
                        if (yesterDatItemMap[bookCode]?.currentDiff != null) {
                            (yesterDatItemMap[bookCode]?.currentDiff ?: 0) - current
                        } else {
                            current
                        }

                    NaverRef["date"] = dateMMDD()
                    NaverRef["type"] = platform

                    miningValue(
                        ref = NaverRef,
                        num = (naverSeries.size * (pageCount - 1)) + i,
                        platform = platform,
                        genre = getNaverSeriesGenre(genre),
                        type = type
                    )

                    itemBookInfoList.add(convertItemBook(BestRef.setItemBookInfoRef(NaverRef)))
                    itemBestInfoList.add(convertItemBest(BestRef.setItemBestInfoRef(NaverRef)))
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
                val NaverRef: MutableMap<String?, Any> = HashMap()


                for (i in naverSeries.indices) {
                    val bookCode = naverSeries.select(".comic_cont a")[i].absUrl("href").replace("https://series.naver.com/novel/detail.series?productNo=", "")
                    val current = (naverSeries.size * (pageCount - 1)) + i

                    val yesterDayItem = checkMiningTrophyValue(yesterDatItemMap[bookCode] ?: ItemBookInfo())

                    NaverRef["writerName"] = naverSeries[i].select(".comic_cont .info .ellipsis .author").first()?.text() ?: ""
                    NaverRef["subject"] = naverSeries.select(".comic_cont h3 a")[i].text()
                    NaverRef["bookImg"] = naverSeries.select("a img")[i].absUrl("src")
                    NaverRef["bookCode"] = bookCode
                    NaverRef["info1"] = naverSeries.select(".comic_cont .info .score_num")[i].text()
                    NaverRef["info2"] = naverSeries[i].select(".comic_cont .info .ellipsis")[1]?.text() ?: ""
                    NaverRef["info3"] = naverSeries.select(".comic_cont .dsc")[i].text()
                    NaverRef["current"] = current

                    NaverRef["total"] = yesterDayItem.current + current
                    NaverRef["totalCount"] = yesterDayItem.totalCount + 1
                    NaverRef["totalWeek"] = yesterDayItem.totalWeek + current
                    NaverRef["totalWeekCount"] = yesterDayItem.totalWeekCount + 1
                    NaverRef["totalMonth"] = yesterDayItem.totalMonth + current
                    NaverRef["totalMonthCount"] = yesterDayItem.totalMonthCount + 1
                    NaverRef["currentDiff"] =
                        if (yesterDatItemMap[bookCode]?.currentDiff != null) {
                            (yesterDatItemMap[bookCode]?.currentDiff ?: 0) - current
                        } else {
                            current
                        }

                    NaverRef["date"] = dateMMDD()
                    NaverRef["type"] = platform

                    miningValue(
                        ref = NaverRef,
                        num = (naverSeries.size * (pageCount - 1)) + i,
                        platform = platform,
                        genre = getNaverSeriesGenre(genre),
                        type = type
                    )

                    itemBookInfoList.add(convertItemBook(BestRef.setItemBookInfoRef(NaverRef)))
                    itemBestInfoList.add(convertItemBest(BestRef.setItemBestInfoRef(NaverRef)))
                }

                callBack.invoke(itemBookInfoList, itemBestInfoList)

            }

        }.start()
    }
}