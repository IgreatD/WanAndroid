package com.sjkj.wanandroid.bean

import com.sjkj.wanandroid.R
import com.sjkj.wanandroid.extensions.getString
import java.util.*

/**
 * @author by dingl on 2018/3/3.
 * @desc UserBean
 */
data class UserBean(
        var id: Int = 0,
        var username: String = getString(R.string.splash),
        var password: String = "",
        var icon: String = "",
        var collectIds: List<Int> = ArrayList(),
        var isLogin: Boolean = false
)