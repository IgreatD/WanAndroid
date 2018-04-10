package com.sjkj.wanandroid.ui.activity

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.annotation.TransitionRes
import android.support.v4.util.Pair
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.StyleSpan
import android.transition.Transition
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.util.SparseArray
import android.view.View
import android.view.ViewStub
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.qmuiteam.qmui.util.QMUIKeyboardHelper
import com.sjkj.wanandroid.R
import com.sjkj.wanandroid.base.BaseRxActivity
import com.sjkj.wanandroid.bean.HomeData
import com.sjkj.wanandroid.common.Common
import com.sjkj.wanandroid.di.module.CollectionRepertoryModule
import com.sjkj.wanandroid.di.module.SearchRepertoryModule
import com.sjkj.wanandroid.extensions.transitionTo
import com.sjkj.wanandroid.mvp.BaseRecycleView
import com.sjkj.wanandroid.ui.adapter.HomeListAdapter
import com.sjkj.wanandroid.ui.persenter.FavPresenter
import com.sjkj.wanandroid.ui.persenter.SearchPresenter
import com.sjkj.wanandroid.ui.view.FavView
import com.sjkj.wanandroid.ui.widght.CustomDividerItemDecoration
import com.sjkj.wanandroid.ui.widght.ElasticDragDismissFrameLayout
import com.sjkj.wanandroid.utils.AnimUtils
import com.sjkj.wanandroid.utils.ImeUtils
import com.sjkj.wanandroid.utils.PreferenceUtils
import com.sjkj.wanandroid.utils.TransitionUtils
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import javax.inject.Inject


/**
 * @author by dingl on 2018/3/5.
 * @desc SearchActivity
 */
class SearchActivity : BaseRxActivity<SearchPresenter>(), FavView, BaseRecycleView<HomeData>, BaseQuickAdapter.OnItemChildClickListener {

