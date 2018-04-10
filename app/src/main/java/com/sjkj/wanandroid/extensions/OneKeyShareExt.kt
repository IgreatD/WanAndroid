package com.sjkj.wanandroid.extensions

import android.content.Context
import cn.sharesdk.onekeyshare.OnekeyShare
import com.sjkj.wanandroid.R

/**
 * @author by dingl on 2018/3/7.
 * @desc OneKeyShareExt
 */
fun Context.oneKeyShare(title: String = getString(R.string.app_name),
                        content: String = getString(R.string.app_name),
                        shareUrl: String) {
    OnekeyShare().apply {
        disableSSOWhenAuthorize()
        setTitle(title)
        setTitleUrl(shareUrl)
        text = content
        setUrl(shareUrl)
    }.show(this)
}