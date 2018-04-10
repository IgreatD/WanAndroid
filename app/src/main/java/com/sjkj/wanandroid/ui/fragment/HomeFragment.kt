package com.sjkj.wanandroid.ui.fragment

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.os.Bundle
import android.support.v4.util.Pair
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.sjkj.wanandroid.R
import com.sjkj.wanandroid.base.BaseRxFragment
import com.sjkj.wanandroid.bean.HomeBannerBean
import com.sjkj.wanandroid.bean.HomeData
import com.sjkj.wanandroid.common.Common
import com.sjkj.wanandroid.di.module.CollectionRepertoryModule
import com.sjkj.wanandroid.di.module.HomeRepertoryModule
import com.sjkj.wanandroid.extensions.dismiss
import com.sjkj.wanandroid.extensions.getInteger
import com.sjkj.wanandroid.extensions.transitionTo
import com.sjkj.wanandroid.extensions.transitionToResult
import com.sjkj.wanandroid.ui.activity.BrowserActivity
import com.sjkj.wanandroid.ui.activity.LoginActivity
import com.sjkj.wanandroid.ui.adapter.HomeListAdapter
import com.sjkj.wanandroid.ui.persenter.FavPresenter
import com.sjkj.wanandroid.ui.persenter.HomePresenter
import com.sjkj.wanandroid.ui.view.FavView
import com.sjkj.wanandroid.ui.view.HomeView
import com.sjkj.wanandroid.utils.AnimUtils
import com.sjkj.wanandroid.utils.GlideImageLoader
import com.sjkj.wanandroid.utils.PreferenceUtils
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.support.v4.onRefresh
import javax.inject.Inject

/**
 * @author by dingl on 2018/2/28.
 * @desc HomeFragment
 */
class HomeFragment : BaseRxFragment<HomePresenter>(), HomeView, FavView, BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        private val HOME_LIST_DATA = "home_list_data"
        private val HOME_BANNER_DATA = "home_banner_data"
    }

    private lateinit var banner: Banner

    private val isLogin by PreferenceUtils(Common.LOGIN_KEY, false)

    @Inject
    lateinit var favPresenter: FavPresenter

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View?, position: Int) {
        view ?: return
        val homeData = adapter.data[position] as HomeData
        when (view.id) {
            R.id.homeItemLike -> {
                if (isLogin) {
                    val scaleX by lazy { PropertyValuesHolder.ofFloat("scaleX", 1F, 2F, 1F) }
                    val scaleY by lazy { PropertyValuesHolder.ofFloat("scaleY", 1F, 2F, 1F) }
                    val animator by lazy { ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY) }
                    animator.start()
                    animator.addListener(object : AnimUtils.AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animator: Animator?) {
                            super.onAnimationEnd(animator)
                            animator?.removeListener(this)
                            favPresenter.toggleCollectionArticle(homeData.collect, homeData.id, position)
                        }
                    })
                } else {
                    val intent = Intent(activity, LoginActivity::class.java)
                    transitionToResult(intent, Common.LOGIN_RESULT_CODE, Pair(view, getString(R.string.dn_login_action)))
                }
            }
        }
    }

    override fun loadHomeBannerDataSuccess(list: List<HomeBannerBean>) {
        with(banner) {
            setImages(list.map { it.imagePath })
            setBannerTitles(list.map { it.title })
            start()
            setOnBannerListener({
                startBrowserActivity(list[it].url)
            })
        }
    }

    override fun loadHomeBannerDataFailed() {

    }

    private val homeListAdapter by lazy {
        HomeListAdapter {
            startBrowserActivity(it)
        }
    }

    private fun startBrowserActivity(it: String) {
        val intent = Intent(context, BrowserActivity::class.java)
        intent.putExtra(BrowserActivity.PARAM_URL, it)
        transitionTo(intent)
    }

    override fun setNewData(t: List<HomeData>) {
        homeListAdapter.setNewData(t)
        refresh.dismiss()
    }

    override fun setMoreData(t: List<HomeData>) {
        homeListAdapter.addData(t)
    }

    override fun loadEnd() {
        refresh.dismiss()
        homeListAdapter.loadMoreEnd()
    }

    override fun loadError() {
        homeListAdapter.loadMoreFail()
        refresh.dismiss()
    }

    override fun loadComplete() {
        homeListAdapter.loadMoreComplete()
    }

    override fun injectComponent() {
        mActivityComponent.getHomeRepertoryComponent(HomeRepertoryModule()).inject(this)
        mActivityComponent.getCollectionRepertoryComponent(CollectionRepertoryModule()).inject(this)
        mPresenter.mView = this
        favPresenter.mView = this
    }

    override fun getLayoutId() = R.layout.fragment_home

    override fun initView(savedInstanceState: Bundle?) {

        banner = layoutInflater.inflate(R.layout.view_home_banner, recyclerview.parent as ViewGroup, false) as Banner

        with(banner) {
            setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
            setImageLoader(GlideImageLoader())
        }

        with(refresh) {
            setColorSchemeResources(R.color.colorPrimary)
            onRefresh {
                isRefreshing = true
                mPresenter.getHomeData()
            }
        }

        with(homeListAdapter) {
            addHeaderView(banner)
            setOnLoadMoreListener({
                mPresenter.getMoreHomeData()
            }, recyclerview)
            setHeaderAndEmpty(true)
            emptyView = mEmptyView
            onItemChildClickListener = this@HomeFragment
        }

        recyclerview.apply {
            adapter = homeListAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        with(mEmptyView) {
            setLoadingShowing(true)
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        if (homeListAdapter.data.size == 0) {
            mPresenter.getHomeData()
            mPresenter.getHomeBannerData()
        }
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        banner.postDelayed({ banner.startAutoPlay() }, getInteger(R.integer.transition_duration))
    }

    override fun onSupportInvisible() {
        super.onSupportInvisible()
        banner.stopAutoPlay()
    }

    override fun favSuccess(position: Int) {
        val data = homeListAdapter.data[position]
        data.collect = true
        homeListAdapter.setData(position, data)
    }

    override fun favFailed() {

    }

    override fun removeFavSuccess(position: Int) {
        val data = homeListAdapter.data[position]
        data.collect = false
        homeListAdapter.setData(position, data)
    }

    override fun removeFavFailed() {

    }

}
