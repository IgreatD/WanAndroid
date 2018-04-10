package com.sjkj.wanandroid.rxhelper

import com.sjkj.wanandroid.bean.BaseJsonResult
import com.sjkj.wanandroid.rxhelper.exception.RequestFailedException
import com.sjkj.wanandroid.rxhelper.exception.RequestNullDataException
import rx.Observable

/**
 * @author by dingl on 2017/9/12.
 * *
 * @desc RxResultHelper
 */

object RxResultHelper {

    fun <T> handleResult(): Observable.Transformer<BaseJsonResult<T>, T> {

        return Observable.Transformer { tObservable ->
            tObservable.flatMap({
                when {
                    it == null -> Observable.error<T>(RequestNullDataException(it?.errorMsg
                            ?: "请求失败"))
                    it.errorCode < 0 -> Observable.error<T>(RequestFailedException(it.errorMsg))
                    it.errorCode == 0 -> {
                        if (null == it.data) {
                            Observable.error<T>(RequestNullDataException(it.errorMsg))
                        } else
                            Observable.just(it.data)
                    }
                    else -> Observable.error<T>(RequestFailedException(it.errorMsg))
                }
            })
        }

    }
}
