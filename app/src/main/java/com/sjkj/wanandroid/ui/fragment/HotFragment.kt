package com.sjkj.wanandroid.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.util.Pair
import android.support.v7.widget.LinearLayoutManager
import com.sjkj.wanandroid.R
import com.sjkj.wanandroid.base.BaseRxFragment
import com.sjkj.wanandroid.bean.HotTagBean
import com.sjkj.wanandroid.di.module.HotRepertoryModule
import com.sjkj.wanandroid.extensions.dismiss
import com.sjkj.wanandroid.extensions.transitionTo
import com.sjkj.wanandroid.ui.activity.BrowserActivity
import com.sjkj.wanandroid.ui.activity.SearchActivity
import com.sjkj.wanandroid.ui.adapter.HotTagAdapter
import com.sjkj.wanandroid.ui.persenter.HotPresenter
import com.sjkj.wanandroid.ui.view.HotView
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.support.v4.onRefresh

/**
 * @author by dingl on 2018/3/6.
 * @desc HotFragment
 */
class HotFragment : BaseRxFragment<HotPresenter>(), HotView {

    override fun getLayoutId() = R.layout.fragment_home

    private val hotAdapter by lazy {
        HotTagAdapter { tag, link, view ->
            if (tag == 1) {
                val intent = Intent(context, SearchActivity::class.java)
                intent.putExtra(SearchActivity.EXTRA_SEARCH, link)
                transitionTo(intent, Pair(view, getString(R.string.transition_search_back)))
            } else {
                startBrowserActivity(link)
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        with(recyclerview) {
            layoutManager = LinearLayoutManager(context)
            adapter = hotAdapter
        }

        with(refresh) {
            setColorSchemeResources(R.color.colorPrimary)
            onRefresh {
                isRefreshing = true
                mPresenter.getHotKeyData()
            }
        }

        with(hotAdapter) {
            emptyView = mEmptyView
        }

        with(mEmptyView) {
            setLoadingShowing(true)
        }
    }

    override fun injectComponent() {
        mActivityComponent.getHotRepertoryComponent(HotRepertoryModule()).inject(this)
        mPresenter.mView = this
    }

    override fun setHotKeyData(list: List<HotTagBean>) {
        hotAdapter.setNewData(list)
    }

    override fun loadEnd() {
        refresh.dismiss()
    }

    override fun loadError() {

    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        mPresenter.getHotKeyData()
    }

    private fun startBrowserActivity(it: String) {
        val intent = Intent(context, BrowserActivity::class.java)
        intent.putExtra(BrowserActivity.PARAM_URL, it)
        transitionTo(intent)
    }
}