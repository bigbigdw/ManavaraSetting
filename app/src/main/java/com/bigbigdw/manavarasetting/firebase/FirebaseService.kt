package com.bigbigdw.manavarasetting.firebase

import com.bigbigdw.manavarasetting.firebase.DataFCMBody
import com.bigbigdw.manavarasetting.firebase.FWorkManagerResult
import retrofit2.Call
import retrofit2.http.*

interface FirebaseService {
    @Headers("Content-Type: application/json", "Authorization: key=AAAA2bUCIrA:APA91bFkYpkyMCvDxb58jMdPrO_rIZ8Po1u6B2xstSI0s2s7R1IG1G17OVm8_onR_IA02xeg2pdxkZntl1XClBqgfgCqNP772mo7bN6q5l0OMItjBIhqjBYHalKz4N_UxKXZef9pIYeS")
    @POST("/v1/projects/manavara/messages:send")
    fun postRetrofit(
        @Body body : DataFCMBody
    ): Call<FWorkManagerResult>
}

