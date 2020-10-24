package kktyu.xyz.testphotoviewer.imageLoader

import android.widget.ImageView
import com.bumptech.glide.Glide

class GlideImageLoader(
    private val url: String
) : ImageLoader {

    override fun load(view: ImageView) {
        Glide.with(view).load(url).into(view)
    }
}