package kktyu.xyz.testphotoviewer.imageLoader

import android.widget.ImageView
import androidx.databinding.BindingAdapter

class ImageLoaderBinding {

    companion object {

        @BindingAdapter("image_loader")
        @JvmStatic
        fun bind(view: ImageView, imageLoader: ImageLoader) {
            imageLoader.load(view)
        }
    }
}