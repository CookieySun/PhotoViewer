package kktyu.xyz.testphotoviewer

import androidx.databinding.BaseObservable
import kktyu.xyz.testphotoviewer.imageLoader.GlideImageLoader
import kktyu.xyz.testphotoviewer.imageLoader.ImageLoader
import kktyu.xyz.testphotoviewer.imageLoader.NullImageLoader

class ListItemViewModel : BaseObservable() {
    var item: ListItem? = null
        set(value) {
            field = value
            notifyChange()
        }

    val photoLoader: ImageLoader
        get() {
            val sageItem = item ?: return NullImageLoader()
            return GlideImageLoader(
                sageItem.photo.getListUrl()
            )
        }

}