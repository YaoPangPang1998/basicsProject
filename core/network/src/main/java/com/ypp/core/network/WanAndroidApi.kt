package com.ypp.core.network

import com.ypp.model.BannerBean
import retrofit2.http.GET

interface WanAndroidApi {
    companion object{
        const val BaseUrl="https://www.wanandroid.com"
    }
    @GET("/banner/json")
    suspend fun banner(): BannerBean

    @GET("/article/top/json")
    suspend fun topJson(): com.ypp.model.TopJsonBean
}