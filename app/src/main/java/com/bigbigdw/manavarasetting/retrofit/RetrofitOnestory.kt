package com.bigbigdw.manavarasetting.retrofit

import com.bigbigdw.manavarasetting.retrofit.result.OneStoreBookResult
import com.bigbigdw.manavarasetting.retrofit.result.OnestoreBookDetail

class RetrofitOnestory {
    private val apiOneStory = Retrofit.apiOneStory

    fun getBestOneStore(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<OneStoreBookResult>) {
        apiOneStory.getBestOneStore(map).enqueue(baseCallback(dataListener))
    }

    fun getBestOneStorePass(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<OneStoreBookResult>) {
        apiOneStory.getBestOneStorePass(map).enqueue(baseCallback(dataListener))
    }

    fun getOneStoreDetail(id: String, map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<OnestoreBookDetail>) {
        apiOneStory.getOneStoryBookDetail(id, map).enqueue(baseCallback(dataListener))
    }
}
