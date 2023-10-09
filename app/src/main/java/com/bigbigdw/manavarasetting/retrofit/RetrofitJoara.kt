package com.bigbigdw.moavara.Retrofit

class RetrofitJoara {
    private val apiJoara = Retrofit.apiJoara

    fun getJoaraBookBest(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<JoaraBestListResult>) {
        apiJoara.getJoaraBookBest(map).enqueue(baseCallback(dataListener))
    }

}