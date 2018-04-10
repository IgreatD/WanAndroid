package com.sjkj.wanandroid.ui.view

import com.sjkj.wanandroid.bean.HotTagBean
import com.sjkj.wanandroid.mvp.BaseView

/**
 * @author by dingl on 2018/3/6.
 * @desc HotView
 */
interface HotView : BaseView {
    fun setHotKeyData(list: List<HotTagBean>)
}