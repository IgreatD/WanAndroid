package com.sjkj.wanandroid.http

import com.orhanobut.logger.Logger
import okhttp3.logging.HttpLoggingInterceptor

/**
 * @author by dingl on 2018/1/17.
 * @desc HttpLogInterceptor
 */
object HttpLogInterceptor {

    fun provideInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor { msg ->
            Logger.d(msg)
        }
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        return interceptor
    }

}
