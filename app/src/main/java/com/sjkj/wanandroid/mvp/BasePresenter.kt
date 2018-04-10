package com.sjkj.wanandroid.mvp

import android.content.Context
import com.trello.rxlifecycle.LifecycleProvider
import javax.inject.Inject

/**
 * @author by dingl on 2018/1/15.
 * @desc BasePresenter
 */
open class BasePresenter<V : BaseView> {

    lateinit var mView: V

    @Inject
    lateinit var lifecycleProvider: LifecycleProvider<*>

    @Inject
    lateinit var context: Context

}

open class BaseRecyclePresenter<V : BaseRecycleView<*>> : BasePresenter<V>() {

    protected open var pageIndex = 1

    fun getCurrentPageIndex() = pageIndex

    fun setCurrentPageIndex(pageIndex: Int) {
        this.pageIndex = pageIndex
    }

    fun setData() {
        mView.loadComplete()
        pageIndex++
    }

    fun setMoreData(size: Int) {
        if (size == 0) {
            mView.loadEnd()
        } else {
            pageIndex++
            mView.loadComplete()
        }
    }
}