    companion object {

        const val EXTRA_SEARCH = "extra_search"
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
                            favPresenter.toggleCollectionArticle(homeData.collect, homeData.id, position)
                        }
                    })
                } else {
                    val intent = Intent(this, LoginActivity::class.java)
                    transitionTo(intent, Pair(view, getString(R.string.dn_login_action)))
                }
            }
        }
    }

    private val transitions = SparseArray<Transition>()

    private var noResults: TextView? = null

    override fun getLayoutId() = R.layout.activity_search

    private var query = ""

    private var isLogin by PreferenceUtils(Common.LOGIN_KEY, false)

    @Inject
    lateinit var favPresenter: FavPresenter

    private val searchAdapter: HomeListAdapter by lazy {
        HomeListAdapter(R.layout.adapter_search_list) {
            startBrowserActivity(it)
        }
    }

    private fun startBrowserActivity(it: String) {
        val intent = Intent(this, BrowserActivity::class.java)
        intent.putExtra(BrowserActivity.PARAM_URL, it)
        transitionTo(intent)
    }

    override fun setNewData(t: List<HomeData>) {
        if (t.isNotEmpty()) {
            if (search_results.visibility != View.VISIBLE) {
                TransitionManager.beginDelayedTransition(root,
                        getTransition(R.transition.search_show_results))
                loading_progress.visibility = View.GONE
                search_results.visibility = View.VISIBLE
            }
            searchAdapter.setNewData(t)
        } else {
            TransitionManager.beginDelayedTransition(
                    root, getTransition(R.transition.auto))
            loading_progress.visibility = View.GONE
            setNoResultsVisibility(View.VISIBLE)
        }
    }

    override fun setMoreData(t: List<HomeData>) {
        searchAdapter.addData(t)
    }

    override fun loadEnd() {
        searchAdapter.loadMoreEnd()
    }

    override fun loadError() {
        searchAdapter.loadMoreFail()
    }

    override fun loadComplete() {
        searchAdapter.loadMoreComplete()
    }

    override fun injectComponent() {
        mActivityComponent.getSearchRepertoryComponent(SearchRepertoryModule()).inject(this)
        mActivityComponent.getCollectionRepertoryComponent(CollectionRepertoryModule()).inject(this)
        mPresenter.mView = this
        favPresenter.mView = this
    }

    override fun initView(savedInstanceState: Bundle?) {
        searchback.onClick {
            searchback.background = null
            finishAfterTransition()
        }
        setupSearchView()
        setupTransitions()
        root.addListener(
                object : ElasticDragDismissFrameLayout.SystemChromeFader(this) {
                    override fun onDragDismissed() {
                        if (root.translationY > 0) {
                            window.returnTransition = TransitionInflater.from(this@SearchActivity)
                                    .inflateTransition(R.transition.about_return_downward)
                        }
                        finishAfterTransition()
                    }
                })
        scrim.onClick {
            searchback.background = null
            finishAfterTransition()
        }

        setupIntent(intent)
    }

    private fun setupIntent(intent: Intent?) {
        query = intent?.extras?.getString(EXTRA_SEARCH) ?: ""
    }

    private fun setupTransitions() {
        window.enterTransition.addListener(object : TransitionUtils.TransitionListenerAdapter() {
            override fun onTransitionEnd(transition: Transition?) {
                super.onTransitionEnd(transition)
                search_view.requestFocus()
                if (query.isNotEmpty()) {
                    search_view.setQuery(query, true)
                    searchFor(query)
                }
            }
        })
    }

    private fun setupSearchView() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        with(search_view) {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            queryHint = getString(R.string.search_hint)
            inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
            imeOptions = imeOptions or EditorInfo.IME_ACTION_SEARCH or
                    EditorInfo.IME_FLAG_NO_EXTRACT_UI or EditorInfo.IME_FLAG_NO_FULLSCREEN

            val id = resources.getIdentifier("android:id/search_src_text", null, null)

            val textView = findViewById<TextView>(id)

            textView.setTextColor(Color.WHITE)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    searchFor(query)
                    return true
                }

                override fun onQueryTextChange(query: String): Boolean {
                    if (TextUtils.isEmpty(query)) {
                        clearResults()
                    }
                    return true
                }
            })
        }

        with(search_results) {
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
            addItemDecoration(CustomDividerItemDecoration(this@SearchActivity))
        }
        with(searchAdapter) {
            setOnLoadMoreListener({ mPresenter.getMoreSearchData(query) }, search_results)
            onItemChildClickListener = this@SearchActivity
        }
    }

    private fun searchFor(query: String) {
        this.query = query
        clearResults()
        QMUIKeyboardHelper.hideKeyboard(search_view)
        search_view.clearFocus()
        loading_progress.visibility = View.VISIBLE
        mPresenter.getSearchData(query)
    }

    private fun clearResults() {
        searchAdapter.setNewData(null)
        TransitionManager.beginDelayedTransition(root, getTransition(R.transition.auto))
        loading_progress.visibility = View.GONE
        setNoResultsVisibility(View.GONE)
    }

    private fun getTransition(@TransitionRes transitionId: Int): Transition? {
        var transition: Transition? = transitions.get(transitionId)
        if (transition == null) {
            transition = TransitionInflater.from(this).inflateTransition(transitionId)
            transitions.put(transitionId, transition)
        }
        return transition
    }

    private fun setNoResultsVisibility(visibility: Int) {
        if (visibility == View.VISIBLE) {
            if (noResults == null) {
                noResults = (findViewById<View>(R.id.stub_no_search_results) as ViewStub).inflate() as TextView
                noResults?.setOnClickListener({
                    search_view.setQuery("", false)
                    search_view.requestFocus()
                    ImeUtils.showIme(search_view)
                })
            }
            val message = String.format(getString(R.string.no_search_results), search_view.query.toString())
            val ssb = SpannableStringBuilder(message)
            ssb.setSpan(StyleSpan(Typeface.ITALIC),
                    message.indexOf('“') + 1,
                    message.length - 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            noResults?.text = ssb
        }
        noResults?.visibility = visibility
    }

    override fun favSuccess(position: Int) {
        val data = searchAdapter.data[position]
        data.collect = true
        searchAdapter.setData(position, data)
    }

    override fun favFailed() {

    }

    override fun removeFavSuccess(position: Int) {
        val data = searchAdapter.data[position]
        data.collect = false
        searchAdapter.setData(position, data)
    }

    override fun removeFavFailed() {

    }

}