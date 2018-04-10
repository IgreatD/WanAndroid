package com.sjkj.wanandroid.ui.persenter

import com.sjkj.wanandroid.bean.HomeData
import com.sjkj.wanandroid.bean.HomeListBean
import com.sjkj.wanandroid.data.repertory.SearchRepertory
import com.sjkj.wanandroid.mvp.BaseRecyclePresenter
import com.sjkj.wanandroid.mvp.BaseRecycleView
import com.sjkj.wanandroid.rxhelper.RxSubscriber
import com.sjkj.wanandroid.rxhelper.io_main
import javax.inject.Inject

/**
 * @author by dingl on 2018/3/5.
 * @desc SearchPresenter
 */
class SearchPresenter @Inject constructor() : BaseRecyclePresenter<BaseRecycleView<HomeData>>() {

    @Inject
    lateinit var searchRepertory: SearchRepertory

    fun getSearchData(k: String) {
        pageIndex = 1
        searchRepertory.getSearchData(pageIndex - 1, k)
                .compose(io_main(lifecycleProvider))
                .subscribe(object : RxSubscriber<HomeListBean>(mView) {
                    override fun _onNext(t: HomeListBean) {
                        setData()
                        mView.setNewData(t.datas)
                    }

                })
    }

    fun getMoreSearchData(k: String) {
        searchRepertory.getSearchData(pageIndex - 1, k)
                .compose(io_main(lifecycleProvider))
                .subscribe(object : RxSubscriber<HomeListBean>(mView) {
                    override fun _onNext(t: HomeListBean) {
                        setMoreData(t.datas.size)
                        mView.setMoreData(t.datas)
                    }

                })
    }
}

