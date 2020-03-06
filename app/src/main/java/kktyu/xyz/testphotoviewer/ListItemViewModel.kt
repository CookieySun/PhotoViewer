package kktyu.xyz.testphotoviewer

import androidx.lifecycle.ViewModel
import kktyu.xyz.testphotoviewer.imageLoader.GlideImageLoader
import kktyu.xyz.testphotoviewer.imageLoader.ImageLoader
import kktyu.xyz.testphotoviewer.imageLoader.NullImageLoader

class ListItemViewModel : ViewModel() {
    var item: ListItem? = null

    val photoLoader: ImageLoader
        get() {
            val sageItem = item ?: return NullImageLoader()
            return GlideImageLoader(
                sageItem.photo.getListUrl()
            )
        }
}