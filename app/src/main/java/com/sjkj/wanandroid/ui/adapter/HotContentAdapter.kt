package com.sjkj.wanandroid.ui.adapter

import android.graphics.Color
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sjkj.wanandroid.R
import com.sjkj.wanandroid.bean.HotBean
import com.sjkj.wanandroid.extensions.getColorWithExtension
import com.sjkj.wanandroid.extensions.getRandomColor
import com.sjkj.wanandroid.ui.widght.BaselineGridTextView
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * @author by dingl on 2018/3/7.
 * @desc HotContentAdapter
 */
class HotContentAdapter(private val tag: Int, list: List<HotBean>, private val itemClick: (Int, String, View) -> Unit) : BaseQuickAdapter<HotBean, BaseViewHolder>(R.layout.adapter_hot_list, list) {
    override fun convert(helper: BaseViewHolder, item: HotBean?) {
        item ?: return
        val hotItemName = helper.getView<BaselineGridTextView>(R.id.hotItemName)
        hotItemName.text = item.name
        val color = try {
            Color.parseColor(getRandomColor())
        } catch (e: Exception) {
            e.printStackTrace()
            getColorWithExtension(R.color.colorPrimary)
        }
        hotItemName.setTextColor(color)
        helper.itemView.onClick {
            itemClick(tag, if (tag == 1) item.name else item.link, helper.itemView)
        }
    }
}