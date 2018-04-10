package com.sjkj.wanandroid.data.repertory

import com.sjkj.wanandroid.bean.*
import com.sjkj.wanandroid.data.api.*
import com.sjkj.wanandroid.http.createService
import rx.Observable
import javax.inject.Inject

/**
 * @author by dingl on 2018/1/18.
 *
 *
 */
class UserRepertory @Inject constructor() {

    fun login(username: String, password: String): Observable<BaseJsonResult<UserBean>> {
        return createService(UserApi::class.java).login(username, password)
    }

    fun sign(username: String, password: String): Observable<BaseJsonResult<UserBean>> {
        return createService(UserApi::class.java).sign(username, password, password)
    }
}

class CollectionRepertory @Inject constructor() {
    fun collectionArticle(id: Int): Observable<BaseJsonResult<String>> {
        return createService(HomeApi::class.java).collectionArticle(id)
    }

    fun removeCollectionArticle(id: Int): Observable<BaseJsonResult<String>> {
        return createService(HomeApi::class.java).removeCollectArticle(id)
    }

    fun getCollectionData(page: Int): Observable<BaseJsonResult<HomeListBean>> {
        return createService(CollectionApi::class.java).getCollectionData(page)
    }

    fun getCollectionMoreData(page: Int): Observable<BaseJsonResult<HomeListBean>> {
        return createService(CollectionApi::class.java).getCollectionData(page)
    }

}

class HomeRepertory @Inject constructor() {

    fun getHomeListData(page: Int): Observable<BaseJsonResult<HomeListBean>> {
        return createService(HomeApi::class.java).getHomeListData(page)
    }

    fun getHomeBanner(): Observable<BaseJsonResult<List<HomeBannerBean>>> {
        return createService(HomeApi::class.java).getHomeBanner()
    }

}

class KnowRepertory @Inject constructor() {

    fun getTreeData(): Observable<BaseJsonResult<List<TreeBean>>> {
        return createService(KnowApi::class.java).getTreeDate()
    }

    fun getKnowData(page: Int, cid: Int): Observable<BaseJsonResult<HomeListBean>> {
        return createService(KnowApi::class.java).getKnowData(page, cid)
    }

}

class SearchRepertory @Inject constructor() {

    fun getSearchData(page: Int, k: String): Observable<BaseJsonResult<HomeListBean>> {
        return createService(SearchApi::class.java).search(page, k)
    }
}

class HotRepertory @Inject constructor() {
    fun getHotKeyData(): Observable<BaseJsonResult<List<HotBean>>> {
        return createService(HotApi::class.java).getHotKeyData()
    }

    fun getFriendData(): Observable<BaseJsonResult<List<HotBean>>> {
        return createService(HotApi::class.java).getFriendData()
    }
}
