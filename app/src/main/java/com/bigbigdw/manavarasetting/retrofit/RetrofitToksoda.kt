package com.bigbigdw.moavara.Retrofit

import com.bigbigdw.manavarasetting.retrofit.Retrofit
import com.bigbigdw.manavarasetting.retrofit.result.BestToksodaResult

class RetrofitToksoda {
    private val apiToksoda = Retrofit.apiToksoda

    fun getBestList(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<BestToksodaResult>) {
        apiToksoda.getBestList(map).enqueue(baseCallback(dataListener))
    }

}