package com.sjkj.wanandroid.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sjkj.wanandroid.R
import com.sjkj.wanandroid.bean.HomeData
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * @author by dingl on 2018/2/28.
 * @desc HomeListAdapter
 */
class HomeListAdapter(layoutId: Int = 0,
                      private val itemClick: (String) -> Unit)
    : BaseQuickAdapter<HomeData, BaseViewHolder>(if (layoutId == 0) R.layout.adapter_home_list else layoutId) {

    override fun convert(helper: BaseViewHolder, item: HomeData?) {
        item ?: return
        with(item) {
            helper.setText(R.id.homeItemAuthor, author)
                    .setText(R.id.homeItemDate, niceDate)
                    .setText(R.id.homeItemChapter, chapterName)
                    .setText(R.id.homeItemContent, title)
                    .addOnClickListener(R.id.homeItemLike)
                    .setImageResource(R.id.homeItemLike,
                            if (collect) {
                                R.drawable.ic_action_like
                            } else
                                R.drawable.ic_action_no_like
                    )
                    .itemView.onClick { itemClick(link) }
        }
    }
}

