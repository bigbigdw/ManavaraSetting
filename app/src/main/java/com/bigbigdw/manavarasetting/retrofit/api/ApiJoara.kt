package com.bigbigdw.manavarasetting.retrofit.api

import com.bigbigdw.moavara.Retrofit.*
import retrofit2.Call
import retrofit2.http.*


interface ApiJoara {

    @GET("v1/best/book.joa")
    fun getJoaraBookBest(@QueryMap queryMap: MutableMap<String?, Any>): Call<JoaraBestListResult>

}