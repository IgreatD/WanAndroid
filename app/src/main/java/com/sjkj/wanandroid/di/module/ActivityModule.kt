package com.sjkj.wanandroid.di.module

import android.app.Activity
import com.sjkj.wanandroid.di.scope.PerActivity
import dagger.Module
import dagger.Provides

/**
 * @author by dingl on 2018/1/17.
 * @desc ActivityModule
 */
@Module
class ActivityModule(private val activity: Activity) {

    @PerActivity
    @Provides
    fun provideActivity(): Activity = activity

}
