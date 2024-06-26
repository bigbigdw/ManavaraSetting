package com.bigbigdw.manavarasetting.util

import androidx.compose.ui.graphics.Color
import androidx.datastore.preferences.core.Preferences
import com.bigbigdw.manavarasetting.R
import com.bigbigdw.manavarasetting.ui.theme.colorCHALLENGE
import com.bigbigdw.manavarasetting.ui.theme.colorJOARA
import com.bigbigdw.manavarasetting.ui.theme.colorKAKAO
import com.bigbigdw.manavarasetting.ui.theme.colorMUNPIA
import com.bigbigdw.manavarasetting.ui.theme.colorNAVER
import com.bigbigdw.manavarasetting.ui.theme.colorNOBLESS
import com.bigbigdw.manavarasetting.ui.theme.colorONESTORY
import com.bigbigdw.manavarasetting.ui.theme.colorPREMIUM
import com.bigbigdw.manavarasetting.ui.theme.colorRIDI
import com.bigbigdw.manavarasetting.ui.theme.colorTOKSODA

val WeekKor = arrayListOf(
    "일",
    "월",
    "화",
    "수",
    "목",
    "금",
    "토",
)
fun comicListKor(): List<String> {
    return listOf(
        "시리즈",
    )
}
fun novelListKor(): List<String> {
    return listOf(
        "조아라",
        "노블레스",
        "프리미엄",
        "시리즈",
        "네이버 웹소설 유료",
        "네이버 웹소설 무료",
        "챌린지리그",
        "베스트리그",
        "리디 판타지",
        "리디 로맨스",
        "리디 로맨스 판타지",
        "스테이지",
        "원스토리 판타지",
        "원스토리 로맨스",
        "원스토리 PASS 판타지",
        "원스토리 PASS 로맨스",
        "문피아 유료",
        "문피아 무료",
        "톡소다",
        "톡소다 자유연재",
    )
}

fun novelListEng(): List<String> {
    return listOf(
        "JOARA",
        "JOARA_NOBLESS",
        "JOARA_PREMIUM",
        "NAVER_SERIES",
        "NAVER_WEBNOVEL_PAY",
        "NAVER_WEBNOVEL_FREE",
        "NAVER_CHALLENGE",
        "NAVER_BEST",
        "RIDI_FANTAGY",
        "RIDI_ROMANCE",
        "RIDI_ROFAN",
        "KAKAO_STAGE",
        "ONESTORY_FANTAGY",
        "ONESTORY_ROMANCE",
        "ONESTORY_PASS_FANTAGY",
        "ONESTORY_PASS_ROMANCE",
        "MUNPIA_PAY",
        "MUNPIA_FREE",
        "TOKSODA",
        "TOKSODA_FREE",
    )
}

fun genreListEng(): List<String> {
    return listOf(
        "JOARA",
        "JOARA_NOBLESS",
        "JOARA_PREMIUM",
        "RIDI_FANTAGY",
        "RIDI_ROMANCE",
        "RIDI_ROFAN",
        "KAKAO_STAGE",
        "MUNPIA_PAY",
        "MUNPIA_FREE",
        "TOKSODA",
        "TOKSODA_FREE",
    )
}

fun keywordListEng(): List<String> {
    return listOf(
        "JOARA",
        "JOARA_NOBLESS",
        "JOARA_PREMIUM",
        "ONESTORY_FANTAGY",
        "ONESTORY_ROMANCE",
        "ONESTORY_PASS_FANTAGY",
        "ONESTORY_PASS_ROMANCE",
        "TOKSODA",
        "TOKSODA_FREE",
        "RIDI_FANTAGY",
        "RIDI_ROMANCE", "RIDI_ROFAN",
        "NAVER_WEBNOVEL_FREE",
        "NAVER_WEBNOVEL_PAY",
        "NAVER_BEST",
        "NAVER_CHALLENGE",
    )
}



