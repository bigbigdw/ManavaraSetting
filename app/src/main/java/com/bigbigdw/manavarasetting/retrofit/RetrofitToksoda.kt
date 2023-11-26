package com.bigbigdw.manavarasetting.retrofit

import com.bigbigdw.manavarasetting.retrofit.Retrofit
import com.bigbigdw.manavarasetting.retrofit.RetrofitDataListener
import com.bigbigdw.manavarasetting.retrofit.baseCallback
import com.bigbigdw.manavarasetting.retrofit.result.BestToksodaDetailResult
import com.bigbigdw.manavarasetting.retrofit.result.BestToksodaResult

class RetrofitToksoda {
    private val apiToksoda = Retrofit.apiToksoda

    fun getBestList(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<BestToksodaResult>) {
        apiToksoda.getBestList(map).enqueue(baseCallback(dataListener))
    }

    fun getBestDetail(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<BestToksodaDetailResult>) {
        apiToksoda.getBestDetail(map).enqueue(baseCallback(dataListener))
    }

}