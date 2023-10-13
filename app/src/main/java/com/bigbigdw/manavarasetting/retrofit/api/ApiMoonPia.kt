package com.bigbigdw.moavara.Retrofit.Api

import com.bigbigdw.manavarasetting.retrofit.result.BestMoonpiaResult
import retrofit2.Call
import retrofit2.http.*

interface ApiMoonPia {

    @FormUrlEncoded
    @POST("/module/api/apiCode/d7dbfddf567dca21794d120f48678360/service/novel/scope/bests/MUNP/brfcr4nqlb2fhj6irrg6uohu75/HTTP_FORCED_AJAX/true")
    fun postMoonPiaBest(@FieldMap queryMap: MutableMap<String?, Any>): Call<BestMoonpiaResult>

    @FormUrlEncoded
    @POST("/module/api/apiCode/d7dbfddf567dca21794d120f48678360/service/novel/scope/bests/MUNP/8p28c7n7oinp83e5aaikrn2cb6/HTTP_FORCED_AJAX/true")
    fun postMoonPiaBestPay(@FieldMap queryMap: MutableMap<String?, Any>): Call<BestMoonpiaResult>

}