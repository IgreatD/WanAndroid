package com.sjkj.wanandroid.ui.persenter

import com.sjkj.wanandroid.R
import com.sjkj.wanandroid.bean.HotTagBean
import com.sjkj.wanandroid.data.repertory.HotRepertory
import com.sjkj.wanandroid.extensions.getString
import com.sjkj.wanandroid.mvp.BasePresenter
import com.sjkj.wanandroid.rxhelper.CustomerRetryWhen
import com.sjkj.wanandroid.rxhelper.RxSubscriber
import com.sjkj.wanandroid.rxhelper.exception.RxException
import com.sjkj.wanandroid.ui.view.HotView
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * @author by dingl on 2018/3/6.
 * @desc HotPresenter
 */
class HotPresenter @Inject constructor() : BasePresenter<HotView>() {

    @Inject
    lateinit var hotRepertory: HotRepertory

    fun getHotKeyData() {
        val friendObservable = hotRepertory.getFriendData()
        val hotObservable = hotRepertory.getHotKeyData()
        Observable.zip(hotObservable, friendObservable, { t1, t2 ->
            val hotList = ArrayList<HotTagBean>()
            hotList.add(HotTagBean(1, getString(R.string.hot_search), t1))
            hotList.add(HotTagBean(2, getString(R.string.hot_common), t2))
            hotList
        }).retryWhen(CustomerRetryWhen())
                .onErrorResumeNext({ Observable.error(RxException().analysisException(it)) })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleProvider.bindToLifecycle())
                .subscribe(object : RxSubscriber<List<HotTagBean>>(mView) {
                    override fun _onNext(t: List<HotTagBean>) {
                        mView.setHotKeyData(t)
                        mView.loadEnd()
                    }

                    override fun _onError(toast: String) {
                        super._onError(toast)
                        mView.loadError()
                        mView.loadEnd()
                    }

                })
    }

}
