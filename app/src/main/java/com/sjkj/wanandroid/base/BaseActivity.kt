package com.sjkj.wanandroid.base

import android.os.Bundle
import me.yokeyword.fragmentation.SupportActivity

/**
 * @author by dingl on 2018/1/19.
 * @desc BaseActivity
 */
abstract class BaseActivity : SupportActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initView(savedInstanceState)
    }

    abstract fun initView(savedInstanceState: Bundle?)

    abstract fun getLayoutId(): Int

    override fun onBackPressedSupport() {
        super.onBackPressedSupport()
        finishAfterTransition()
    }

}
