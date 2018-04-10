package com.sjkj.wanandroid.extensions

import android.app.Activity
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import com.just.agentweb.AgentWeb
import com.just.agentweb.ChromeClientCallbackManager
import com.sjkj.wanandroid.BaseApp
import com.sjkj.wanandroid.utils.PreferenceUtils

/**
 * @author by dingl on 2018/1/15.
 * @desc ContextExtensions
 */
fun getApp() = BaseApp.instance

fun getString(resId: Int): String = getApp().getString(resId)

fun getColorWithExtension(resId: Int) = ContextCompat.getColor(getApp(), resId)

fun getStringArray(resId: Int): Array<out String> = getApp().resources.getStringArray(resId)

fun getInteger(resId: Int): Long = getApp().resources.getInteger(resId).toLong()

fun getRandomColor(): String = "#${Integer.toHexString((Math.random() * 16777215).toInt())}"

fun AppCompatActivity.setToolBar(toolbar: Toolbar, titleString: String?) {
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setDisplayShowTitleEnabled(false)
    toolbar.title = titleString
}

fun View.setVisible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun SwipeRefreshLayout.dismiss() {
    postDelayed({ isRefreshing = false }, 500)
}

@Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
fun saveCookie(url: String?, domain: String?, cookies: String) {
    url ?: return
    var spUrl: String by PreferenceUtils(url, cookies)
    @Suppress("UNUSED_VALUE")
    spUrl = cookies
    domain ?: return
    var spDomain: String by PreferenceUtils(domain, cookies)
    @Suppress("UNUSED_VALUE")
    spDomain = cookies
}

fun encodeCookie(cookies: List<String>): String {
    val sb = StringBuilder()
    val set = HashSet<String>()
    cookies
            .map { cookie ->
                cookie.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            }
            .forEach {
                it.filterNot { set.contains(it) }.forEach { set.add(it) }
            }

    val ite = set.iterator()
    while (ite.hasNext()) {
        val cookie = ite.next()
        sb.append(cookie).append(";")
    }

    val last = sb.lastIndexOf(";")
    if (sb.length - 1 == last) {
        sb.deleteCharAt(last)
    }

    return sb.toString()
}

fun String.getAgentWeb(
        activity: Activity, webContent: ViewGroup,
        layoutParams: ViewGroup.LayoutParams,
        receivedTitleCallback: ChromeClientCallbackManager.ReceivedTitleCallback?
) = AgentWeb.with(activity)//传入Activity or Fragment
        .setAgentWebParent(webContent, layoutParams)//传入AgentWeb 的父控件
        .useDefaultIndicator()// 使用默认进度条
        .defaultProgressBarColor() // 使用默认进度条颜色
        .setReceivedTitleCallback(receivedTitleCallback) //设置 Web 页面的 title 回调
        .createAgentWeb()//
        .ready()
        .go(this)!!
