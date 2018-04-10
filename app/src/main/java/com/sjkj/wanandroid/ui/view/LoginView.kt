package com.sjkj.wanandroid.ui.view

import com.sjkj.wanandroid.bean.UserBean
import com.sjkj.wanandroid.mvp.BaseView

/**
 * @author by dingl on 2018/3/3.
 * @desc LoginView
 */
interface LoginView : BaseView {
    fun loginSuccess(userBean: UserBean)
    fun loginFailed()

    fun signSuccess(userBean: UserBean)
    fun signFailed()
}