package kktyu.xyz.testphotoviewer

import android.content.Context

class Url(
    val url: String,
    private val context: Context
) {
    fun getListUrl(): String {
        return url + context.getString(R.string.photo_url_small)
    }

    fun getDetailUrl(): String {
        return url + context.getString(R.string.photo_url_large)
    }
}