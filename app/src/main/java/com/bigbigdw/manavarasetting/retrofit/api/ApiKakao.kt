package com.bigbigdw.moavara.Retrofit.Api

import com.bigbigdw.moavara.Retrofit.*
import retrofit2.Call
import retrofit2.http.*


interface ApiKakaoStage {

    @GET("v2/ranking/best")
    fun getBestKakaoStage(@QueryMap queryMap: MutableMap<String?, Any>): Call<List<BestResultKakaoStageNovel>>

}