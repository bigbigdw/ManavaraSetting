package com.bigbigdw.manavarasetting.main.screen

import com.bigbigdw.manavarasetting.R



sealed class ScreemBottomItem(val title: String, val iconOn: Int, val iconOff: Int, val screenRoute: String) {
    object SETTING : ScreemBottomItem("세팅", R.drawable.icon_setting, R.drawable.icon_setting_gr, "SETTING")
    object FCM : ScreemBottomItem("FCM", R.drawable.icon_fcm, R.drawable.icon_fcm_gr, "FCM")
    object NOVEL : ScreemBottomItem("웹소설", R.drawable.icon_novel, R.drawable.icon_novel_gr, "NOVEL")
    object WEBTOON : ScreemBottomItem("웹툰", R.drawable.icon_webtoon, R.drawable.icon_webtoon_gr, "WEBTOON")
}