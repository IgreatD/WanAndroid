package com.sjkj.wanandroid.bean

import java.io.Serializable

/**
 * @author by dingl on 2018/2/28.
 * @desc HomeListBean
 */
data class HomeListBean(
        val curPage: Int,
        var datas: List<HomeData>
) : Serializable

data class HomeData(
        var id: Int,
        var originId: Int,
        var title: String,
        var chapterId: Int,
        var chapterName: String?,
        var envelopePic: Any,
        var link: String,
        var author: String,
        var publishTime: Long,
        var zan: Int,
        var visible: Int,
        var niceDate: String,
        var courseId: Int,
        var collect: Boolean
) : Serializable
