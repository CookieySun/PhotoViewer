package kktyu.xyz.testphotoviewer.ui.detail

import kktyu.xyz.testphotoviewer.Url
import kktyu.xyz.testphotoviewer.imageLoader.GlideImageLoader

object DetailImageLoader {
    fun load(url: Url): GlideImageLoader {
        return GlideImageLoader(
            url.getDetailUrl()
        )
    }
}