package com.sjkj.wanandroid.common

/**
 * @author by dingl on 2018/1/16.
 * @desc Constant
 */
object Common {
    const val CONNECT_TIMEOUT_IN_MS: Long = 30L
    const val READ_TIMEOUT: Long = 10L
    const val SHARED_NAME = "_preferences"
    const val USERNAME_KEY = "username"
    const val PASSWORD_KEY = "password"
    const val COLLECTION_IDS = "collections"
    const val LOGIN_KEY = "login"
    const val LOGIN_RESULT_CODE = 100
}

object CookieConstant {
    const val SAVE_USER_LOGIN_KEY = "user/login"
    const val SAVE_USER_REGISTER_KEY = "user/register"
    const val SET_COOKIE_KEY = "set-cookie"
    const val COOKIE_NAME = "Cookie"
}

object CodeException {
    const val HTTP_ERROR = 0x2L
    const val JSON_ERROR = 0x3L
    const val UNKNOWN_ERROR = 0x4L
    const val UNKOWNHOST_ERROR = 0x6L
}
