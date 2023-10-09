package com.bigbigdw.moavara.Retrofit.Api

import com.bigbigdw.manavarasetting.retrofit.result.OneStoreBookResult
import retrofit2.Call
import retrofit2.http.*

interface ApiOneStory {

    @GET("api/display/product/RNK050700001")
    fun getBestOneStore(@QueryMap queryMap: MutableMap<String?, Any>): Call<OneStoreBookResult>

}