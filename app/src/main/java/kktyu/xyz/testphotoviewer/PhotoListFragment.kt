package kktyu.xyz.testphotoviewer

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kktyu.xyz.testphotoviewer.databinding.FragmentPhotoListBinding
import kktyu.xyz.testphotoviewer.listResponseDataClass.Photo
import kktyu.xyz.testphotoviewer.listResponseDataClass.Photos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class PhotoListFragment : Fragment() {
    private lateinit var binding: FragmentPhotoListBinding
    private lateinit var baseUrl: String
    private val parameter = mutableMapOf<String, String>()

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        baseUrl = activity!!.getString(R.string.search_base_url)

        parameter[activity!!.getString(R.string.search_parameter_method)] =
            activity!!.getString(R.string.search_parameter_method_value)
        parameter[activity!!.getString(R.string.search_parameter_api_key)] =
            activity!!.getString(R.string.api_key)

        val calendar = Calendar.getInstance().apply {
            time = Date(System.currentTimeMillis())
            add(Calendar.DATE, -14)
        }
        parameter[activity!!.getString(R.string.search_parameter_min_upload_date)] =
            SimpleDateFormat("yyyy-MM-dd").format(calendar.time)
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
        binding.loading = true
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner

        // 検索単語取得
        val searchWord = arguments?.getString(activity!!.getString(R.string.SEARCH_WORD)) ?: ""

        parameter[activity!!.getString(R.string.search_parameter_text)] = searchWord

        val adapter = GroupAdapter<ViewHolder>()

        binding.photoList.adapter = adapter

        listCreate(adapter)

        binding.photoList.layoutManager = GridLayoutManager(this.activity, 2)
    }

    private fun listCreate(
        adapter: GroupAdapter<ViewHolder>
    ) {
        // 処理に時間がかかることがあるので別スレッドで実行
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val mainHandler = Handler(Looper.getMainLooper())
                // API処理
                lateinit var photos: Photos
                val photoInfo = mutableListOf<Photo>()
                val response = getApi()

                if (response.isSuccessful) {
                    binding.loading = false

                    if (response.body() != null) {
                        photos = response.body()!!.photos

                        // 検索結果件数確認
                        if (photos.photo.isNotEmpty()) {
                            photos.photo.forEach {
                                photoInfo.add(it)
                            }
                        } else {
                            // 検索結果なし
                            // トースト表示
                            mainHandler.post {
                                Toast.makeText(
                                    activity!!.applicationContext,
                                    "検索結果 0件",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            // サーチ画面に戻る
                            if (fragmentManager != null) {
                                fragmentManager!!.popBackStack()
                            }
                        }
                    }
                } else {
                    // API失敗
                    // トースト表示
                    mainHandler.post {
                        Toast.makeText(
                            activity!!.applicationContext,
                            "ステータスコード:${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    // サーチ画面に戻る
                    if (fragmentManager != null) {
                        fragmentManager!!.popBackStack()
                    }
                }

                val itemList = mutableListOf<ListItem>()

                photoInfo.forEach {
                    itemList.add(
                        ListItem(
                            it.id,
                            it.title,
                            Url(
                                activity!!.getString(R.string.photo_base_url_1) +
                                        it.farm +
                                        activity!!.getString(R.string.photo_base_url_2) +
                                        "/" +
                                        it.server +
                                        "/" +
                                        it.id + "_" + it.secret,
                                activity!!.applicationContext
                            )
                        )
                    )
                }

                // メインスレッドへ処理を移譲
                mainHandler.post {
                    adapter.update(mutableListOf<Group>().apply {
                        itemList.forEach {
                            add(PhotoItem(it, activity!!, fragmentManager))
                        }
                    })
                }
            }
        }
    }

    private fun getApi() =
        GetApiData(activity!!.getString(R.string.search_base_url)).getPhotoList(parameter)
}
