package com.sjkj.wanandroid.di.component

import android.content.Context
import com.sjkj.wanandroid.di.module.AppModule
import com.sjkj.wanandroid.di.scope.PerApplication
import dagger.Component

/**
 * @author by dingl on 2018/1/17.
 * @desc AppComponent
 */
@PerApplication
@Component(modules = [(AppModule::class)])
interface AppComponent {

    fun context(): Context
}
