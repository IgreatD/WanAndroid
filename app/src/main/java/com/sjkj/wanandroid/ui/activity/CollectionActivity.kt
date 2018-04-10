package com.sjkj.wanandroid.ui.activity

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.os.Bundle
import android.support.v4.util.Pair
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.MenuItem
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.sjkj.wanandroid.R
import com.sjkj.wanandroid.base.BaseRxActivity
import com.sjkj.wanandroid.bean.HomeData
import com.sjkj.wanandroid.common.Common
import com.sjkj.wanandroid.di.module.CollectionRepertoryModule
import com.sjkj.wanandroid.extensions.setToolBar
import com.sjkj.wanandroid.extensions.transitionTo
import com.sjkj.wanandroid.extensions.transitionToResult
import com.sjkj.wanandroid.mvp.BaseRecycleView
import com.sjkj.wanandroid.ui.adapter.HomeListAdapter
import com.sjkj.wanandroid.ui.persenter.CollectionPresenter
import com.sjkj.wanandroid.ui.persenter.FavPresenter
import com.sjkj.wanandroid.ui.view.FavView
import com.sjkj.wanandroid.ui.widght.ElasticDragDismissFrameLayout
import com.sjkj.wanandroid.utils.AnimUtils
import com.sjkj.wanandroid.utils.PreferenceUtils
import com.sjkj.wanandroid.utils.TransitionUtils
import kotlinx.android.synthetic.main.activity_collection.*
import javax.inject.Inject

/**
 * @author by dingl on 2018/3/6.
 * @desc CollectionActivity
 */
class CollectionActivity :
        BaseRxActivity<CollectionPresenter>(),
        BaseRecycleView<HomeData>, FavView,
        BaseQuickAdapter.OnItemChildClickListener {

    override fun getLayoutId(): Int = R.layout.activity_collection

    private val isLogin by PreferenceUtils(Common.LOGIN_KEY, false)

    private val collectionAdapter by lazy {
        HomeListAdapter {
            startBrowserActivity(it)
        }
    }

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
                            mFavPresenter.toggleCollectionArticle(homeData.collect, homeData.id, position)
                        }
                    })
                } else {
                    val intent = Intent(this, LoginActivity::class.java)
                    transitionToResult(intent, Common.LOGIN_RESULT_CODE, Pair(view, getString(R.string.dn_login_action)))
                }
            }
        }
    }

    @Inject
    lateinit var mFavPresenter: FavPresenter

    override fun setNewData(t: List<HomeData>) {
        collectionAdapter.setNewData(t)
    }

    override fun setMoreData(t: List<HomeData>) {
        collectionAdapter.addData(t)
    }

    override fun loadEnd() {
        collectionAdapter.loadMoreEnd()
    }

    override fun loadError() {
        collectionAdapter.loadMoreFail()
    }

    override fun loadComplete() {
        collectionAdapter.loadMoreComplete()
    }

    override fun injectComponent() {
        mActivityComponent.getCollectionRepertoryComponent(CollectionRepertoryModule()).inject(this)
        mPresenter.mView = this
        mFavPresenter.mView = this
    }

    override fun favSuccess(position: Int) {
        val data = collectionAdapter.data[position]
        data.collect = true
        collectionAdapter.setData(position, data)
    }

    override fun favFailed() {
    }

    override fun removeFavSuccess(position: Int) {
        val data = collectionAdapter.data[position]
        data.collect = false
        collectionAdapter.setData(position, data)
    }

    override fun removeFavFailed() {
    }

    override fun initView(savedInstanceState: Bundle?) {
        setToolBar(toolbar, getString(R.string.my_like))
        with(collectionAdapter) {
            setOnLoadMoreListener({
                mPresenter.getCollectionMoreData()
            }, recyclerview)
            setHeaderAndEmpty(true)
            setHeaderAndEmpty(true)
            emptyView = mEmptyView
            onItemChildClickListener = this@CollectionActivity
        }

        recyclerview.apply {
            adapter = collectionAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        setupTransitions()

        draglayout.addListener(object : ElasticDragDismissFrameLayout.SystemChromeFader(this) {
            override fun onDragDismissed() {
                super.onDragDismissed()
                if (draglayout.translationY > 0) {
                    window.returnTransition = TransitionInflater.from(this@CollectionActivity)
                            .inflateTransition(R.transition.about_return_downward)
                }
                finishAfterTransition()
            }
        })

        with(mEmptyView) {
            setLoadingShowing(true)
        }
    }

    private fun setupTransitions() {
        window.enterTransition.addListener(object : TransitionUtils.TransitionListenerAdapter() {
            override fun onTransitionEnd(transition: Transition?) {
                super.onTransitionEnd(transition)
                mPresenter.getCollectionData()
            }
        })
    }

    private fun startBrowserActivity(it: String) {
        val intent = Intent(this, BrowserActivity::class.java)
        intent.putExtra(BrowserActivity.PARAM_URL, it)
        transitionTo(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedSupport()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}