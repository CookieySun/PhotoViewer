package kktyu.xyz.testphotoviewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kktyu.xyz.testphotoviewer.databinding.FragmentPhotoDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class PhotoDetailFragment : Fragment() {
    lateinit var binding: FragmentPhotoDetailBinding
    private val parameter = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parameter[activity!!.getString(R.string.search_parameter_method)] =
            activity!!.getString(R.string.get_info_parameter_method_value)
        parameter[activity!!.getString(R.string.search_parameter_api_key)] =
            activity!!.getString(R.string.api_key)
        parameter[activity!!.getString(R.string.search_parameter_format)] =
            activity!!.getString(R.string.search_parameter_format_value)
        parameter[activity!!.getString(R.string.search_parameter_nojsoncallback)] =
            activity!!.getString(R.string.search_parameter_nojsoncallback_value)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lateinit var id: String
        lateinit var url: String

        if (arguments != null) {
            id = arguments!!.getString(activity!!.getString(R.string.ID))!!
            url = arguments!!.getString(activity!!.getString(R.string.URL))!!
        } else {
            id = ""
            url = ""
        }

        parameter[activity!!.getString(R.string.get_info_parameter_id)] = id

        val response = getApi()

        if (response.isSuccessful) {
            if (response.body() != null) {
                val photoInfo = response.body()!!.photo

                binding.viewModel = PhotoDetailViewModel()
                binding.viewModel?.item = PhotoDetail(
                    photoInfo.title._content,
                    photoInfo.description._content,
                    photoInfo.dates.taken,
                    Url(url, activity!!.applicationContext)
                )
            }
        } else {
            // API失敗
            // トースト表示
            Toast.makeText(
                activity!!.applicationContext,
                "ステータスコード:${response.code()}",
                Toast.LENGTH_LONG
            ).show()

            // サーチ画面に戻る
            if (fragmentManager != null) {
                fragmentManager!!.popBackStack()
            }
        }
    }

    private fun getApi() = runBlocking {
        return@runBlocking withContext(Dispatchers.IO) {
            GetApiData(activity!!.getString(R.string.search_base_url)).getPhotoInfo(parameter)
        }
    }
}
