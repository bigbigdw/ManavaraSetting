package com.bigbigdw.moavara.Retrofit

import com.bigbigdw.manavarasetting.retrofit.result.BestMoonpiaResult

class RetrofitMoonPia {
    private val apiMoonPia = com.bigbigdw.moavara.Retrofit.Retrofit.apiMoonPia

    fun postMoonPiaBest(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<BestMoonpiaResult>) {
        apiMoonPia.postMoonPiaBest(map).enqueue(baseCallback(dataListener))
    }

}