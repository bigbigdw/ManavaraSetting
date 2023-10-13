package com.bigbigdw.manavarasetting.retrofit

import com.bigbigdw.manavarasetting.retrofit.result.BestMoonpiaResult
import com.bigbigdw.moavara.Retrofit.RetrofitDataListener
import com.bigbigdw.moavara.Retrofit.baseCallback

class RetrofitMoonPia {
    private val apiMoonPia = Retrofit.apiMoonPia

    fun postMoonPiaBest(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<BestMoonpiaResult>) {
        apiMoonPia.postMoonPiaBest(map).enqueue(baseCallback(dataListener))
    }

    fun postMoonPiaBestPay(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<BestMoonpiaResult>) {
        apiMoonPia.postMoonPiaBestPay(map).enqueue(baseCallback(dataListener))
    }

}