package com.sjkj.wanandroid.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.util.Pair
import android.transition.Slide
import android.transition.Transition
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.sjkj.wanandroid.R
import com.sjkj.wanandroid.base.BaseActivity
import com.sjkj.wanandroid.extensions.setToolBar
import com.sjkj.wanandroid.extensions.transitionTo
import com.sjkj.wanandroid.ui.fragment.KnowFragment
import com.sjkj.wanandroid.utils.TransitionUtils
import kotlinx.android.synthetic.main.activity_know.*

/**
 * @author by dingl on 2018/3/7.
 * @desc KnowActivity
 */
class KnowActivity : BaseActivity() {

    private lateinit var ids: String

    private lateinit var tabs: String

    override fun getLayoutId(): Int = R.layout.activity_know

    companion object {
        const val EXTRA_IDS = "extra_ids"
        const val EXTRA_TABS = "extra_tabs"
        const val EXTRA_TITLE = "extra_title"
    }

    override fun initView(savedInstanceState: Bundle?) {
        setToolBar(toolbar, intent.extras.getString(EXTRA_TITLE))
        ids = intent.extras.getString(EXTRA_IDS)
        tabs = intent.extras.getString(EXTRA_TABS)
        if (ids.isNotEmpty() && tabs.isNotEmpty() && ids.split(",").size == tabs.split(",").size) {
            setupLayout()
            setupTransitions()
        }
    }

    private fun setupTransitions() {
        val slideTransition = Slide(Gravity.END)
        window.enterTransition = slideTransition
        slideTransition.addListener(object : TransitionUtils.TransitionListenerAdapter() {
            override fun onTransitionEnd(transition: Transition?) {
                transition?.removeListener(this)
                with(knowViewPager) {
                    adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
                        override fun getItem(position: Int): Fragment {
                            return KnowFragment.newInstance(ids.split(",")[position].toInt())
                        }

                        override fun getCount(): Int = tabs.split(",").size

                        override fun getPageTitle(position: Int): CharSequence? {
                            return tabs.split(",")[position]
                        }

                    }
                }
            }
        })
    }

    private fun setupLayout() {
        with(knowTabLayout) {
            setupWithViewPager(knowViewPager)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finishAfterTransition()
            R.id.menuSearch -> {
                val searchView = toolbar.findViewById<View>(R.id.menuSearch)
                transitionTo(Intent(this, SearchActivity::class.java), Pair(searchView, getString(R.string.transition_search_back)))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

}