fun changePlatformNameEng(platform : String) : String {
    return when (platform) {
        "조아라" -> {
            "JOARA"
        }
        "노블레스" -> {
            "JOARA_NOBLESS"
        }
        "프리미엄" -> {
            "JOARA_PREMIUM"
        }
        "챌린지리그" -> {
            "NAVER_CHALLENGE"
        }
        "베스트리그" -> {
            "NAVER_BEST"
        }
        "시리즈" -> {
            "NAVER_SERIES"
        }
        "네이버 웹소설 유료" -> {
            "NAVER_WEBNOVEL_PAY"
        }
        "네이버 웹소설 무료" -> {
            "NAVER_WEBNOVEL_FREE"
        }
        "리디 판타지" -> {
            "RIDI_FANTAGY"
        }
        "리디 로맨스" -> {
            "RIDI_ROMANCE"
        }
        "리디 로맨스 판타지" -> {
            "RIDI_ROFAN"
        }
        "스테이지" -> {
            "KAKAO_STAGE"
        }
        "원스토리 판타지" -> {
            "ONESTORY_FANTAGY"
        }
        "원스토리 로맨스" -> {
            "ONESTORY_ROMANCE"
        }
        "원스토리 PASS 판타지" -> {
            "ONESTORY_PASS_FANTAGY"
        }
        "원스토리 PASS 로맨스" -> {
            "ONESTORY_PASS_ROMANCE"
        }
        "문피아 유료" -> {
            "MUNPIA_PAY"
        }
        "문피아 무료" -> {
            "MUNPIA_FREE"
        }
        "톡소다" -> {
            "TOKSODA"
        }
        "톡소다 자유연재" -> {
            "TOKSODA_FREE"
        }
        else -> {
            platform
        }
    }
}

fun changePlatformNameKor(platform : String) : String {
    return when (platform) {
        "JOARA" -> {
            "조아라"
        }
        "JOARA_NOBLESS" -> {
            "노블레스"
        }
        "JOARA_PREMIUM" -> {
            "프리미엄"
        }
        "NAVER_CHALLENGE" -> {
            "챌린지리그"
        }
        "NAVER_BEST" -> {
            "베스트리그"
        }
        "NAVER_SERIES" -> {
            "시리즈"
        }
        "NAVER_WEBNOVEL_PAY" -> {
            "네이버 웹소설 유료"
        }
        "NAVER_WEBNOVEL_FREE" -> {
            "네이버 웹소설 무료"
        }
        "RIDI_FANTAGY" -> {
            "리디 판타지"
        }
        "RIDI_ROMANCE" -> {
            "리디 로맨스"
        }
        "RIDI_ROFAN" -> {
            "리디 로맨스 판타지"
        }
        "KAKAO_STAGE" -> {
            "스테이지"
        }
        "ONESTORY_FANTAGY" -> {
            "원스토리 판타지"
        }
        "ONESTORY_ROMANCE" -> {
            "원스토리 로맨스"
        }
        "ONESTORY_PASS_FANTAGY" -> {
            "원스토리 PASS 판타지"
        }
        "ONESTORY_PASS_ROMANCE" -> {
            "원스토리 PASS 로맨스"
        }
        "MUNPIA_PAY" -> {
            "문피아 유료"
        }
        "MUNPIA_FREE" -> {
            "문피아 무료"
        }
        "TOKSODA" -> {
            "톡소다"
        }
        "TOKSODA_FREE" -> {
            "톡소다 자유연재"
        }
        else -> {
            platform
        }
    }
}

fun changeDetailNameKor(detail : String) : String {
    return if(detail.contains("TODAY_BEST")){
        detail.replace("TODAY_BEST", "투데이 베스트")
    } else if(detail.contains("WEEK_BEST")){
        detail.replace("WEEK_BEST", "주간 베스트")
    }  else if(detail.contains("MONTH_BEST")){
        detail.replace("MONTH_BEST", "월간 베스트")
    } else {
        detail
    }
}

