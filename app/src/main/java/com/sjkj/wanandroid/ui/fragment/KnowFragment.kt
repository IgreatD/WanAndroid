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
import com.chad.library.adapter.base.BaseQuickAdapter
import com.sjkj.wanandroid.R
import com.sjkj.wanandroid.base.BaseRxFragment
import com.sjkj.wanandroid.bean.HomeData
import com.sjkj.wanandroid.common.Common
import com.sjkj.wanandroid.di.module.CollectionRepertoryModule
import com.sjkj.wanandroid.di.module.KnowRepertoryModule
import com.sjkj.wanandroid.extensions.dismiss
import com.sjkj.wanandroid.extensions.transitionTo
import com.sjkj.wanandroid.extensions.transitionToResult
import com.sjkj.wanandroid.mvp.BaseRecycleView
import com.sjkj.wanandroid.ui.activity.BrowserActivity
import com.sjkj.wanandroid.ui.activity.LoginActivity
import com.sjkj.wanandroid.ui.adapter.HomeListAdapter
import com.sjkj.wanandroid.ui.persenter.FavPresenter
import com.sjkj.wanandroid.ui.persenter.KnowPresenter
import com.sjkj.wanandroid.ui.view.FavView
import com.sjkj.wanandroid.utils.AnimUtils
import com.sjkj.wanandroid.utils.PreferenceUtils
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.support.v4.onRefresh
import javax.inject.Inject

/**
 * @author by dingl on 2018/3/7.
 * @desc KnowFragment
 */

class KnowFragment : BaseRxFragment<KnowPresenter>(), BaseRecycleView<HomeData>, FavView, BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        private const val BUNDLE_DATA = "bundle_data"
        private const val BUNDLE_PAGE_INDEX = "bundle_page_index"

        fun newInstance(cid: Int): KnowFragment {
            val knowFragment = KnowFragment()
            val bundle = Bundle()
            bundle.putInt("cid", cid)
            knowFragment.arguments = bundle
            return knowFragment
        }
    }

    private var cid: Int = 0

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
        mActivityComponent.getKnowRepertoryComponent(KnowRepertoryModule()).inject(this)
        mActivityComponent.getCollectionRepertoryComponent(CollectionRepertoryModule()).inject(this)
        mPresenter.mView = this
        favPresenter.mView = this
    }

    override fun getLayoutId() = R.layout.fragment_home

    override fun initView(savedInstanceState: Bundle?) {

        cid = arguments?.getInt("cid") ?: 0

        with(refresh) {
            setColorSchemeResources(R.color.colorPrimary)
            onRefresh {
                isRefreshing = true
                mPresenter.getKnowData(cid)
            }
        }

        with(homeListAdapter) {
            bindToRecyclerView(recyclerview)
            setOnLoadMoreListener({
                mPresenter.getMoreKnowData(cid)
            }, recyclerview)
            setHeaderAndEmpty(true)
            emptyView = mEmptyView
            onItemChildClickListener = this@KnowFragment
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
        @Suppress("UNCHECKED_CAST")
        val list = savedInstanceState?.get(BUNDLE_DATA) as? ArrayList<HomeData>
        if (list?.isNotEmpty() == true) {
            mPresenter.setCurrentPageIndex(savedInstanceState.getInt(BUNDLE_PAGE_INDEX))
            homeListAdapter.setNewData(list)
        } else
            mPresenter.getKnowData(cid)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (homeListAdapter.data.size > 0) {
            val list = homeListAdapter.data as ArrayList<HomeData>
            outState.putSerializable(BUNDLE_DATA, list)
            outState.putInt(BUNDLE_PAGE_INDEX, mPresenter.getCurrentPageIndex())
        }
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