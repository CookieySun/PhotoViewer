package kktyu.xyz.testphotoviewer.ui.list

import com.xwray.groupie.databinding.BindableItem
import kktyu.xyz.testphotoviewer.R
import kktyu.xyz.testphotoviewer.databinding.PhotoItemBinding

typealias OnSelectItem = (ListItem) -> Unit

class PhotoItem(
    private val photoModel: ListItem
) :
    BindableItem<PhotoItemBinding>() {
    var clickListener: OnSelectItem? = null

    override fun getLayout() = R.layout.photo_item

    override fun bind(viewBinding: PhotoItemBinding, position: Int) {
        viewBinding.viewModel = ListItemModel()
        viewBinding.viewModel?.item = photoModel
        viewBinding.imageLoader = ListImageLoader
        viewBinding.root.setOnClickListener { clickListener?.invoke(photoModel) }
    }
}
