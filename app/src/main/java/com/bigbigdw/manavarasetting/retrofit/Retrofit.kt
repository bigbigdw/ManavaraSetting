package com.bigbigdw.manavarasetting.retrofit

import com.bigbigdw.manavarasetting.retrofit.api.ApiJoara
import com.bigbigdw.manavarasetting.retrofit.api.ApiKakaoStage
import com.bigbigdw.manavarasetting.retrofit.api.ApiMoonPia
import com.bigbigdw.manavarasetting.retrofit.api.ApiOneStory
import com.bigbigdw.manavarasetting.retrofit.api.ApiRidi
import com.bigbigdw.manavarasetting.retrofit.api.ApiToksoda
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object Retrofit {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.joara.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitKakaoStage = Retrofit.Builder()
        .baseUrl("https://api-pagestage.kakao.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitOneStory = Retrofit.Builder()
        .baseUrl("https://onestory.co.kr")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitMoonPia = Retrofit.Builder()
        .baseUrl("https://www.munpia.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitToksoda = Retrofit.Builder()
        .baseUrl("https://www.tocsoda.co.kr")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitRidi = Retrofit.Builder()
        .baseUrl("https://api.ridibooks.com")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()


    val apiJoara: ApiJoara = retrofit.create(ApiJoara::class.java)
    val apiKakaoStage: ApiKakaoStage = retrofitKakaoStage.create(ApiKakaoStage::class.java)
    val apiOneStory: ApiOneStory = retrofitOneStory.create(ApiOneStory::class.java)
    val apiRidi: ApiRidi = retrofitRidi.create(ApiRidi::class.java)
    val apiMoonPia: ApiMoonPia = retrofitMoonPia.create(ApiMoonPia::class.java)
    val apiToksoda: ApiToksoda = retrofitToksoda.create(ApiToksoda::class.java)
}