package com.sjkj.wanandroid.di.scope

import javax.inject.Scope

/**
 * @author by dingl on 2018/1/18.
 * @desc PerRepertory
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerRepertory

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerUser

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerCollection
