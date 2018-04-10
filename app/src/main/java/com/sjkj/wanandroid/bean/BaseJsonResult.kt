package com.sjkj.wanandroid.bean

/**
 * @author by dingl on 2018/2/28.
 * @desc BaseJsonResult
 */
data class BaseJsonResult<out T>(
        val errorCode: Int,
        val errorMsg: String,
        val data: T
)
