package com.bigbigdw.manavarasetting.retrofit.api

import com.bigbigdw.manavarasetting.retrofit.result.BestResultKakaoStageNovel
import retrofit2.Call
import retrofit2.http.*


interface ApiKakaoStage {

    @GET("v2/ranking/best")
    fun getBestKakaoStage(@QueryMap queryMap: MutableMap<String?, Any>): Call<List<BestResultKakaoStageNovel>>

}