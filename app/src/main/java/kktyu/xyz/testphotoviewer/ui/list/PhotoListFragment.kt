package kktyu.xyz.testphotoviewer.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kktyu.xyz.testphotoviewer.R
import kktyu.xyz.testphotoviewer.databinding.FragmentPhotoListBinding
import kktyu.xyz.testphotoviewer.ui.detail.PhotoDetailFragment

class PhotoListFragment : Fragment() {
    private lateinit var binding: FragmentPhotoListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoListBinding.inflate(inflater, container, false)
        binding.loading = true
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner

        val viewModel by viewModels<PhotoListViewModel>()

        val adapter = GroupAdapter<ViewHolder>()

        binding.photoList.adapter = adapter

        val onSelectItem: OnSelectItem = {
            val bundle = Bundle()
            bundle.putString(requireContext().getString(R.string.ID), it.id)
            bundle.putString(requireContext().getString(R.string.URL), it.photo.url)

            val fragment = PhotoDetailFragment()
            fragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .add(R.id.container, fragment)
                .addToBackStack("PhotoListFragment")
                .commit()
        }

        binding.loading = true

        val param = requireArguments().getString(this.getString(R.string.SEARCH_WORD)) ?: ""
        viewModel.getApi(param)

        viewModel.itemList.observe(viewLifecycleOwner, {
            adapter.addAll(it.map { listItem ->
                PhotoItem(listItem).apply {
                    this.clickListener = onSelectItem
                }
            })
            binding.loading = false
        })

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            // トースト表示
            Toast.makeText(
                requireContext(),
                it,
                Toast.LENGTH_LONG
            ).show()
            // サーチ画面に戻る
            parentFragmentManager.popBackStack()
        }

        binding.photoList.layoutManager = GridLayoutManager(this.requireContext(), 2)
    }
}
