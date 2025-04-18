package com.ypp.data.repository

import androidx.datastore.core.DataStore
import com.ypp.core.network.NetWorkClient
import com.ypp.core.network.request
import com.ypp.datastore.UserInfo
import com.ypp.datastore.database.UserInfoDatabase
import com.ypp.model.datastore.Banners
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class NetWorkHomeDataSource @Inject constructor(
    private val netWorkClient: NetWorkClient,
    private val banners: DataStore<Banners>,
    private val userInfoDatabase: UserInfoDatabase
) : HomeDataSource {
    private val _topJsonFlow= MutableStateFlow(com.ypp.model.TopJsonBean())
    override fun banner(): Flow<Banners> =banners.data

    override fun topJson(): Flow<com.ypp.model.TopJsonBean> =_topJsonFlow.asStateFlow()
    override fun userInfo(): Flow<List<UserInfo>> =userInfoDatabase.userDao().getUsers()

    override suspend fun updateBanner():Result<com.ypp.model.BannerBean> {

       return request({
            netWorkClient.banner()
        },{bannerBean->
            val newBanners = Banners.newBuilder()
            bannerBean.data.forEach { networkBanner ->
                val bannerProto = Banners.Banner.newBuilder()
                    .setId(networkBanner.id)
                    .setUrl(networkBanner.url)
                    .setTitle(networkBanner.title)
                    .setDesc(networkBanner.desc)
                    .setImagePath(networkBanner.imagePath)
                    .setIsVisible(networkBanner.isVisible)
                    .setOrder(networkBanner.order)
                    .build()
                newBanners.addBanner(bannerProto)
            }
            banners.updateData {
                newBanners.build()
            }
        })

    }
    override suspend fun updateTopJson(succeed: () -> Unit, fail: () -> Unit) {

        try {
            val response=netWorkClient.topJson()
            if (response.errorCode==0){
                _topJsonFlow.emit(response)
                succeed()
            }else{
                fail()
            }

        }catch (e:Exception){
            fail()
        }


    }

    override suspend fun addUserInfo(userInfo: UserInfo) {
        userInfoDatabase.userDao().addUserInfo(userInfo)
    }
}