package com.sjkj.wanandroid.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.CheckResult
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qmuiteam.qmui.util.QMUIKeyboardHelper
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
import com.trello.rxlifecycle.android.FragmentEvent
import com.trello.rxlifecycle.android.RxLifecycleAndroid
import me.yokeyword.fragmentation.SupportFragment
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.toast
import rx.Observable
import rx.subjects.BehaviorSubject
import javax.inject.Inject

/**
 * @author by dingl on 2018/1/17.
 * @desc BaseRxFragment
 */
abstract class BaseRxFragment<P : BasePresenter<*>> : SupportFragment(), LifecycleProvider<FragmentEvent>, BaseView {

    @Inject
    lateinit var mPresenter: P

    private val lifecycleSubject = BehaviorSubject.create<FragmentEvent>()

    private lateinit var mLoadingDialog: QMUITipDialog

    protected open lateinit var mActivityComponent: ActivityComponent

    protected open val mEmptyView by lazy { QMUIEmptyView(context) }

    @CheckResult
    override fun lifecycle(): Observable<FragmentEvent> {
        return lifecycleSubject.asObservable()
    }

    @CheckResult
    override fun <T> bindUntilEvent(event: FragmentEvent): LifecycleTransformer<T> {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event)
    }

    @CheckResult
    override fun <T> bindToLifecycle(): LifecycleTransformer<T> {
        return RxLifecycleAndroid.bindFragment(lifecycleSubject)
    }

    @CallSuper
    override fun onAttach(context: Context) {
        super.onAttach(context)
        lifecycleSubject.onNext(FragmentEvent.ATTACH)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleSubject.onNext(FragmentEvent.CREATE)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW)
        initView(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getLayoutId(), container, false)
        mLoadingDialog = QMUITipDialog.Builder(activity).setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING).create()
        initActivityInjection()
        injectComponent()
        return view
    }

    abstract fun getLayoutId(): Int

    abstract fun injectComponent()

    abstract fun initView(savedInstanceState: Bundle?)

    private fun initActivityInjection() {
        mActivityComponent = DaggerActivityComponent.builder()
                .appComponent((act.application as BaseApp).appComponent)
                .activityModule(ActivityModule(act))
                .lifecycleProviderModule(LifecycleProviderModule(this))
                .build()
    }


    @CallSuper
    override fun onStart() {
        super.onStart()
        lifecycleSubject.onNext(FragmentEvent.START)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        lifecycleSubject.onNext(FragmentEvent.RESUME)
    }

    @CallSuper
    override fun onPause() {
        lifecycleSubject.onNext(FragmentEvent.PAUSE)
        super.onPause()
    }

    @CallSuper
    override fun onStop() {
        lifecycleSubject.onNext(FragmentEvent.STOP)
        super.onStop()
    }

    @CallSuper
    override fun onDestroyView() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW)
        if (QMUIKeyboardHelper.isKeyboardVisible(activity)) {
            QMUIKeyboardHelper.hideKeyboard(view)
        }
        super.onDestroyView()
    }

    @CallSuper
    override fun onDestroy() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY)
        super.onDestroy()
    }

    @CallSuper
    override fun onDetach() {
        lifecycleSubject.onNext(FragmentEvent.DETACH)
        super.onDetach()
    }

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
}
