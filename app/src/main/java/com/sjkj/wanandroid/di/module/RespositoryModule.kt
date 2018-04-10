package com.sjkj.wanandroid.di.module

import com.sjkj.wanandroid.data.repertory.*
import com.sjkj.wanandroid.di.scope.PerRepertory
import dagger.Module
import dagger.Provides

/**
 * @author by dingl on 2018/1/18.
 * @desc UserRepertoryModule
 */
@Module
class UserRepertoryModule {
    @Provides
    @PerRepertory
    fun provideUserRepertory() = UserRepertory()
}

@Module
class HomeRepertoryModule {
    @Provides
    @PerRepertory
    fun provideHomeRepertory() = HomeRepertory()
}

@Module
class KnowRepertoryModule {

    @Provides
    @PerRepertory
    fun provideKnowRepertory() = KnowRepertory()
}

@Module
class SearchRepertoryModule {

    @Provides
    @PerRepertory
    fun provideSearchRepertory() = SearchRepertory()
}

@Module
class CollectionRepertoryModule {

    @Provides
    @PerRepertory
    fun providCollectionRepertory() = CollectionRepertory()
}

@Module
class HotRepertoryModule {
    @Provides
    @PerRepertory
    fun provideHotRepertory() = HotRepertory()
}
