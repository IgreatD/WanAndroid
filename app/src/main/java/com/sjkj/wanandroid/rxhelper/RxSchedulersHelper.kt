package com.sjkj.wanandroid.rxhelper

import com.sjkj.wanandroid.bean.BaseJsonResult
import com.sjkj.wanandroid.rxhelper.exception.RxException
import com.trello.rxlifecycle.LifecycleProvider
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * @author by dingl on 2017/9/12.
 * *
 * @desc RxSchedulersHelper
 */

inline fun <reified T> io_main(lifecycleProvider: LifecycleProvider<*>): Observable.Transformer<BaseJsonResult<T>, T> {
    return Observable.Transformer { tObservable ->
        tObservable
                .retryWhen(CustomerRetryWhen())
                .onErrorResumeNext({ Observable.error(RxException().analysisException(it)) })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleProvider.bindToLifecycle())
                .compose(RxResultHelper.handleResult())
    }
}
