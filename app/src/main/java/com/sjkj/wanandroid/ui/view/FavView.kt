package com.sjkj.wanandroid.ui.view

import com.sjkj.wanandroid.mvp.BaseView

/**
 * @author by dingl on 2018/3/5.
 * @desc FavView
 */
interface FavView : BaseView {
    fun favSuccess(position: Int)
    fun favFailed()
    fun removeFavSuccess(position: Int)
    fun removeFavFailed()
}