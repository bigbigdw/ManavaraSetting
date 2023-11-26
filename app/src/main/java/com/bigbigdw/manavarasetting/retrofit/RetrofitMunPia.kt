package com.bigbigdw.manavarasetting.retrofit


import com.bigbigdw.manavarasetting.retrofit.result.BestMunpiaResult

class RetrofitMunPia {
    private val apiMoonPia = Retrofit.apiMoonPia

    fun postMoonPiaBest(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<BestMunpiaResult>) {
        apiMoonPia.postMoonPiaBest(map).enqueue(baseCallback(dataListener))
    }

    fun postMoonPiaBestPay(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<BestMunpiaResult>) {
        apiMoonPia.postMoonPiaBestPay(map).enqueue(baseCallback(dataListener))
    }

}