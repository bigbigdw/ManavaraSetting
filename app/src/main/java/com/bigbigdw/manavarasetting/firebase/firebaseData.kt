package com.bigbigdw.manavarasetting.firebase

class DataFCMBody(
    var to: String? = "/topics/all",
    var priority: String? = "high",
    var data: DataFCMBodyData? = null,
    var notification: DataFCMBodyNotification? = null,
)

class DataFCMBodyData(
    var activity: String = "",
    var data: String = "",
)

class DataFCMBodyNotification(
    var title: String = "notification 타이틀",
    var body: String = "notification message",
    var click_action : String = "",
)