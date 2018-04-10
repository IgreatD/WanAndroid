package com.sjkj.wanandroid.di.module

import com.trello.rxlifecycle.LifecycleProvider
import dagger.Module
import dagger.Provides

/**
 * @author by dingl on 2018/1/17.
 * @desc LifecycleProviderModule
 */
@Module
class LifecycleProviderModule(private val lifecycleProvider: LifecycleProvider<*>) {

    @Provides
    fun provideLifecycleProvider() = lifecycleProvider
}
