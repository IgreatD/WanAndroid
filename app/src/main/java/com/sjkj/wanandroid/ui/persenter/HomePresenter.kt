package com.sjkj.wanandroid.ui.persenter

import com.sjkj.wanandroid.bean.HomeBannerBean
import com.sjkj.wanandroid.bean.HomeListBean
import com.sjkj.wanandroid.data.repertory.HomeRepertory
import com.sjkj.wanandroid.mvp.BaseRecyclePresenter
import com.sjkj.wanandroid.rxhelper.RxSubscriber
import com.sjkj.wanandroid.rxhelper.io_main
import com.sjkj.wanandroid.ui.view.HomeView
import javax.inject.Inject

/**
 * @author by dingl on 2018/2/28.
 * @desc HomePresenter
 */
class HomePresenter @Inject constructor() : BaseRecyclePresenter<HomeView>() {

    @Inject
    lateinit var homeRepertory: HomeRepertory

    fun getHomeData() {
        pageIndex = 1
        homeRepertory.getHomeListData(pageIndex - 1)
                .compose(io_main(lifecycleProvider))
                .subscribe(object : RxSubscriber<HomeListBean>(mView) {
                    override fun _onNext(t: HomeListBean) {
                        setData()
                        mView.setNewData(t.datas)
                    }
                })
    }

    fun getMoreHomeData() {
        homeRepertory.getHomeListData(pageIndex - 1)
                .compose(io_main(lifecycleProvider))
                .subscribe(object : RxSubscriber<HomeListBean>(mView) {
                    override fun _onNext(t: HomeListBean) {
                        setMoreData(t.datas.size)
                        mView.setMoreData(t.datas)
                    }
                })
    }

    fun getHomeBannerData() {
        homeRepertory.getHomeBanner()
                .compose(io_main(lifecycleProvider))
                .subscribe(object : RxSubscriber<List<HomeBannerBean>>(mView) {
                    override fun _onNext(t: List<HomeBannerBean>) {
                        mView.loadHomeBannerDataSuccess(t)
                    }

                    override fun _onError(toast: String) {
                        super._onError(toast)
                        mView.loadHomeBannerDataFailed()
                    }
                })
    }

}
