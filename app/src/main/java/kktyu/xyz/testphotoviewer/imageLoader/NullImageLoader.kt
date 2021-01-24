package kktyu.xyz.testphotoviewer.imageLoader

import android.widget.ImageView
import com.bumptech.glide.Glide

class NullImageLoader : ImageLoader {
    override fun load(view: ImageView) {
        Glide.with(view).clear(view)
    }
}
