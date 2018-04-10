package com.sjkj.wanandroid.utils

import android.app.Activity
import android.support.v4.util.Pair
import android.view.View
import java.util.*


class TransitionHelper {

    companion object {
        val instance by lazy { TransitionHelper() }
    }

    fun createSafeTransitionPair(
            activity: Activity,
            includeStatusBar: Boolean,
            vararg otherPair: Pair<View, String>): Array<Pair<View, String>> {
        val decorView = activity.window.decorView
        var statusBar: View? = null
        if (includeStatusBar) {
            statusBar = decorView.findViewById(android.R.id.statusBarBackground)
        }
        val navBar = decorView.findViewById<View>(android.R.id.navigationBarBackground)
        val pair = ArrayList<Pair<View, String>>(3)
        addNonNullViewToTransitionParticipants(statusBar, pair)
        addNonNullViewToTransitionParticipants(navBar, pair)
        pair.addAll(otherPair)
        return pair.toTypedArray()
    }

    private fun addNonNullViewToTransitionParticipants(view: View?, participants: MutableList<Pair<View, String>>) {
        if (view == null) {
            return
        }
        participants.add(Pair(view, view.transitionName))
    }
}
