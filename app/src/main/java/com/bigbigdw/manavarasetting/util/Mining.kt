package com.bigbigdw.manavarasetting.util

import android.util.Log
import com.bigbigdw.manavarasetting.util.DBDate.dateMMDD
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

object Mining {
    fun miningNaverSeriesAll(pageCount: Int, genre: String){

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

                    miningValue(NaverRef, (naverSeries.size * (pageCount - 1)) + i, "NAVER_SERIES", getNaverSeriesGenre(genre))
                }

            } catch (exception: Exception) {
                Log.d("EXCEPTION!!!!", "NAVER TODAY")
            }
        }.start()
    }


}