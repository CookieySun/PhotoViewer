package kktyu.xyz.testphotoviewer.ui.detail

import androidx.databinding.BaseObservable
import kktyu.xyz.testphotoviewer.imageLoader.GlideImageLoader
import kktyu.xyz.testphotoviewer.imageLoader.ImageLoader
import kktyu.xyz.testphotoviewer.imageLoader.NullImageLoader

class PhotoDetailViewModel : BaseObservable() {
    var item: PhotoDetail? = null
        set(value) {
            field = value
            notifyChange()
        }

    val photoLoader: ImageLoader
        get() {
            val sageItem = item ?: return NullImageLoader()
            return GlideImageLoader(
                sageItem.url.getDetailUrl()
            )
        }
}
