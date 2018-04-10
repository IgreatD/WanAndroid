package com.sjkj.wanandroid.di.component

import android.app.Activity
import android.content.Context
import com.sjkj.wanandroid.di.module.*
import com.sjkj.wanandroid.di.scope.PerActivity
import com.trello.rxlifecycle.LifecycleProvider
import dagger.Component

/**
 * @author by dingl on 2018/1/17.
 * @desc ActivityComponent
 */
@PerActivity
@Component(dependencies = [(AppComponent::class)],
        modules = [(ActivityModule::class),
            (LifecycleProviderModule::class)])
interface ActivityComponent {

    fun activity(): Activity

    fun context(): Context

    fun lifecycleProvider(): LifecycleProvider<*>

    fun getHomeRepertoryComponent(homeRepertoryModule: HomeRepertoryModule): HomeRepertoryComponent
    fun getUserRepertoryComponent(userRepertoryModule: UserRepertoryModule): UserRepertoryComponent
    fun getKnowRepertoryComponent(knowRepertoryModule: KnowRepertoryModule): KnowRepertoryComponent
    fun getSearchRepertoryComponent(searchRepertoryModule: SearchRepertoryModule): SearchRepertoryComponent

    fun getCollectionRepertoryComponent(collectionRepertoryModule: CollectionRepertoryModule): CollectionRepertoryComponent

    fun getHotRepertoryComponent(hotRepertoryModule: HotRepertoryModule): HotRepertoryComponent

}
