package com.bigbigdw.manavarasetting.util

import android.util.Log
import com.bigbigdw.manavarasetting.util.DBDate.dateMMDD
import com.bigbigdw.manavarasetting.main.model.BestItemData
import com.bigbigdw.manavarasetting.util.DBDate.getCurrentWeekNumber
import com.bigbigdw.manavarasetting.util.DBDate.getDayOfWeekAsNumber
import com.bigbigdw.manavarasetting.util.DBDate.month
import com.bigbigdw.manavarasetting.util.DBDate.year
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.ByteArrayInputStream
import java.nio.charset.Charset

object Mining {
    fun miningNaverSeriesAll(pageCount: Int, genre: String){

        Thread {
            try {
                val doc: Document = Jsoup.connect("https://series.naver.com/comic/top100List.series?rankingTypeCode=DAILY&categoryCode=${genre}&page=${pageCount + 1}").post()
                val naverSeries: Elements = doc.select(".comic_top_lst li")
                val NaverRef: MutableMap<String?, Any> = HashMap()

                val books = ArrayList<BestItemData>()

                for (i in naverSeries.indices) {

                    NaverRef["writerName"] = naverSeries[i].select(".comic_cont .info .ellipsis .author").first()?.text() ?: ""
                    NaverRef["subject"] = naverSeries.select(".comic_cont h3 a")[i].text()
                    NaverRef["bookImg"] = naverSeries.select("a img")[i].absUrl("src")
                    NaverRef["bookCode"] = naverSeries.select(".comic_cont a")[i].absUrl("href").replace("https://series.naver.com/comic/detail.series?productNo=", "")
                    NaverRef["info1"] = naverSeries.select(".comic_cont .info .score_num")[i].text()
                    NaverRef["info2"] = naverSeries[i].select(".comic_cont .info .ellipsis")[1]?.text() ?: ""
                    NaverRef["info3"] = naverSeries.select(".comic_cont .dsc")[i].text()
                    NaverRef["number"] = (naverSeries.size * (pageCount - 1)) + i

                    NaverRef["date"] = dateMMDD()
                    NaverRef["type"] = "NAVER_SERIES"

                    books.add(BestRef.setBookListDataBest(NaverRef))

                    miningValue(NaverRef, (naverSeries.size * (pageCount - 1)) + i, "NAVER_SERIES", getNaverSeriesGenre(genre))
                }

                Log.d("!!!!!!MINING", "완료")

            } catch (exception: Exception) {
                Log.d("EXCEPTION!!!!", "NAVER TODAY")
            }
        }.start()
    }

    fun uploadJsonFile() {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val jsonFileRef = storageRef.child("your_folder/your_json_file.json") // 저장할 경로 및 파일 이름 지정

        // BestItemData 객체를 JSON 문자열로 변환
        val json = Gson().toJson(BestItemData()) // Gson 라이브러리를 사용하여 객체를 JSON으로 변환

        // JSON 문자열을 바이트 배열로 변환
        val jsonBytes = ByteArrayInputStream(json.toByteArray(Charsets.UTF_8))

        val uploadTask = jsonFileRef.putStream(jsonBytes)

        uploadTask.addOnSuccessListener {
            // 업로드 성공 시 처리
            Log.d("!!!!!!MINING", "JSON 파일 업로드 성공!")
        }.addOnFailureListener {
            // 업로드 실패 시 처리
            Log.d("!!!!!!MINING", "JSON 파일 업로드 실패: ${it.message}")
        }
    }

