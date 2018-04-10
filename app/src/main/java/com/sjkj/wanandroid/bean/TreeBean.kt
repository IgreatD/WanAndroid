package com.sjkj.wanandroid.bean

/**
 * @author by dingl on 2018/3/1.
 * @desc TreeBean
 */
data class TreeBean(
        val id: Int,
        val name: String,
        val courseId: Int,
        val parentChapterId: Int,
        val order: Int,
        val visible: Int,
        val children: List<KnowBean>
)

data class KnowBean(
        val id: Int,
        val name: String,
        val courseId: Int,
        val parentChapterId: Int,
        val order: Int,
        val visible: Int
)
