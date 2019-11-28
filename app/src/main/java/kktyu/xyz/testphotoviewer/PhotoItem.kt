package kktyu.xyz.testphotoviewer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.xwray.groupie.databinding.BindableItem
import kktyu.xyz.testphotoviewer.databinding.PhotoItemBinding

class PhotoItem(
    private val photoModel: ListItemModel,
    private val activity: FragmentActivity,
    fragmentManager: FragmentManager?
) :
    BindableItem<PhotoItemBinding>() {
    private val clickListener: (View) -> Unit = {
        val bundle = Bundle()
        bundle.putString(activity.getString(R.string.ID), photoModel.id)
        bundle.putString(activity.getString(R.string.URL), photoModel.photo.url)

        val fragment = PhotoDetailFragment()
        fragment.arguments = bundle

        fragmentManager!!.beginTransaction()
            .add(R.id.container, fragment)
            .addToBackStack("PhotoListFragment")
            .commit()
    }

    override fun getLayout() = R.layout.photo_item

    override fun bind(viewBinding: PhotoItemBinding, position: Int) {
        viewBinding.model = photoModel
        viewBinding.root.setOnClickListener(clickListener)
        Glide.with(viewBinding.root.context)
            .load(photoModel.photo.getListUrl())
            .into(viewBinding.imageView)
    }
}