fun getPlatformLogo(platform: String) : Int {
    return when (platform) {
        "조아라" -> {
            R.drawable.logo_joara
        }
        "노블레스" -> {
            R.drawable.logo_joara_nobless
        }
        "프리미엄" -> {
            R.drawable.logo_joara_premium
        }
        "시리즈" -> {
            R.drawable.logo_naver
        }
        "네이버 웹소설 유료" -> {
            R.drawable.logo_naver
        }
        "네이버 웹소설 무료" -> {
            R.drawable.logo_naver
        }
        "챌린지리그" -> {
            R.drawable.logo_naver_challenge
        }
        "베스트리그" -> {
            R.drawable.logo_naver_challenge
        }
        "리디 판타지" -> {
            R.drawable.logo_ridibooks
        }
        "리디 로맨스" -> {
            R.drawable.logo_ridibooks
        }
        "리디 로맨스 판타지" -> {
            R.drawable.logo_ridibooks
        }
        "스테이지" -> {
            R.drawable.logo_kakaostage
        }
        "원스토리 판타지" -> {
            R.drawable.logo_onestore
        }
        "원스토리 로맨스" -> {
            R.drawable.logo_onestore
        }
        "원스토리 PASS 판타지" -> {
            R.drawable.logo_onestore
        }
        "원스토리 PASS 로맨스" -> {
            R.drawable.logo_onestore
        }
        "문피아 유료" -> {
            R.drawable.logo_munpia
        }
        "문피아 무료" -> {
            R.drawable.logo_munpia
        }
        "톡소다" -> {
            R.drawable.logo_toksoda
        }
        "톡소다 자유연재" -> {
            R.drawable.logo_toksoda
        }
        else -> {
            R.drawable.icon_best_wht
        }
    }
}

fun getPlatformLogoEng(platform: String) : Int {
    return when (platform) {
        "JOARA" -> {
            R.drawable.logo_joara
        }
        "JOARA_NOBLESS" -> {
            R.drawable.logo_joara_nobless
        }
        "JOARA_PREMIUM" -> {
            R.drawable.logo_joara_premium
        }
        "NAVER_SERIES" -> {
            R.drawable.logo_naver
        }
        "NAVER_WEBNOVEL_PAY" -> {
            R.drawable.logo_naver
        }
        "NAVER_WEBNOVEL_FREE" -> {
            R.drawable.logo_naver
        }
        "NAVER_CHALLENGE" -> {
            R.drawable.logo_naver_challenge
        }
        "NAVER_BEST" -> {
            R.drawable.logo_naver_challenge
        }
        "RIDI_FANTAGY" -> {
            R.drawable.logo_ridibooks
        }
        "RIDI_ROMANCE" -> {
            R.drawable.logo_ridibooks
        }
        "RIDI_ROFAN" -> {
            R.drawable.logo_ridibooks
        }
        "KAKAO_STAGE" -> {
            R.drawable.logo_kakaostage
        }
        "ONESTORY_FANTAGY" -> {
            R.drawable.logo_onestore
        }
        "ONESTORY_ROMANCE" -> {
            R.drawable.logo_onestore
        }
        "ONESTORY_PASS_FANTAGY" -> {
            R.drawable.logo_onestore
        }
        "ONESTORY_PASS_ROMANCE" -> {
            R.drawable.logo_onestore
        }
        "MUNPIA_PAY" -> {
            R.drawable.logo_munpia
        }
        "MUNPIA_FREE" -> {
            R.drawable.logo_munpia
        }
        "TOKSODA" -> {
            R.drawable.logo_toksoda
        }
        "TOKSODA_FREE" -> {
            R.drawable.logo_toksoda
        }
        else -> {
            R.drawable.logo_transparent
        }
    }
}

