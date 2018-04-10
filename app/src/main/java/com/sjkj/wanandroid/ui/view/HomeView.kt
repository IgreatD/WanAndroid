package com.sjkj.wanandroid.ui.view

import com.sjkj.wanandroid.bean.HomeBannerBean
import com.sjkj.wanandroid.bean.HomeData
import com.sjkj.wanandroid.mvp.BaseRecycleView

/**
 * @author by dingl on 2018/2/28.
 * @desc HomeView
 */
interface HomeView : BaseRecycleView<HomeData> {
    fun loadHomeBannerDataSuccess(list: List<HomeBannerBean>)
    fun loadHomeBannerDataFailed()
}
