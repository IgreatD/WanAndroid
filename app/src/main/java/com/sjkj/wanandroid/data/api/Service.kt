package com.sjkj.wanandroid.data.api

import com.sjkj.wanandroid.bean.*
import retrofit2.http.*
import rx.Observable

/**
 * @author by dingl on 2018/2/28.
 * @desc Service
 */
interface CollectionApi {

    @GET("/lg/collect/list/{page}/json")
    fun getCollectionData(@Path("page") page: Int): Observable<BaseJsonResult<HomeListBean>>

}

interface SearchApi {
    @POST("/article/query/{page}/json")
    @FormUrlEncoded
    fun search(@Path("page") page: Int,
               @Field("k") k: String): Observable<BaseJsonResult<HomeListBean>>
}

interface UserApi {

    @POST("/user/login")
    @FormUrlEncoded
    fun login(@Field("username") username: String,
              @Field("password") password: String): Observable<BaseJsonResult<UserBean>>

    @POST("/user/register")
    @FormUrlEncoded
    fun sign(@Field("username") username: String,
             @Field("password") password: String,
             @Field("repassword") repassword: String): Observable<BaseJsonResult<UserBean>>

}

interface HomeApi {
    @GET("/article/list/{page}/json")
    fun getHomeListData(@Path("page") page: Int): Observable<BaseJsonResult<HomeListBean>>

    @GET("/banner/json")
    fun getHomeBanner(): Observable<BaseJsonResult<List<HomeBannerBean>>>

    @POST("/lg/collect/{id}/json")
    fun collectionArticle(@Path("id") id: Int): Observable<BaseJsonResult<String>>

    @POST("/lg/uncollect/{id}/json")
    @FormUrlEncoded
    fun removeCollectArticle(@Path("id") id: Int,
                             @Field("originId") originId: Int = -1): Observable<BaseJsonResult<String>>
}

interface KnowApi {
    @GET("/tree/json")
    fun getTreeDate(): Observable<BaseJsonResult<List<TreeBean>>>

    @GET("/article/list/{page}/json")
    fun getKnowData(@Path("page") page: Int,
                    @Query("cid") cid: Int): Observable<BaseJsonResult<HomeListBean>>
}

interface HotApi {

    @GET("/hotkey/json")
    fun getHotKeyData(): Observable<BaseJsonResult<List<HotBean>>>

    @GET("/friend/json")
    fun getFriendData(): Observable<BaseJsonResult<List<HotBean>>>

}
