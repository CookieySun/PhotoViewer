package kktyu.xyz.testphotoviewer.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kktyu.xyz.testphotoviewer.GetApiData
import kktyu.xyz.testphotoviewer.R
import kktyu.xyz.testphotoviewer.Url
import kktyu.xyz.testphotoviewer.databinding.FragmentPhotoDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

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

        binding.lifecycleOwner = viewLifecycleOwner

        val id: String =
            requireArguments().getString(requireActivity().getString(R.string.ID)) ?: ""
        val url: String =
            requireArguments().getString(requireActivity().getString(R.string.URL)) ?: ""

        parameter[requireActivity().getString(R.string.get_info_parameter_id)] = id

        val response = getApi()

        if (response.isSuccessful) {
            if (response.body() != null) {
                val photoInfo = response.body()!!.photo

                binding.viewModel = PhotoDetail()
                binding.viewModel?.item = PhotoDetailModel(
                    photoInfo.title._content,
                    photoInfo.description._content,
                    photoInfo.dates.taken,
                    Url(url, requireActivity().applicationContext)
                )
            }
        } else {
            // API失敗
            // トースト表示
            Toast.makeText(
                requireActivity().applicationContext,
                "ステータスコード:${response.code()}",
                Toast.LENGTH_LONG
            ).show()

            // サーチ画面に戻る
            parentFragmentManager.popBackStack()
        }
    }

    private fun getApi() = runBlocking {
        return@runBlocking withContext(Dispatchers.IO) {
            GetApiData(requireActivity().getString(R.string.search_base_url)).getPhotoInfo(parameter)
        }
    }
}