fun getPlatformColor(platform: String): Color {
    return when (platform) {
        "조아라" -> {
            colorJOARA
        }
        "노블레스" -> {
            colorNOBLESS
        }
        "프리미엄" -> {
            colorPREMIUM
        }
        "시리즈" -> {
            colorNAVER
        }
        "네이버 웹소설 유료" -> {
            colorNAVER
        }
        "네이버 웹소설 무료" -> {
            colorNAVER
        }
        "챌린지리그" -> {
            colorCHALLENGE
        }
        "베스트리그" -> {
            colorCHALLENGE
        }
        "리디 판타지" -> {
            colorRIDI
        }
        "리디 로맨스" -> {
            colorRIDI
        }
        "리디 로맨스 판타지" -> {
            colorRIDI
        }
        "스테이지" -> {
            colorKAKAO
        }
        "원스토리 판타지" -> {
            colorONESTORY
        }
        "원스토리 로맨스" -> {
            colorONESTORY
        }
        "원스토리 PASS 판타지" -> {
            colorONESTORY
        }
        "원스토리 PASS 로맨스" -> {
            colorONESTORY
        }
        "문피아 유료" -> {
            colorMUNPIA
        }
        "문피아 무료" -> {
            colorMUNPIA
        }
        "톡소다" -> {
            colorTOKSODA
        }
        "톡소다 자유연재" -> {
            colorTOKSODA
        }
        else -> {
            Color.Black
        }
    }
}

fun getPlatformColorEng(platform: String): Color {
    return when (platform) {
        "JOARA" -> {
            colorJOARA
        }
        "JOARA_NOBLESS" -> {
            colorNOBLESS
        }
        "JOARA_PREMIUM" -> {
            colorPREMIUM
        }
        "NAVER_SERIES" -> {
            colorNAVER
        }
        "NAVER_WEBNOVEL_PAY" -> {
            colorNAVER
        }
        "NAVER_WEBNOVEL_FREE" -> {
            colorNAVER
        }
        "NAVER_CHALLENGE" -> {
            colorCHALLENGE
        }
        "NAVER_BEST" -> {
            colorCHALLENGE
        }
        "RIDI_FANTAGY" -> {
            colorRIDI
        }
        "RIDI_ROMANCE" -> {
            colorRIDI
        }
        "RIDI_ROFAN" -> {
            colorRIDI
        }
        "KAKAO_STAGE" -> {
            colorKAKAO
        }
        "ONESTORY_FANTAGY" -> {
            colorONESTORY
        }
        "ONESTORY_ROMANCE" -> {
            colorONESTORY
        }
        "ONESTORY_PASS_FANTAGY" -> {
            colorONESTORY
        }
        "ONESTORY_PASS_ROMANCE" -> {
            colorONESTORY
        }
        "MUNPIA_PAY" -> {
            colorMUNPIA
        }
        "MUNPIA_FREE" -> {
            colorMUNPIA
        }
        "TOKSODA" -> {
            colorTOKSODA
        }
        "TOKSODA_FREE" -> {
            colorTOKSODA
        }
        else -> {
            Color.Black
        }
    }
}

