package com.bigbigdw.manavarasetting.main.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.bigbigdw.manavarasetting.Util.BestRef
import com.bigbigdw.manavarasetting.Util.DBDate
import com.bigbigdw.manavarasetting.main.event.EventMining
import com.bigbigdw.manavarasetting.main.event.StateMining
import com.bigbigdw.manavarasetting.main.model.BestItemData
import com.bigbigdw.manavarasetting.room.DBBest
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import javax.inject.Inject

class ViewModelMining @Inject constructor() : ViewModel() {

    private val events = Channel<EventMining>()

    val state: StateFlow<StateMining> = events.receiveAsFlow()
        .runningFold(StateMining(), ::reduceState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, StateMining())

    private val _sideEffects = Channel<String>()

    val sideEffects = _sideEffects.receiveAsFlow()

    private fun reduceState(current: StateMining, event: EventMining): StateMining {
        return when(event){
            EventMining.Loaded -> {
                current.copy(Loaded = true)
            }
        }
    }

    private fun miningValue(ref: MutableMap<String?, Any>, num: Int, platform: String, genre: String) {
        val test = BestRef.setBookCode(platform, genre, ref["bookCode"] as String)
        test.setValue(BestRef.setBookListDataBestAnalyze(ref))
        BestRef.setBestData(platform, num, genre).setValue(BestRef.setBookListDataBest(ref))
    }

    fun miningNaverSeriesAll(context : Context){

        Thread {
            try {
                val doc: Document = Jsoup.connect("https://series.naver.com/comic/top100List.series?rankingTypeCode=HOURLY&categoryCode=ALL").post()
                val naverSeries: Elements = doc.select(".comic_top_lst li")
                val NaverRef: MutableMap<String?, Any> = HashMap()

                val books = ArrayList<BestItemData>()

                for (i in naverSeries.indices) {

                    NaverRef["writerName"] = naverSeries[i].select(".comic_cont .info .ellipsis .author").first()?.text() ?: ""
                    NaverRef["subject"] = naverSeries.select(".comic_cont h3 a")[i].text()
                    NaverRef["bookImg"] = naverSeries.select("a img")[i].absUrl("src")
                    NaverRef["bookCode"] = naverSeries.select("a")[i].absUrl("href").replace("https://series.naver.com/comic/detail.series?productNo=", "")
                    NaverRef["info1"] = naverSeries.select(".comic_cont .info .score_num")[i].text()
                    NaverRef["info2"] = naverSeries[i].select(".comic_cont .info .ellipsis")[1]?.text() ?: ""
                    NaverRef["info3"] = naverSeries.select(".comic_cont .dsc")[i].text()
                    NaverRef["info4"] = ""
                    NaverRef["info5"] = ""
                    NaverRef["info6"] = ""
                    NaverRef["number"] = i

                    NaverRef["date"] = DBDate.dateMMDD()
                    NaverRef["type"] = "Naver_Series"

                    books.add(BestRef.setBookListDataBest(NaverRef))
                    miningValue(NaverRef, i, "Naver_Series", "ALL")
                }

                val bestDaoToday = Room.databaseBuilder(
                    context,
                    DBBest::class.java,
                    "Today_Naver_Series_ALL"
                ).allowMainThreadQueries().build()

                val bestDaoWeek = Room.databaseBuilder(
                    context,
                    DBBest::class.java,
                    "Week_Naver_Series_ALL"
                ).allowMainThreadQueries().build()

                val bestDaoMonth = Room.databaseBuilder(
                    context,
                    DBBest::class.java,
                    "Month_Naver_Series_ALL"
                ).allowMainThreadQueries().build()

                bestDaoToday.bestDao().initAll()
                bestDaoWeek.bestDao().initAll()
                bestDaoMonth.bestDao().initAll()


            } catch (exception: Exception) {
                Log.d("EXCEPTION!!!!", "NAVER TODAY")
            }
        }.start()
    }
    
}