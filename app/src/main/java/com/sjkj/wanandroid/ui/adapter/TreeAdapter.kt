package com.sjkj.wanandroid.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sjkj.wanandroid.R
import com.sjkj.wanandroid.bean.TreeBean
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * @author by dingl on 2018/3/1.
 * @desc TreeAdapter
 */
class TreeAdapter(private val itemClick: (String, String, String) -> Unit) : BaseQuickAdapter<TreeBean, BaseViewHolder>(R.layout.adapter_know_list) {

    override fun convert(helper: BaseViewHolder, item: TreeBean?) {
        item ?: return
        with(item) {
            helper.setText(R.id.knowItemTitle, name)
                    .setText(R.id.knowItemContent, children.joinToString("    ") { it.name })
                    .itemView.onClick {
                itemClick(name, children.joinToString(",") { it.id.toString() },
                        children.joinToString(",") { it.name })
            }
        }
    }

}