package kktyu.xyz.testphotoviewer

import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.xwray.groupie.databinding.BindableItem
import kktyu.xyz.testphotoviewer.databinding.PhotoItemBinding

class PhotoItem(
    private val photo: Photo,
    private val activity: FragmentActivity
) :
    BindableItem<PhotoItemBinding>() {
    private val clickListener: (View) -> Unit = {
        Log.d("loglog", photo.title)
    }

    override fun getLayout() = R.layout.photo_item

    override fun bind(viewBinding: PhotoItemBinding, position: Int) {
        viewBinding.item = photo
        viewBinding.root.setOnClickListener(clickListener)
        Glide.with(viewBinding.root.context)
            .load(photo.photo + activity.getString(R.string.photo_url_small))
            .into(viewBinding.imageView)
    }
}