package com.bigbigdw.manavarasetting.retrofit

import com.bigbigdw.manavarasetting.retrofit.result.OneStoreBookResult
import com.bigbigdw.moavara.Retrofit.RetrofitDataListener
import com.bigbigdw.moavara.Retrofit.baseCallback

class RetrofitOnestore {
    private val apiOneStory = Retrofit.apiOneStory

    fun getBestOneStore(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<OneStoreBookResult>) {
        apiOneStory.getBestOneStore(map).enqueue(baseCallback(dataListener))
    }

    fun getBestOneStorePass(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<OneStoreBookResult>) {
        apiOneStory.getBestOneStorePass(map).enqueue(baseCallback(dataListener))
    }
}
