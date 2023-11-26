package com.bigbigdw.manavarasetting.retrofit

interface RetrofitDataListener<T> {
    fun onSuccess(data: T)
}