package com.sjkj.wanandroid.ui.persenter

import com.sjkj.wanandroid.bean.HomeData
import com.sjkj.wanandroid.bean.HomeListBean
import com.sjkj.wanandroid.data.repertory.CollectionRepertory
import com.sjkj.wanandroid.mvp.BaseRecyclePresenter
import com.sjkj.wanandroid.mvp.BaseRecycleView
import com.sjkj.wanandroid.rxhelper.RxSubscriber
import com.sjkj.wanandroid.rxhelper.io_main
import javax.inject.Inject

/**
 * @author by dingl on 2018/3/6.
 * @desc CollectionPresenter
 */
class CollectionPresenter @Inject constructor() : BaseRecyclePresenter<BaseRecycleView<HomeData>>() {

    @Inject
    lateinit var collectionRepertory: CollectionRepertory

    fun getCollectionData() {
        pageIndex = 1
        collectionRepertory.getCollectionData(pageIndex - 1)
                .compose(io_main(lifecycleProvider))
                .subscribe(object : RxSubscriber<HomeListBean>(mView) {
                    override fun _onNext(t: HomeListBean) {
                        setData()
                        mView.setNewData(t.datas)
                    }

                })
    }

    fun getCollectionMoreData() {
        collectionRepertory.getCollectionMoreData(pageIndex - 1)
                .compose(io_main(lifecycleProvider))
                .subscribe(object : RxSubscriber<HomeListBean>(mView) {
                    override fun _onNext(t: HomeListBean) {
                        setMoreData(t.datas.size)
                        mView.setMoreData(t.datas)
                    }
                })
    }

}