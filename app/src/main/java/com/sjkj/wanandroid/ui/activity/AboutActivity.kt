package com.sjkj.wanandroid.ui.activity

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sjkj.wanandroid.R
import com.sjkj.wanandroid.base.BaseActivity
import com.sjkj.wanandroid.ui.widght.ElasticDragDismissFrameLayout
import kotlinx.android.synthetic.main.activity_about.*

/**
 * @author by dingl on 2018/3/3.
 * @desc AboutActivity
 */
class AboutActivity : BaseActivity() {
    override fun initView(savedInstanceState: Bundle?) {

        pager.adapter = object : PagerAdapter() {
            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view == `object`
            }

            override fun instantiateItem(collection: ViewGroup, position: Int): Any {
                val layout = LayoutInflater.from(collection.context).inflate(R.layout.about_libs, collection, false)
                collection.addView(layout)
                return layout
            }

            override fun getCount() = 3

            override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
                collection.removeView(view as View)
            }

        }

        pager.pageMargin = resources.getDimensionPixelSize(R.dimen.spacing_normal)
        indicator.setViewPager(pager)

        draggableFrame.addListener(
                object : ElasticDragDismissFrameLayout.SystemChromeFader(this) {
                    override fun onDragDismissed() {
                        if (draggableFrame.translationY > 0) {
                            window.returnTransition = TransitionInflater.from(this@AboutActivity)
                                    .inflateTransition(R.transition.about_return_downward)
                        }
                        finishAfterTransition()
                    }
                })

    }

    override fun getLayoutId() = R.layout.activity_about
}