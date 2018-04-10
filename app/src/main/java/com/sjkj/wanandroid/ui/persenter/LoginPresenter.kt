package com.sjkj.wanandroid.ui.persenter

import com.sjkj.wanandroid.bean.UserBean
import com.sjkj.wanandroid.data.repertory.UserRepertory
import com.sjkj.wanandroid.mvp.BasePresenter
import com.sjkj.wanandroid.rxhelper.RxSubscriber
import com.sjkj.wanandroid.rxhelper.io_main
import com.sjkj.wanandroid.ui.view.LoginView
import javax.inject.Inject

/**
 * @author by dingl on 2018/3/3.
 * @desc LoginPresenter
 */
class LoginPresenter @Inject constructor() : BasePresenter<LoginView>() {

    @Inject
    lateinit var userRepertory: UserRepertory

    fun login(username: String, password: String) {
        userRepertory.login(username, password)
                .compose(io_main(lifecycleProvider))
                .subscribe(object : RxSubscriber<UserBean>(mView) {
                    override fun _onNext(t: UserBean) {
                        mView.loginSuccess(t)
                    }

                    override fun _onError(toast: String) {
                        super._onError(toast)
                        mView.loginFailed()
                    }
                })
    }

    fun sign(username: String, password: String) {
        userRepertory.sign(username, password)
                .compose(io_main(lifecycleProvider))
                .subscribe(object :RxSubscriber<UserBean>(mView){
                    override fun _onNext(t: UserBean) {
                        mView.signSuccess(t)
                    }

                    override fun _onError(toast: String) {
                        super._onError(toast)
                        mView.signFailed()
                    }
                })
    }

}