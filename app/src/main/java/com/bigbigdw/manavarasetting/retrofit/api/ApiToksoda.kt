package com.bigbigdw.manavarasetting.retrofit.api

import com.bigbigdw.manavarasetting.retrofit.result.BestToksodaDetailResult
import com.bigbigdw.manavarasetting.retrofit.result.BestToksodaResult
import retrofit2.Call
import retrofit2.http.*

interface ApiToksoda {
    @Headers("Accept-Encoding: identity")
    @GET("getTotalBestList")
    fun getBestList(@QueryMap queryMap: MutableMap<String?, Any>): Call<BestToksodaResult>

    @GET("product/selectProductDetail")
    fun getBestDetail(@QueryMap queryMap: MutableMap<String?, Any>): Call<BestToksodaDetailResult>
}