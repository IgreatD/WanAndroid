package com.sjkj.wanandroid.base

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.CheckResult
import com.qmuiteam.qmui.widget.QMUIEmptyView
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.sjkj.wanandroid.BaseApp
import com.sjkj.wanandroid.di.component.ActivityComponent
import com.sjkj.wanandroid.di.component.DaggerActivityComponent
import com.sjkj.wanandroid.di.module.ActivityModule
import com.sjkj.wanandroid.di.module.LifecycleProviderModule
import com.sjkj.wanandroid.mvp.BasePresenter
import com.sjkj.wanandroid.mvp.BaseView
import com.trello.rxlifecycle.LifecycleProvider
import com.trello.rxlifecycle.LifecycleTransformer
import com.trello.rxlifecycle.RxLifecycle
import com.trello.rxlifecycle.android.ActivityEvent
import com.trello.rxlifecycle.android.RxLifecycleAndroid
import me.yokeyword.fragmentation.SupportActivity
import org.jetbrains.anko.act
import org.jetbrains.anko.toast
import rx.Observable
import rx.subjects.BehaviorSubject
import javax.inject.Inject

/**
 * @author by dingl on 2018/1/19.
 * @desc BaseRxActivity
 */
abstract class BaseRxActivity<P : BasePresenter<*>> : SupportActivity(), LifecycleProvider<ActivityEvent>, BaseView {

    @Inject
    lateinit var mPresenter: P

    override fun showToast(toast: String) {
        this.toast(toast)
    }

    override fun showLoading() {
//        if (mLoadingDialog.isShowing.not()) {
//            mLoadingDialog.show()
//        }
    }

    override fun hideLoading() {
//        if (mLoadingDialog.isShowing) {
//            mLoadingDialog.dismiss()
//        }
    }

    private lateinit var mLoadingDialog: QMUITipDialog

    protected open lateinit var mActivityComponent: ActivityComponent

    protected open val mEmptyView by lazy { QMUIEmptyView(this) }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        lifecycleSubject.onNext(ActivityEvent.CREATE)
        mLoadingDialog = QMUITipDialog.Builder(act).setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING).create()
        initActivityInjection()
        injectComponent()
        initView(savedInstanceState)
    }

    abstract fun initView(savedInstanceState: Bundle?)

    abstract fun getLayoutId(): Int

    abstract fun injectComponent()

    private fun initActivityInjection() {
        mActivityComponent = DaggerActivityComponent.builder()
                .appComponent((act.application as BaseApp).appComponent)
                .activityModule(ActivityModule(act))
                .lifecycleProviderModule(LifecycleProviderModule(this))
                .build()
    }

    private val lifecycleSubject = BehaviorSubject.create<ActivityEvent>()

    @CheckResult
    override fun lifecycle(): Observable<ActivityEvent> {
        return lifecycleSubject.asObservable()
    }

    @CheckResult
    override fun <T> bindUntilEvent(event: ActivityEvent): LifecycleTransformer<T> {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event)
    }

    @CheckResult
    override fun <T> bindToLifecycle(): LifecycleTransformer<T> {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject)
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        lifecycleSubject.onNext(ActivityEvent.START)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        lifecycleSubject.onNext(ActivityEvent.RESUME)
    }

    @CallSuper
    override fun onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE)
        super.onPause()
    }

    @CallSuper
    override fun onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP)
        super.onStop()
    }

    @CallSuper
    override fun onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY)
        super.onDestroy()
    }
}
