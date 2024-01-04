package com.nurunabiyev.wpmapp.core.utils

import android.util.Log

fun printLog(text: Any, tag: String = "wpmlogtag") {
    Log.d(tag, text.toString())
}