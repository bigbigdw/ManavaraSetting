package com.bigbigdw.moavara.Retrofit

class RetrofitRidi {
    private val apiRidi = com.bigbigdw.moavara.Retrofit.Retrofit.apiRidi

        fun getRidiRomance(value : String, map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<String>) {
        apiRidi.getRidi(value = value, queryMap = map).enqueue(baseCallback(dataListener))
    }
}
