package com.sjkj.wanandroid.bean

/**
 * @author by dingl on 2018/3/6.
 * @desc HotBean
 */
open class HotBean(
        val id: Int,
        val name: String,
        val link: String
)

data class HotTagBean(
        val tag: Int,
        val name: String,
        val content: BaseJsonResult<List<HotBean>>
)