package kktyu.xyz.testphotoviewer.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kktyu.xyz.testphotoviewer.R
import kktyu.xyz.testphotoviewer.databinding.FragmentPhotoDetailBinding
import kktyu.xyz.testphotoviewer.imageLoader.GlideImageLoader

class PhotoDetailFragment : Fragment() {
    private lateinit var binding: FragmentPhotoDetailBinding
    private val parameter = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parameter[requireActivity().getString(R.string.search_parameter_method)] =
            requireActivity().getString(R.string.get_info_parameter_method_value)
        parameter[requireActivity().getString(R.string.search_parameter_api_key)] =
            requireActivity().getString(R.string.api_key)
        parameter[requireActivity().getString(R.string.search_parameter_format)] =
            requireActivity().getString(R.string.search_parameter_format_value)
        parameter[requireActivity().getString(R.string.search_parameter_nojsoncallback)] =
            requireActivity().getString(R.string.search_parameter_nojsoncallback_value)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = PhotoDetail()

        binding.lifecycleOwner = viewLifecycleOwner

        val viewModel: PhotoDetailViewModel by viewModels()

        val id: String =
            requireArguments().getString(requireActivity().getString(R.string.ID)) ?: ""
        val url: String =
            requireArguments().getString(requireActivity().getString(R.string.URL)) ?: ""

        parameter[requireActivity().getString(R.string.get_info_parameter_id)] = id

        viewModel.getApi(parameter, url)

        viewModel.viewModel.observe(viewLifecycleOwner) {
            binding.viewModel = it
            binding.imageLoader = GlideImageLoader(
                it.item?.url?.getDetailUrl()
            )
        }

        viewModel.errorResponse.observe(viewLifecycleOwner) {
            // API失敗
            // トースト表示
            Toast.makeText(
                requireActivity().applicationContext,
                "ステータスコード:$it",
                Toast.LENGTH_LONG
            ).show()

            // サーチ画面に戻る
            parentFragmentManager.popBackStack()
        }
    }
}
