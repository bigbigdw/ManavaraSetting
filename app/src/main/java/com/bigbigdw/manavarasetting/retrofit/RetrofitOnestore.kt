package com.bigbigdw.moavara.Retrofit

import com.bigbigdw.manavarasetting.retrofit.result.OneStoreBookResult

class RetrofitOnestore {
    private val apiOneStory = com.bigbigdw.moavara.Retrofit.Retrofit.apiOneStory

    fun getBestOneStore(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<OneStoreBookResult>) {
        apiOneStory.getBestOneStore(map).enqueue(baseCallback(dataListener))
    }
}
