package com.bigbigdw.manavarasetting.retrofit.api

import com.bigbigdw.manavarasetting.retrofit.result.OneStoreBookResult
import retrofit2.Call
import retrofit2.http.*

interface ApiOneStory {

    @GET("api/display/product/RNK050700001")
    fun getBestOneStore(@QueryMap queryMap: MutableMap<String?, Any>): Call<OneStoreBookResult>

    @GET("api/display/product/RNK050800001")
    fun getBestOneStorePass(@QueryMap queryMap: MutableMap<String?, Any>): Call<OneStoreBookResult>
}