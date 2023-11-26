package com.bigbigdw.manavarasetting.retrofit

import com.bigbigdw.manavarasetting.retrofit.result.BestResultKakaoStageNovel

class RetrofitKaKao {
    private val apiKakaoStage = Retrofit.apiKakaoStage

    //카카오 스테이지 베스트
    fun getBestKakaoStage(map: MutableMap<String?, Any>, dataListener: RetrofitDataListener<List<BestResultKakaoStageNovel>>) {
        apiKakaoStage.getBestKakaoStage(map).enqueue(baseCallback(dataListener))
    }
}

