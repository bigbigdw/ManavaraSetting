package com.bigbigdw.moavara.Retrofit.Api

import com.bigbigdw.manavarasetting.retrofit.result.BestToksodaResult
import com.bigbigdw.moavara.Retrofit.*
import retrofit2.Call
import retrofit2.http.*

interface ApiToksoda {
    @Headers("Accept-Encoding: identity")
    @GET("getMainChargeProductList")
    fun getBestList(@QueryMap queryMap: MutableMap<String?, Any>): Call<BestToksodaResult>

}