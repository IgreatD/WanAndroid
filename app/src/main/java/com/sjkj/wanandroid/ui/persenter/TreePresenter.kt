package com.sjkj.wanandroid.ui.persenter

import com.sjkj.wanandroid.bean.HomeData
import com.sjkj.wanandroid.bean.HomeListBean
import com.sjkj.wanandroid.bean.TreeBean
import com.sjkj.wanandroid.data.repertory.KnowRepertory
import com.sjkj.wanandroid.mvp.BaseRecyclePresenter
import com.sjkj.wanandroid.mvp.BaseRecycleView
import com.sjkj.wanandroid.rxhelper.RxSubscriber
import com.sjkj.wanandroid.rxhelper.io_main
import javax.inject.Inject

/**
 * @author by dingl on 2018/3/1.
 * @desc TreePresenter
 */
class TreePresenter @Inject constructor() : BaseRecyclePresenter<BaseRecycleView<TreeBean>>() {

    @Inject
    lateinit var knowRepertory: KnowRepertory

    fun getTreeData() {
        knowRepertory.getTreeData()
                .compose(io_main(lifecycleProvider))
                .subscribe(object : RxSubscriber<List<TreeBean>>(mView) {
                    override fun _onNext(t: List<TreeBean>) {
                        mView.setNewData(t)
                        mView.loadEnd()
                    }
                })
    }

}

class KnowPresenter @Inject constructor() : BaseRecyclePresenter<BaseRecycleView<HomeData>>() {

    @Inject
    lateinit var knowRepertory: KnowRepertory

    fun getKnowData(cid: Int) {
        pageIndex = 1
        knowRepertory.getKnowData(pageIndex - 1, cid)
                .compose(io_main(lifecycleProvider))
                .subscribe(object : RxSubscriber<HomeListBean>(mView) {
                    override fun _onNext(t: HomeListBean) {
                        setData()
                        mView.setNewData(t.datas)
                    }
                })
    }

    fun getMoreKnowData(cid: Int) {
        knowRepertory.getKnowData(pageIndex - 1, cid)
                .compose(io_main(lifecycleProvider))
                .subscribe(object : RxSubscriber<HomeListBean>(mView) {
                    override fun _onNext(t: HomeListBean) {
                        setMoreData(t.datas.size)
                        mView.setMoreData(t.datas)
                    }
                })
    }
}