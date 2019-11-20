package kktyu.xyz.testphotoviewer

import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.xwray.groupie.databinding.BindableItem
import kktyu.xyz.testphotoviewer.databinding.PhotoItemBinding

class PhotoItem(private val photo: Photo, val clickListener: (View) -> Unit) :
    BindableItem<PhotoItemBinding>() {
    private val clickListener: (View) -> Unit = {
        Log.d("loglog", photo.title)
    }

    override fun getLayout() = R.layout.photo_item

    override fun bind(viewBinding: PhotoItemBinding, position: Int) {
        viewBinding.item = photo
        viewBinding.root.setOnClickListener(clickListener)
        Glide.with(viewBinding.root.context).load(photo.photo).into(viewBinding.imageView)
    }
}