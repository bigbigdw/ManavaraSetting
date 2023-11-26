package com.bigbigdw.manavarasetting.retrofit


import com.bigbigdw.manavarasetting.retrofit.result.JoaraBestDetailResult
import com.bigbigdw.manavarasetting.retrofit.result.JoaraBestListResult

class RetrofitJoara {
    private val apiJoara = Retrofit.apiJoara

    fun getJoaraBookBest(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<JoaraBestListResult>) {
        apiJoara.getJoaraBookBest(map).enqueue(baseCallback(dataListener))
    }

    fun getBookDetailJoa(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<JoaraBestDetailResult>) {
        apiJoara.getBookDetail(map).enqueue(baseCallback(dataListener))
    }

}