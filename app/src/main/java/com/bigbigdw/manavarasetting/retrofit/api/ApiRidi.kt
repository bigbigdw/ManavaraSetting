package com.bigbigdw.moavara.Retrofit.Api

import com.bigbigdw.moavara.Retrofit.*
import retrofit2.Call
import retrofit2.http.*

interface ApiRidi {
    @Headers("Accept-Encoding: identity")
    @GET("_next/data/LCVxLoy9SrM5febDfes-R/category/bestsellers/{value}.json")
    fun getRidi(@Path("value") value: String, @QueryMap queryMap: MutableMap<String?, Any>): Call<String>
}