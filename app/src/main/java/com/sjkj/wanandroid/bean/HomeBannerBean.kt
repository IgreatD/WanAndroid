package com.sjkj.wanandroid.bean

import java.io.Serializable
/**
 * @author by dingl on 2018/2/28.
 * @desc HomeBinnerBean
 */
data class HomeBannerBean(
        val id: Int,
        val url: String,
        val imagePath: String,
        val title: String,
        val desc: String,
        val isVisible: Int,
        val order: Int,
        val `type`: Int
): Serializable
