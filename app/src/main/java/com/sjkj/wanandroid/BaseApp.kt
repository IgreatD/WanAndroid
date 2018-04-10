package com.sjkj.wanandroid

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.blankj.utilcode.util.Utils
import com.mob.MobSDK
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.sjkj.wanandroid.di.component.AppComponent
import com.sjkj.wanandroid.di.component.DaggerAppComponent
import com.sjkj.wanandroid.di.module.AppModule
import com.sjkj.wanandroid.utils.PreferenceUtils
import org.litepal.LitePal
import kotlin.properties.Delegates


/**
 * @author by dingl on 2018/1/15.
 * @desc BaseApp
 */
open class BaseApp : Application() {

    lateinit var appComponent: AppComponent

    companion object {
        var instance by Delegates.notNull<BaseApp>()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initLogger()
        initAppComponent()
        initLitePal()
        initUtils()
        PreferenceUtils.setContext(applicationContext)
        MobSDK.init(this)
    }

    private fun initUtils() {
        Utils.init(this)
    }

    private fun initLitePal() {
        LitePal.initialize(this)
    }

    private fun initAppComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    private fun initLogger() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .methodCount(0)
                .methodOffset(7)
                .tag("sjkj_logger")
                .build()

        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }

}
