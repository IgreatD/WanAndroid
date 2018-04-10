package com.sjkj.wanandroid.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.youth.banner.loader.ImageLoader

/**
 * @author by dingl on 2018/3/1.
 * @desc GlideImageLoader
 */
class GlideImageLoader : ImageLoader() {

    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
        context ?: return
        imageView ?: return
        Glide.with(context)
                .load(path)
                .into(imageView)
    }
}