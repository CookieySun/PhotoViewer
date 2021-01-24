package kktyu.xyz.testphotoviewer.ui.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.xwray.groupie.databinding.BindableItem
import kktyu.xyz.testphotoviewer.R
import kktyu.xyz.testphotoviewer.databinding.PhotoItemBinding
import kktyu.xyz.testphotoviewer.ui.detail.PhotoDetailFragment

class PhotoItem(
    private val photoModel: ListItem,
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
        viewBinding.viewModel = ListItemModel()
        viewBinding.viewModel?.item = photoModel
        viewBinding.imageLoader = ListImageLoader
        viewBinding.root.setOnClickListener(clickListener)
    }
}
