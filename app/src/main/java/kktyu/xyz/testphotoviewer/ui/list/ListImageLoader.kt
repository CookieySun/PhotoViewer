package kktyu.xyz.testphotoviewer.ui.list

import kktyu.xyz.testphotoviewer.imageLoader.GlideImageLoader

object ListImageLoader {
    fun load(sageItem: ListItem): GlideImageLoader {
        return GlideImageLoader(
            sageItem.photo.getListUrl()
        )
    }
}