package com.sjkj.wanandroid.ui.persenter

import com.sjkj.wanandroid.data.repertory.CollectionRepertory
import com.sjkj.wanandroid.mvp.BasePresenter
import com.sjkj.wanandroid.rxhelper.RxSubscriber
import com.sjkj.wanandroid.rxhelper.io_main
import com.sjkj.wanandroid.ui.view.FavView
import javax.inject.Inject

/**
 * @author by dingl on 2018/3/5.
 * @desc FavPresenter
 */
class FavPresenter @Inject constructor() : BasePresenter<FavView>() {

    @Inject
    lateinit var collectionRepertory: CollectionRepertory

    fun toggleCollectionArticle(isAdd: Boolean, id: Int, position: Int) {
        if (isAdd) {
            collectionRepertory.removeCollectionArticle(id)
                    .compose(io_main(lifecycleProvider))
                    .subscribe(object : RxSubscriber<String>(mView) {
                        override fun _onNext(t: String) {

                        }

                        override fun _onEmpty() {
                            super._onEmpty()
                            mView.removeFavSuccess(position)
                        }

                        override fun _onError(toast: String) {
                            super._onError(toast)
                            mView.removeFavFailed()
                        }

                    })
        } else
            collectionRepertory.collectionArticle(id)
                    .compose(io_main(lifecycleProvider))
                    .subscribe(object : RxSubscriber<String>(mView) {
                        override fun _onNext(t: String) {

                        }

                        override fun _onEmpty() {
                            super._onEmpty()
                            mView.favSuccess(position)
                        }

                        override fun _onError(toast: String) {
                            super._onError(toast)
                            mView.favFailed()
                        }
                    })
    }

}