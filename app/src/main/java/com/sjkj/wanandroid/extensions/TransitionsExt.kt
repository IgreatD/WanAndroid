package com.sjkj.wanandroid.extensions

import android.content.Intent
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.sjkj.wanandroid.utils.TransitionHelper
import org.jetbrains.anko.act
import org.jetbrains.anko.support.v4.act

/**
 * @author by dingl on 2018/3/6.
 * @desc TransitionsExt
 */
fun AppCompatActivity.transitionTo(intent: Intent, vararg otherPair: Pair<View, String>) {
    val pairs = TransitionHelper.instance.createSafeTransitionPair(this, false, *otherPair)
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, *pairs)
    startActivity(intent, options.toBundle())
}

fun AppCompatActivity.transitionToResult(intent: Intent, resultCode: Int, vararg otherPair: Pair<View, String>) {
    val pairs = TransitionHelper.instance.createSafeTransitionPair(this, false, *otherPair)
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, *pairs)
    act.startActivityForResult(intent, resultCode, options.toBundle())
}

fun Fragment.transitionTo(intent: Intent, vararg otherPair: Pair<View, String>) {
    val pairs = TransitionHelper.instance.createSafeTransitionPair(act, false, *otherPair)
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(act, *pairs)
    startActivity(intent, options.toBundle())
}

fun Fragment.transitionToResult(intent: Intent, resultCode: Int, vararg otherPair: Pair<View, String>) {
    val pairs = TransitionHelper.instance.createSafeTransitionPair(act, false, *otherPair)
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(act, *pairs)
    act.startActivityForResult(intent, resultCode, options.toBundle())
}