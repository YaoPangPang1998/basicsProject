package com.ypp.core.network.model.home

import com.ypp.core.network.TopJsonBean
import com.ypp.datastore.UserInfo
import com.ypp.model.datastore.Banners
import kotlinx.coroutines.flow.Flow

interface HomeDataSource {
    fun banner():Flow<Banners>
    fun topJson():Flow<TopJsonBean>
    fun userInfo():Flow<List<UserInfo>>
    suspend fun updateBanner()
    suspend fun updateTopJson(succeed:()->Unit,fail:()->Unit)
    suspend fun addUserInfo(userInfo: UserInfo)

}