package com.sjkj.wanandroid.rxhelper

import com.blankj.utilcode.util.NetworkUtils
import com.orhanobut.logger.Logger
import com.sjkj.wanandroid.mvp.BaseView
import com.sjkj.wanandroid.rxhelper.exception.RequestFailedException
import com.sjkj.wanandroid.rxhelper.exception.RequestIllegalArgumentException
import com.sjkj.wanandroid.rxhelper.exception.RequestNetWorkException
import com.sjkj.wanandroid.rxhelper.exception.RequestNullDataException
import rx.Subscriber
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @author by dingl on 2017/9/12.
 * *
 * @desc RxSubscriber
 */
abstract class RxSubscriber<T>(private val mBaseView: BaseView) : Subscriber<T>() {

    override fun onStart() {
        mBaseView.showLoading()
        if (!NetworkUtils.isConnected()) {
            onError(RequestNetWorkException("暂无网络，请连接网络后再试!"))
        }
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        Logger.e(e.message)
        mBaseView.hideLoading()
        val msg: String
        when (e) {
            is RequestNullDataException -> {
                _onEmpty()
                return
            }
            is RequestNetWorkException -> {
                msg = "暂无网络，请连接网络后再试!"
                mBaseView.showNetError()
            }
            is RequestFailedException -> {
                msg = e.message as String
                mBaseView.showLoadError()
            }
            is RequestIllegalArgumentException -> {
                msg = e.message as String
                mBaseView.showLoadError()
            }
            is UnknownHostException -> {
                msg = "暂无网络，请连接网络后再试!"
                mBaseView.showNetError()
            }
            is SocketTimeoutException -> {
                msg = "请求超时，请稍后重试..."
                mBaseView.showLoadError()
            }
            else -> {
                msg = "请求失败，请稍后重试..."
                mBaseView.showLoadError()
            }
        }
        _onError(msg)
    }

    open fun _onEmpty() {
        mBaseView.loadEnd()
        mBaseView.showEmptyView()
    }

    open fun _onError(toast: String) {
        mBaseView.showToast(toast)
        unsubscribe()
        mBaseView.loadError()
    }

    override fun onNext(t: T) {
        mBaseView.hideLoading()
        _onNext(t)
        unsubscribe()
    }

    abstract fun _onNext(t: T)

}
