package com.sjkj.wanandroid.ui.activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import com.sjkj.wanandroid.R
import com.sjkj.wanandroid.base.BaseActivity
import com.sjkj.wanandroid.common.Common
import com.sjkj.wanandroid.extensions.getInteger
import com.sjkj.wanandroid.extensions.setVisible
import com.sjkj.wanandroid.utils.AnimUtils
import com.sjkj.wanandroid.utils.PreferenceUtils
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * @author by dingl on 2018/3/5.
 * @desc SplashActivity
 */
class SplashActivity : BaseActivity() {

    private var userName by PreferenceUtils(Common.USERNAME_KEY, "")

    override fun initView(savedInstanceState: Bundle?) {
        setUpLayout()
        textView.text = userName
    }

    private fun setUpLayout() {
        val alphaAnimatorTextView = ObjectAnimator.ofFloat(textView, "alpha", 0f, .5f, 1f)
        val alphaAnimatorImageView = ObjectAnimator.ofFloat(imageView, "alpha", 0f, .5f, 1f)
        val animatorSet = AnimatorSet()
        animatorSet.play(alphaAnimatorImageView).before(alphaAnimatorTextView)
        animatorSet.duration = 500
        animatorSet.start()
        animatorSet.addListener(object : AnimUtils.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animator: Animator?) {
                animatorReveal(root)
            }
        })
    }

    override fun getLayoutId(): Int = R.layout.activity_splash

    private fun animatorReveal(view: View) {
        val width = (view.left + view.right) / 2
        val height = (view.top + view.bottom) / 2
        val radius = Math.max(view.width, view.height)
        val circularReveal = ViewAnimationUtils.createCircularReveal(view, width, height, radius.toFloat(), 0f)
        textView.setVisible(false)
        imageView.setVisible(false)
        with(circularReveal) {
            duration = getInteger(R.integer.transition_duration)
            start()
            addListener(object : AnimUtils.AnimatorListenerAdapter() {
                override fun onAnimationEnd(animator: Animator?) {
                    super.onAnimationEnd(animator)
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }
            })
        }
    }
}