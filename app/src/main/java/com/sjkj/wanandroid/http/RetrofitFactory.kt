package com.sjkj.wanandroid.http

import com.sjkj.wanandroid.common.Common
import com.sjkj.wanandroid.common.CookieConstant.COOKIE_NAME
import com.sjkj.wanandroid.common.CookieConstant.SAVE_USER_LOGIN_KEY
import com.sjkj.wanandroid.common.CookieConstant.SAVE_USER_REGISTER_KEY
import com.sjkj.wanandroid.common.CookieConstant.SET_COOKIE_KEY
import com.sjkj.wanandroid.common.HttpStore
import com.sjkj.wanandroid.extensions.encodeCookie
import com.sjkj.wanandroid.extensions.saveCookie
import com.sjkj.wanandroid.utils.PreferenceUtils
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.schedulers.Schedulers
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

/**
 * @author by dingl on 2018/1/15.
 * @desc RetrofitFactory
 */
class RetrofitFactory {

    companion object {
        val instance by lazy { RetrofitFactory() }
    }

    fun getRetrofit(): Retrofit {
        val nullOnEmptyConverterFactory = object : Converter.Factory() {
            fun converterFactory() = this
            override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>, retrofit: Retrofit) = object : Converter<ResponseBody, Any?> {
                val nextResponseBodyConverter = retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)
                override fun convert(value: ResponseBody) = if (value.contentLength() != 0L) nextResponseBodyConverter.convert(value) else null
            }
        }

        return Retrofit.Builder()
                .client(initClient())
                .baseUrl(HttpStore.Base_Url)
                .addConverterFactory(nullOnEmptyConverterFactory)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
    }

    private fun initClient() =
            OkHttpClient.Builder()
                    .connectTimeout(Common.CONNECT_TIMEOUT_IN_MS, TimeUnit.SECONDS)
                    .readTimeout(Common.READ_TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor {
                        val request = it.request()
                        val response = it.proceed(request)
                        val requestUrl = request.url().toString()
                        val domain = request.url().host()
                        if ((requestUrl.contains(SAVE_USER_LOGIN_KEY) || requestUrl.contains(
                                        SAVE_USER_REGISTER_KEY
                                ))
                                && !response.headers(SET_COOKIE_KEY).isEmpty()) {
                            val cookies = response.headers(SET_COOKIE_KEY)
                            val cookie = encodeCookie(cookies)
                            saveCookie(requestUrl, domain, cookie)
                        }
                        response
                    }
                    .addInterceptor {
                        val request = it.request()
                        val builder = request.newBuilder()
                        val domain = request.url().host()
                        if (domain.isNotEmpty()) {
                            val spDomain: String by PreferenceUtils(domain, "")
                            val cookie: String = if (spDomain.isNotEmpty()) spDomain else ""
                            if (cookie.isNotEmpty()) {
                                builder.addHeader(COOKIE_NAME, cookie)
                            }
                        }
                        it.proceed(builder.build())
                    }
                    .build()
}

inline fun <reified T> createService(service: Class<T>): T = RetrofitFactory.instance.getRetrofit().create(service)

