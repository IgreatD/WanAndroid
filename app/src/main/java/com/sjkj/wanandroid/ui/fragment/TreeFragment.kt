package com.sjkj.wanandroid.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.sjkj.wanandroid.R
import com.sjkj.wanandroid.base.BaseRxFragment
import com.sjkj.wanandroid.bean.TreeBean
import com.sjkj.wanandroid.di.module.KnowRepertoryModule
import com.sjkj.wanandroid.extensions.dismiss
import com.sjkj.wanandroid.extensions.transitionTo
import com.sjkj.wanandroid.mvp.BaseRecycleView
import com.sjkj.wanandroid.ui.activity.KnowActivity
import com.sjkj.wanandroid.ui.adapter.TreeAdapter
import com.sjkj.wanandroid.ui.persenter.TreePresenter
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.support.v4.onRefresh

/**
 * @author by dingl on 2018/2/28.
 * @desc TreeFragment
 */
class TreeFragment : BaseRxFragment<TreePresenter>(), BaseRecycleView<TreeBean> {

    override fun setNewData(t: List<TreeBean>) {
        knowListAdapter.setNewData(t)
    }

    override fun setMoreData(t: List<TreeBean>) {
    }

    override fun loadEnd() {
        refresh.dismiss()
        knowListAdapter.loadMoreEnd()
    }

    override fun loadError() {
        knowListAdapter.loadMoreFail()
        refresh.dismiss()
    }

    override fun loadComplete() {
        knowListAdapter.loadMoreComplete()
    }

    override fun injectComponent() {
        mActivityComponent.getKnowRepertoryComponent(KnowRepertoryModule()).inject(this)
        mPresenter.mView = this
    }

    override fun getLayoutId() = R.layout.fragment_home

    private val knowListAdapter by lazy {
        TreeAdapter { title, ids, tabs ->
            val intent = Intent(context, KnowActivity::class.java)
            intent.putExtra(KnowActivity.EXTRA_IDS, ids)
            intent.putExtra(KnowActivity.EXTRA_TABS, tabs)
            intent.putExtra(KnowActivity.EXTRA_TITLE, title)
            transitionTo(intent)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {

        with(refresh) {
            setColorSchemeResources(R.color.colorPrimary)
            onRefresh {
                isRefreshing = true
                mPresenter.getTreeData()
            }
        }

        with(knowListAdapter) {
            setOnLoadMoreListener({
                mPresenter.getTreeData()
            }, recyclerview)
            setHeaderAndEmpty(true)
            emptyView = mEmptyView
        }

        recyclerview.apply {
            adapter = knowListAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        with(mEmptyView) {
            setLoadingShowing(true)
        }

    }


    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        mPresenter.getTreeData()
    }
}
