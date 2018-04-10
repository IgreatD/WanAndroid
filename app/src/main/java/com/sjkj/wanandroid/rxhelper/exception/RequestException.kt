package com.sjkj.wanandroid.rxhelper.exception

import android.accounts.NetworkErrorException
import com.alibaba.fastjson.JSONException
import com.sjkj.wanandroid.common.CodeException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException

/**
 * @author by dingl on 2017/9/30.
 * @desc RequestException
 */
class RequestFailedException(message: String) : Exception(message)

class RequestIllegalArgumentException(message: String) : IllegalArgumentException(message)

class RequestNetWorkException(message: String) : NetworkErrorException(message)

class RequestNullDataException(message: String) : Exception(message)

class RxException {

    private val HttpException_MSG = "网络错误"
    private val ConnectException_MSG = "连接失败"
    private val JSONException_MSG = "数据解析失败"
    private val UnknownHostException_MSG = "无法解析该域名"

    fun analysisException(e: Throwable): ApiException {
        val apiException = ApiException(e)
        if (e is HttpException) {
            apiException.code = CodeException.HTTP_ERROR
            apiException.msg = HttpException_MSG
        } else if (e is ConnectException || e is SocketTimeoutException) {
            apiException.code = (CodeException.HTTP_ERROR)
            apiException.msg = (ConnectException_MSG)
        } else if (e is JSONException || e is ParseException) {
            apiException.code = (CodeException.JSON_ERROR)
            apiException.msg = (JSONException_MSG)
        } else if (e is UnknownHostException) {
            apiException.code = (CodeException.UNKOWNHOST_ERROR)
            apiException.msg = (UnknownHostException_MSG)
        } else {
            apiException.code = (CodeException.UNKNOWN_ERROR)
            apiException.msg = (e.message)
        }
        return apiException
    }
}

data class ApiException(private val e: Throwable) : Exception(e) {
    var code: Long = 0
    var msg: String? = null
}
