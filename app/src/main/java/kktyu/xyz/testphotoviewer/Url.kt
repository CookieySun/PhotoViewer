package kktyu.xyz.testphotoviewer

import androidx.fragment.app.FragmentActivity

class Url(
    val url: String, private val activity: FragmentActivity
) {
    fun getListUrl(): String {
        return url + activity.getString(R.string.photo_url_small)
    }

    fun getDetailUrl(): String {
        return url + activity.getString(R.string.photo_url_large)
    }
}