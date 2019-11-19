package kktyu.xyz.testphotoviewer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kktyu.xyz.testphotoviewer.databinding.FragmentPhotoListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class PhotoListFragment : Fragment() {
    lateinit var binding: FragmentPhotoListBinding
    lateinit var baseUrl: String
    val parameter = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        baseUrl = activity!!.getString(R.string.search_base_url)

        parameter[activity!!.getString(R.string.search_parameter_method)] =
            activity!!.getString(R.string.search_parameter_method_value)
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
        binding = FragmentPhotoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // クリックリスナー設定
        val clickListener: (View) -> Unit = {
            Log.d("loglog", "click:$it")
        }

        // 検索単語取得
        val searchWord = if (arguments == null) {
            ""
        } else {
            arguments!!.getString(activity!!.getString(R.string.SEARCH_WORD))
        }

        parameter[activity!!.getString(R.string.search_parameter_text)] = searchWord

        // API処理
        lateinit var photos : Photos
        val photoInfo= mutableListOf<PhotoInfo>()
        val response = getApi()

        if (response.isSuccessful) {
            if(response.body() != null) {
                photos = response.body()!!.photos
                photos.photo.forEach {
                    photoInfo.add(it)
                }
            }
        }else{
            Toast.makeText(activity!!.applicationContext,"ステータスコード:${response.code()}",Toast.LENGTH_LONG).show()
            
        }

        val adapter = GroupAdapter<ViewHolder>()

        binding.photoList.adapter = adapter

//        adapter.update(mutableListOf<Group>().apply {
//            itemList.forEach {
//                add(PhotoItem(it, clickListener))
//            }
//        })

        binding.photoList.layoutManager = GridLayoutManager(this.activity, 2)
    }

    private fun getApi() = runBlocking {
        return@runBlocking withContext(Dispatchers.IO) {
            val list =
                GetApiData(activity!!.getString(R.string.search_base_url)).getPhotoList(parameter)

            list
        }

    }
}
