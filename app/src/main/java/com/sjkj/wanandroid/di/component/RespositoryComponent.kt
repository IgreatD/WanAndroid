package com.sjkj.wanandroid.di.component

import com.sjkj.wanandroid.di.module.*
import com.sjkj.wanandroid.di.scope.PerRepertory
import com.sjkj.wanandroid.ui.activity.CollectionActivity
import com.sjkj.wanandroid.ui.activity.LoginActivity
import com.sjkj.wanandroid.ui.activity.SearchActivity
import com.sjkj.wanandroid.ui.fragment.*
import dagger.Subcomponent

/**
 * @author by dingl on 2018/1/18.
 * @desc RepertoryComponent
 */
@PerRepertory
@Subcomponent(modules = [(UserRepertoryModule::class)])
interface UserRepertoryComponent {
    fun inject(activity: LoginActivity)
}

@PerRepertory
@Subcomponent(modules = [(HomeRepertoryModule::class)])
interface HomeRepertoryComponent {
    fun inject(fragment: HomeFragment)
    fun inject(fragment: MineFragment)
}

@PerRepertory
@Subcomponent(modules = [(KnowRepertoryModule::class)])
interface KnowRepertoryComponent {
    fun inject(fragment: TreeFragment)
    fun inject(fragment: KnowFragment)
}

@PerRepertory
@Subcomponent(modules = [(SearchRepertoryModule::class)])
interface SearchRepertoryComponent {
    fun inject(activity: SearchActivity)
}

@PerRepertory
@Subcomponent(modules = [(CollectionRepertoryModule::class)])
interface CollectionRepertoryComponent {
    fun inject(activity: SearchActivity)
    fun inject(fragment: HomeFragment)
    fun inject(activity: CollectionActivity)
    fun inject(fragment: KnowFragment)
}

@PerRepertory
@Subcomponent(modules = [(HotRepertoryModule::class)])
interface HotRepertoryComponent {
    fun inject(fragment: HotFragment)
}
