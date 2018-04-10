package com.sjkj.wanandroid.ui.activity

import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import com.just.agentweb.AgentWeb
import com.just.agentweb.ChromeClientCallbackManager
import com.sjkj.wanandroid.R
import com.sjkj.wanandroid.base.BaseActivity
import com.sjkj.wanandroid.extensions.getAgentWeb
import com.sjkj.wanandroid.extensions.getInteger
import com.sjkj.wanandroid.extensions.oneKeyShare
import com.sjkj.wanandroid.extensions.setToolBar
import kotlinx.android.synthetic.main.activity_browser.*
import org.jetbrains.anko.act


class BrowserActivity : BaseActivity() {

    private lateinit var agentWeb: AgentWeb

    private lateinit var linkUrl: String

    override fun getLayoutId(): Int = R.layout.activity_browser

    companion object {
        const val PARAM_URL = "param_url"
    }

    override fun initView(savedInstanceState: Bundle?) {
        intent.extras?.let {
            linkUrl = it.getString(PARAM_URL)
            agentWeb = linkUrl.getAgentWeb(
                    this,
                    webView,
                    LinearLayout.LayoutParams(-1, -1),
                    receivedTitleCallback
            )
        }
        setTransitions()
    }

    private fun setTransitions() {
        val enterTransition = Slide(Gravity.END)
        enterTransition.duration = getInteger(R.integer.transition_duration)
        act.window.enterTransition = enterTransition
        act.window.exitTransition = enterTransition
    }

    private val receivedTitleCallback =
            ChromeClientCallbackManager.ReceivedTitleCallback { _, title ->
                title?.let {
                    setToolBar(toolbar, it)
                }
            }


    override fun onPause() {
        agentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onResume() {
        agentWeb.webLifeCycle.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        agentWeb.webLifeCycle.onDestroy()
        super.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (agentWeb.handleKeyEvent(keyCode, event)) {
            true
        } else {
            finishAfterTransition()
            super.onKeyDown(keyCode, event)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finishAfterTransition()
            R.id.menuShare -> act.oneKeyShare(shareUrl = linkUrl)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_browser, menu)
        return super.onCreateOptionsMenu(menu)
    }


}
