package com.sjkj.wanandroid.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.util.Pair
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.transition.TransitionInflater
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.sjkj.wanandroid.R
import com.sjkj.wanandroid.base.BaseActivity
import com.sjkj.wanandroid.common.Common
import com.sjkj.wanandroid.extensions.setToolBar
import com.sjkj.wanandroid.extensions.transitionTo
import com.sjkj.wanandroid.ui.fragment.HomeFragment
import com.sjkj.wanandroid.ui.fragment.HotFragment
import com.sjkj.wanandroid.ui.fragment.TreeFragment
import com.sjkj.wanandroid.ui.fragment.MineFragment
import com.sjkj.wanandroid.utils.PreferenceUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import me.yokeyword.fragmentation.SupportFragment
import org.jetbrains.anko.find
import org.jetbrains.anko.toast

class MainActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_main

    private var isLogin by PreferenceUtils(Common.LOGIN_KEY, false)

    private var username by PreferenceUtils(Common.USERNAME_KEY, "")

    private val mFragments = arrayOfNulls<SupportFragment>(4)

    private lateinit var tvName: TextView

    override fun initView(savedInstanceState: Bundle?) {
        setToolBar(toolbar, getString(R.string.tab_home))
        drawerLayout.run {
            val toggle = ActionBarDrawerToggle(
                    this@MainActivity,
                    this,
                    toolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close
            )
            addDrawerListener(toggle)
            toggle.syncState()
        }
        setLayout()
        setupEnterTransitions()
    }

    private fun setupEnterTransitions() {
        val transition = TransitionInflater.from(this).inflateTransition(R.transition.change_bounds)
        window.sharedElementEnterTransition = transition
    }

    private fun setLayout() {
        val findFragment = findFragment(HomeFragment::class.java)
        if (findFragment == null) {
            mFragments[FragmentPosition.FIRST.getPosition()] = HomeFragment()
            mFragments[FragmentPosition.SECOND.getPosition()] = TreeFragment()
            mFragments[FragmentPosition.THIRD.getPosition()] = MineFragment()
            mFragments[FragmentPosition.FOUTH.getPosition()] = HotFragment()
            loadMultipleRootFragment(R.id.content, FragmentPosition.FIRST.getPosition(),
                    mFragments[FragmentPosition.FIRST.getPosition()],
                    mFragments[FragmentPosition.SECOND.getPosition()],
                    mFragments[FragmentPosition.THIRD.getPosition()],
                    mFragments[FragmentPosition.FOUTH.getPosition()]
            )
        } else {
            mFragments[FragmentPosition.FIRST.getPosition()] = findFragment
            mFragments[FragmentPosition.SECOND.getPosition()] = findFragment(TreeFragment::class.java)
            mFragments[FragmentPosition.THIRD.getPosition()] = findFragment(MineFragment::class.java)
            mFragments[FragmentPosition.FOUTH.getPosition()] = findFragment(HotFragment::class.java)
        }

        bottomBar.setOnTabSelectListener {
            val position = when (it) {
                R.id.tab_home -> {
                    toolbar.setTitle(R.string.tab_home)
                    FragmentPosition.FIRST.getPosition()
                }
                R.id.tab_know -> {
                    toolbar.setTitle(R.string.tab_know)
                    FragmentPosition.SECOND.getPosition()
                }
                R.id.tab_hot -> {
                    toolbar.setTitle(R.string.hot_title)
                    FragmentPosition.FOUTH.getPosition()
                }
                R.id.tab_mine -> {
                    toolbar.setTitle(R.string.tab_me)
                    FragmentPosition.THIRD.getPosition()
                }
                else -> throw IllegalStateException("Incorrect position")
            }
            showHideFragment(mFragments[position])
        }

        navigationView.setNavigationItemSelectedListener({ item ->
            drawerLayout.closeDrawer(Gravity.START)
            when (item.itemId) {
                R.id.nav_like -> {
                    val intent = Intent(this, CollectionActivity::class.java)
                    transitionTo(intent)
                }
                R.id.nav_about -> {
                    val intent = Intent(this, AboutActivity::class.java)
                    transitionTo(intent)
                }
            }
            true
        })

        tvName = navigationView.getHeaderView(0).find(R.id.textView)

        if (isLogin) {
            tvName.text = username
        }

    }

    enum class FragmentPosition(private val position: Int) {
        FIRST(0), SECOND(1), THIRD(2), FOUTH(3);

        fun getPosition() = position
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val searchView = toolbar.findViewById<View>(R.id.menuSearch)
        when (item.itemId) {
            R.id.menuSearch -> {
                transitionTo(Intent(this, SearchActivity::class.java), Pair(searchView, getString(R.string.transition_search_back)))
            }
            android.R.id.home ->
                if (drawerLayout.isDrawerOpen(Gravity.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else
                    drawerLayout.openDrawer(Gravity.START)
        }
        return super.onOptionsItemSelected(item)
    }

    private var lastTime: Long = 0

    override fun onBackPressedSupport() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
            return
        }
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTime < 2 * 1000) {
            super.onBackPressedSupport()
            finish()
        } else {
            toast(getString(R.string.double_click_exit))
            lastTime = currentTime
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Common.LOGIN_RESULT_CODE) {
            tvName.text = username
        }
    }

}
