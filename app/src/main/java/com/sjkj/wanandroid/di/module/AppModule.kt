package com.sjkj.wanandroid.di.module

import android.content.Context
import com.sjkj.wanandroid.di.scope.PerApplication
import com.sjkj.wanandroid.BaseApp
import dagger.Module
import dagger.Provides

/**
 * @author by dingl on 2018/1/15.
 * @desc AppModule
 */
@Module
class AppModule(private val context: BaseApp) {

    @PerApplication
    @Provides
    fun provideContext(): Context {
        return this.context
    }
}
