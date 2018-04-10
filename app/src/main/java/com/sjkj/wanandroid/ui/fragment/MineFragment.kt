package com.sjkj.wanandroid.ui.fragment

import android.os.Bundle
import com.sjkj.wanandroid.R
import com.sjkj.wanandroid.base.BaseRxFragment
import com.sjkj.wanandroid.bean.HomeBannerBean
import com.sjkj.wanandroid.bean.HomeData
import com.sjkj.wanandroid.ui.persenter.HomePresenter
import com.sjkj.wanandroid.ui.view.HomeView
import com.sjkj.wanandroid.utils.GlideImageLoader
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 * @author by dingl on 2018/2/28.
 * @desc HomeFragment
 */
class MineFragment : BaseRxFragment<HomePresenter>(), HomeView {

//    override fun favSuccess(position: Int) {
//    }
//
//    override fun favFailed() {
//    }
//
//    override fun removeFavSuccess(position: Int) {
//    }
//
//    override fun removeFavFailed() {
//    }

    override fun injectComponent() {
//        DaggerHomeRepertoryComponent.builder()
//                .activityComponent(mActivityComponent)
//                .build()
//                .inject(this)
//        mPresenter.mView = this
    }

    override fun loadHomeBannerDataSuccess(list: List<HomeBannerBean>) {
        banner.setImages(list.map { it.imagePath })
        banner.setBannerTitles(list.map { it.title })
        banner.start()
    }

    override fun loadHomeBannerDataFailed() {
    }

    override fun setNewData(t: List<HomeData>) {
    }

    override fun setMoreData(t: List<HomeData>) {
    }

    override fun loadComplete() {
    }

    override fun getLayoutId() = R.layout.fragment_mine

    override fun initView(savedInstanceState: Bundle?) {
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE)
        banner.setImageLoader(GlideImageLoader())

    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
//        mPresenter.getHomeBannerData()
    }
}
