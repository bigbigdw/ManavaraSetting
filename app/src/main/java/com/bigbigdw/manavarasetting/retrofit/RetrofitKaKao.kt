package com.bigbigdw.moavara.Retrofit

import com.bigbigdw.manavarasetting.retrofit.Retrofit

class RetrofitKaKao {
    private val apiKakaoStage = Retrofit.apiKakaoStage

    //카카오 스테이지 베스트
    fun getBestKakaoStage(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<List<BestResultKakaoStageNovel>>) {
        apiKakaoStage.getBestKakaoStage(map).enqueue(baseCallback(dataListener))
    }
}