fun getPlatformDescriptionEng(platform: String) : String {
    return when (platform) {
        "JOARA" -> {
            "조아라"
        }
        "JOARA_NOBLESS" -> {
            "조아라 노블레스"
        }
        "JOARA_PREMIUM" -> {
            "조아라 프리미엄"
        }
        "NAVER_SERIES" -> {
            "네이버 시리즈"
        }
        "NAVER_WEBNOVEL_PAY" -> {
            "네이버 웹소설 유료"
        }
        "NAVER_WEBNOVEL_FREE" -> {
            "네이버 웹소설 무료"
        }
        "NAVER_CHALLENGE" -> {
            "네이버 챌린지 리그"
        }
        "NAVER_BEST" -> {
            "네이버 베스트 리그"
        }
        "RIDI_FANTAGY" -> {
            "리디북스 판타지"
        }
        "RIDI_ROMANCE" -> {
            "리디북스 로맨스"
        }
        "RIDI_ROFAN" -> {
            "리디 로맨스 판타지"
        }
        "KAKAO_STAGE" -> {
            "카카오 스테이지"
        }
        "ONESTORY_FANTAGY" -> {
            "원스토리 판타지"
        }
        "ONESTORY_ROMANCE" -> {
            "원스토리 로맨스"
        }
        "ONESTORY_PASS_FANTAGY" -> {
            "원스토리 PASS 판타지"
        }
        "ONESTORY_PASS_ROMANCE" -> {
            "원스토리 PASS 로맨스"
        }
        "MUNPIA_PAY" -> {
            "문피아 유료"
        }
        "MUNPIA_FREE" -> {
            "문피아 무료"
        }
        "TOKSODA" -> {
            "톡소다"
        }
        "TOKSODA_FREE" -> {
            "톡소다 자유연재"
        }
        else -> {
            "하하"
        }
    }
}

fun getPlatformDescription(platform: String) : String {
    return when (platform) {
        "시리즈" -> {
            "네이버 시리즈"
        }
        "프리미엄" -> {
            "조아라 프리미엄"
        }
        "노블레스" -> {
            "조아라 노블레스"
        }
        "조아라" -> {
            "조아라"
        }
        "네이버 웹소설 유료" -> {
            "네이버 웹소설 유료"
        }
        "네이버 웹소설 무료" -> {
            "네이버 웹소설 무료"
        }
        "챌린지리그" -> {
            "네이버 챌린지 리그"
        }
        "베스트리그" -> {
            "네이버 베스트 리그"
        }
        "리디 판타지" -> {
            "리디북스 판타지"
        }
        "리디 로맨스" -> {
            "리디북스 로맨스"
        }
        "리디 로맨스 판타지" -> {
            "리디 로맨스 판타지"
        }
        "스테이지" -> {
            "카카오 스테이지"
        }
        "원스토리 판타지" -> {
            "원스토리 판타지"
        }
        "원스토리 로맨스" -> {
            "원스토리 로맨스"
        }
        "원스토리 PASS 판타지" -> {
            "원스토리 PASS 판타지"
        }
        "원스토리 PASS 로맨스" -> {
            "원스토리 PASS 로맨스"
        }
        "문피아 유료" -> {
            "문피아 유료"
        }
        "문피아 무료" -> {
            "문피아 무료"
        }
        "톡소다" -> {
            "톡소다"
        }
        "톡소다 자유연재" -> {
            "톡소다 자유연재"
        }
        else -> {
            "하하"
        }
    }
}
fun weekListAll(): List<String> {
    return listOf(
        "전체",
        "일요일",
        "월요일",
        "화요일",
        "수요일",
        "목요일",
        "금요일",
        "토요일"
    )
}

fun weekList(): List<String> {
    return listOf(
        "일요일",
        "월요일",
        "화요일",
        "수요일",
        "목요일",
        "금요일",
        "토요일"
    )
}

fun getWeekDate(date : String) : Int {
    return when (date) {
        "일요일" -> {
            0
        }
        "월요일" -> {
            1
        }
        "화요일" -> {
            2
        }
        "수요일" -> {
            3
        }
        "목요일" -> {
            4
        }
        "금요일" -> {
            5
        }
        "토요일" -> {
            6
        }
        else -> {
            0
        }
    }
}

fun geMonthDate(date : String) : Int {
    return when (date) {
        "1주차" -> {
            0
        }
        "2주차" -> {
            1
        }
        "3주차" -> {
            2
        }
        "4주차" -> {
            3
        }
        "5주차" -> {
            4
        }
        "6주차" -> {
            5
        }
        else -> {
            0
        }
    }
}

