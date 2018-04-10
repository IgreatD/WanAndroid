package com.sjkj.wanandroid.mvp

/**
 * @author by dingl on 2018/1/15.
 * @desc BaseView
 */
interface BaseView {
    fun showToast(toast: String)
    fun showLoading()
    fun hideLoading()
    fun showLoadError() {}
    fun showNetError() {}
    fun loadEnd() {}
    fun loadError() {}
    fun showEmptyView() {}
}

interface BaseRecycleView<in T> : BaseView {
    fun setNewData(t: List<T>)
    fun setMoreData(t: List<T>)
    fun loadComplete()
}
