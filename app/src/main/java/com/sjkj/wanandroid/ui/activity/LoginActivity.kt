package com.sjkj.wanandroid.ui.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.sjkj.wanandroid.R
import com.sjkj.wanandroid.base.BaseRxActivity
import com.sjkj.wanandroid.bean.UserBean
import com.sjkj.wanandroid.common.Common
import com.sjkj.wanandroid.di.module.UserRepertoryModule
import com.sjkj.wanandroid.extensions.getInteger
import com.sjkj.wanandroid.ui.persenter.LoginPresenter
import com.sjkj.wanandroid.ui.view.LoginView
import com.sjkj.wanandroid.utils.PreferenceUtils
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class LoginActivity : BaseRxActivity<LoginPresenter>(), LoginView {

    override fun signSuccess(userBean: UserBean) {
        toast(R.string.sign_success)
        transitionTo(userBean)
    }

    override fun signFailed() {
        isLogin = false
        showProgress(false)
    }

    private var isLogin by PreferenceUtils(Common.LOGIN_KEY, false)

    private var user by PreferenceUtils(defaultValue = UserBean())

    override fun injectComponent() {
        mActivityComponent.getUserRepertoryComponent(UserRepertoryModule()).inject(this)
        mPresenter.mView = this
    }

    override fun loginSuccess(userBean: UserBean) {
        transitionTo(userBean)
    }

    private fun transitionTo(userBean: UserBean) {
        userBean.isLogin = true
        user = userBean
        setResult(Activity.RESULT_OK)
        finishAfterTransition()
    }

    override fun loginFailed() {
        isLogin = false
        showProgress(false)
    }

    override fun getLayoutId() = R.layout.activity_login

    override fun initView(savedInstanceState: Bundle?) {

        showProgress(false)

        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && isLoginValid()) {
                login.performClick()
                return@OnEditorActionListener true
            }
            false
        })

        username.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                password.requestFocus()
                return@OnEditorActionListener true
            }
            false
        })

        signup.onClick { attemptSign() }

        login.onClick { attemptLogin() }

        username.addTextChangedListener(loginFieldWatcher)
        password.addTextChangedListener(loginFieldWatcher)
    }

    private fun attemptSign() {
        if (checkContent()) {
            showProgress(true)
            username.postDelayed({ mPresenter.sign(username.text.toString(), password.text.toString()) }, 2000)

        } else
            showProgress(false)
    }

    private fun attemptLogin() {
        if (checkContent()) {
            showProgress(true)
            username.postDelayed({ mPresenter.login(username.text.toString(), password.text.toString()) }, 2000)
        } else
            showProgress(false)
    }

    private fun showProgress(show: Boolean) {

        val shortAnimTime = getInteger(R.integer.transition_duration)

        login_form.visibility = if (show) View.GONE else View.VISIBLE

        login_form.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0f else 1f))
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_form.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

        login_progress.visibility = if (show) View.VISIBLE else View.GONE

        login_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1f else 0f))
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })


    }

    private fun isLoginValid(): Boolean {
        return username.length() > 0 && password.length() > 0
    }

    private val loginFieldWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            login.isEnabled = isLoginValid()
        }
    }

    private fun checkContent(): Boolean {
        username.error = null
        password.error = null
        // cancel
        var cancel = false
        var focusView: View? = null
        val pwdText = password.text.toString()
        val usernameText = username.text.toString()
        if (TextUtils.isEmpty(pwdText)) {
            password.error = getString(R.string.password_not_empty)
            focusView = password
            cancel = true
        } else if (password.text.length < 6) {
            password.error = getString(R.string.password_length_short)
            focusView = password
            cancel = true
        }

        if (TextUtils.isEmpty(usernameText)) {
            username.error = getString(R.string.username_not_empty)
            focusView = username
            cancel = true
        }

        return if (cancel) {
            if (focusView != null) {
                focusView.requestFocus()
            }
            false
        } else {
            true
        }
    }

}
