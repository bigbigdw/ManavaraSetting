package com.bigbigdw.moavara.Retrofit

class RetrofitKaKao {
    private val apiKakaoStage = com.bigbigdw.moavara.Retrofit.Retrofit.apiKakaoStage

    //카카오 스테이지 베스트
    fun getBestKakaoStage(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<List<BestResultKakaoStageNovel>>) {
        apiKakaoStage.getBestKakaoStage(map).enqueue(baseCallback(dataListener))
    }
}