    fun uploadJsonArrayToStorageWeek(platform : String, genre: String) {
        val storage = Firebase.storage
        val storageRef = storage.reference

        val jsonFileRef = storageRef.child("${platform}/${genre}/WEEK/${year()}_${month()}_${getCurrentWeekNumber()}.json") // 읽어올 JSON 파일의 경로

        // JSON 파일을 다운로드
        jsonFileRef.getBytes(1024 * 1024) // 원하는 파일 크기로 변경 가능
            .addOnSuccessListener { bytes ->
                val jsonString = String(bytes, Charset.forName("UTF-8"))
                val weekArray = JsonParser().parse(jsonString).asJsonArray

                // JSON 파일을 읽은 후 처리
                makeWeekJson(platform = platform, genre = genre, jsonArray = weekArray)

                // 이제 json 문자열을 파싱하거나 원하는 처리를 수행할 수 있습니다.
            }

            .addOnFailureListener { exception ->

                val jsonArray = JsonArray()

                for(i in 0..6){
                    jsonArray.add("")
                }

                // 다운로드 실패 시 처리
                makeWeekJson(platform = platform, genre = genre, jsonArray = jsonArray)
            }
    }

    fun makeWeekJson(platform : String, genre: String, jsonArray : JsonArray)  {
        val storage = Firebase.storage
        val storageRef = storage.reference

        val jsonArrayRef = storageRef.child("${platform}/${genre}/WEEK/${year()}_${month()}_${getCurrentWeekNumber()}.json")

        val jsonFileRef = storageRef.child("${platform}/${genre}/DAY/${dateMMDD()}.json") // 읽어올 JSON 파일의 경로

        // JSON 파일을 다운로드
        val file = jsonFileRef.getBytes(1024 * 1024) // 원하는 파일 크기로 변경 가능

        file.addOnSuccessListener { bytes ->
            val jsonString = String(bytes, Charset.forName("UTF-8"))
            val json = Json { ignoreUnknownKeys = true }
            val itemList = json.decodeFromString<List<BestItemData>>(jsonString)
            val itemJsonArray = JsonArray()

            // JSON 배열 사용
            for (item in itemList) {
                itemJsonArray.add(convertBestItemData(item))
            }

            jsonArray.set(getDayOfWeekAsNumber() , itemJsonArray)

            // JSON 배열을 바이트 배열로 변환
            val jsonBytes = jsonArray.toString().toByteArray(Charsets.UTF_8)

            // Firebase Storage에 JSON 배열 업로드
            jsonArrayRef.putBytes(jsonBytes)
                .addOnSuccessListener {
                    // 업로드 성공 시 처리
                    Log.d("!!!!!!MINING", "JSON 배열 업로드 성공!")
                }
        }
    }

    fun uploadJsonArrayToStorageDay(platform : String, genre: String) {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val jsonArrayRef = storageRef.child("${platform}/${genre}/DAY/${dateMMDD()}.json") // 업로드 경로 및 파일 이름 설정

        BestRef.setBestRef(platform, genre).child("DATA").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){

                    val jsonArray = JsonArray()

                    for (postSnapshot in dataSnapshot.children) {
                        val group: BestItemData? = postSnapshot.getValue(BestItemData::class.java)
                        jsonArray.add(convertBestItemData(group ?: BestItemData()))
                    }

                    // JSON 배열을 바이트 배열로 변환
                    val jsonArrayByteArray = jsonArray.toString().toByteArray(Charsets.UTF_8)

                    // Firebase Storage에 JSON 배열 업로드
                    jsonArrayRef.putBytes(jsonArrayByteArray)
                        .addOnSuccessListener {
                            // 업로드 성공 시 처리
                            Log.d("!!!!!!MINING", "JSON 배열 업로드 성공!")
                        }
                } else {
                    Log.d("!!!!!!MINING", "JSON 배열 업로드 실패")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun miningValue(ref: MutableMap<String?, Any>, num: Int, platform: String, genre: String) {
        BestRef.setBookCode(platform, genre, ref["bookCode"] as String).setValue(BestRef.setBookListDataBest(ref))
        BestRef.setBookCode(platform, genre, ref["bookCode"] as String).child("number").child(DBDate.dateYYYY()).child(DBDate.dateMM()).child(DBDate.datedd()).setValue(BestRef.setBookListDataBestAnalyze(ref))
        BestRef.setBestData(platform, num, genre).setValue(BestRef.setBookListDataBest(ref))
    }
}