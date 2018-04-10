package com.sjkj.wanandroid.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.sjkj.wanandroid.R
import com.sjkj.wanandroid.bean.HotTagBean

/**
 * @author by dingl on 2018/3/6.
 * @desc HotTagAdapter
 */
class HotTagAdapter(private val itemClick: (Int, String, View) -> Unit) : BaseQuickAdapter<HotTagBean, BaseViewHolder>(R.layout.adapter_hot_tag_list) {
    override fun convert(helper: BaseViewHolder, item: HotTagBean?) {
        item ?: return
        helper.setText(R.id.hotItemTag, item.name)
        val hotItemRecyclerView = helper.getView<RecyclerView>(R.id.hotItemRecyclerView)
        with(hotItemRecyclerView) {
            layoutManager = FlexboxLayoutManager(mContext, FlexDirection.ROW)
            adapter = HotContentAdapter(item.tag, item.content.data, itemClick)
        }
    }
}