fun getPlatformDataKeyNovel(platform : String) : Preferences.Key<String> {
    return when (platform) {
        "JOARA" -> {
            DataStoreManager.JOARA
        }
        "JOARA_NOBLESS" -> {
            DataStoreManager.JOARA_NOBLESS
        }
        "JOARA_PREMIUM" -> {
            DataStoreManager.JOARA_PREMIUM
        }
        "NAVER_CHALLENGE" -> {
            DataStoreManager.NAVER_CHALLENGE
        }
        "NAVER_BEST" -> {
            DataStoreManager.NAVER_BEST
        }
        "NAVER_SERIES" -> {
            DataStoreManager.NAVER_SERIES_NOVEL
        }
        "NAVER_WEBNOVEL_PAY" -> {
            DataStoreManager.NAVER_WEBNOVEL_PAY
        }
        "NAVER_WEBNOVEL_FREE" -> {
            DataStoreManager.NAVER_WEBNOVEL_FREE
        }
        "RIDI_FANTAGY" -> {
            DataStoreManager.RIDI_FANTAGY
        }
        "RIDI_ROMANCE" -> {
            DataStoreManager.RIDI_ROMANCE
        }
        "RIDI_ROFAN" -> {
            DataStoreManager.RIDI_ROFAN
        }
        "KAKAO_STAGE" -> {
            DataStoreManager.KAKAO_STAGE
        }
        "ONESTORY_FANTAGY" -> {
            DataStoreManager.ONESTORY_FANTAGY
        }
        "ONESTORY_ROMANCE" -> {
            DataStoreManager.ONESTORY_ROMANCE
        }
        "ONESTORY_PASS_FANTAGY" -> {
            DataStoreManager.ONESTORY_PASS_FANTAGY
        }
        "ONESTORY_PASS_ROMANCE" -> {
            DataStoreManager.ONESTORY_PASS_ROMANCE
        }
        "MUNPIA_PAY" -> {
            DataStoreManager.MUNPIA_PAY
        }
        "MUNPIA_FREE" -> {
            DataStoreManager.MUNPIA_FREE
        }
        "TOKSODA" -> {
            DataStoreManager.TOKSODA
        }
        "TOKSODA_FREE" -> {
            DataStoreManager.TOKSODA_FREE
        }
        else -> {
            DataStoreManager.TEST
        }
    }
}

fun comicListEng(): List<String> {
    return listOf(
        "NAVER_SERIES"
    )
}

fun getPlatformDataKeyComic(platform : String) : Preferences.Key<String> {
    return when (platform) {
        "NAVER_SERIES" -> {
            DataStoreManager.NAVER_SERIES_COMIC
        }
        else -> {
            DataStoreManager.TEST
        }
    }
}

fun miningListAllEng(): List<String> {
    return listOf(
        "NOVEL+JOARA",
        "NOVEL+JOARA_NOBLESS",
        "NOVEL+JOARA_PREMIUM",
        "NOVEL+NAVER_SERIES",
        "NOVEL+NAVER_WEBNOVEL_PAY",
        "NOVEL+NAVER_WEBNOVEL_FREE",
        "NOVEL+NAVER_CHALLENGE",
        "NOVEL+NAVER_BEST",
        "NOVEL+RIDI_FANTAGY",
        "NOVEL+RIDI_ROMANCE",
        "NOVEL+RIDI_ROFAN",
        "NOVEL+KAKAO_STAGE",
        "NOVEL+ONESTORY_FANTAGY",
        "NOVEL+ONESTORY_ROMANCE",
        "NOVEL+ONESTORY_PASS_FANTAGY",
        "NOVEL+ONESTORY_PASS_ROMANCE",
        "NOVEL+MUNPIA_PAY",
        "NOVEL+MUNPIA_FREE",
        "NOVEL+TOKSODA",
        "NOVEL+TOKSODA_FREE",
        "COMIC+NAVER_SERIES",
    )
}