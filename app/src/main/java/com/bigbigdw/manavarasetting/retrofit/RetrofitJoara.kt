package com.bigbigdw.moavara.Retrofit

import com.bigbigdw.manavarasetting.retrofit.Retrofit

class RetrofitJoara {
    private val apiJoara = Retrofit.apiJoara

    fun getJoaraBookBest(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<JoaraBestListResult>) {
        apiJoara.getJoaraBookBest(map).enqueue(baseCallback(dataListener))
    }

}