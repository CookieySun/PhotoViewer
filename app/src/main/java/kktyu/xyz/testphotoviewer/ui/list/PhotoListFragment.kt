package kktyu.xyz.testphotoviewer.ui.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kktyu.xyz.testphotoviewer.R
import kktyu.xyz.testphotoviewer.Url
import kktyu.xyz.testphotoviewer.databinding.FragmentPhotoListBinding
import kktyu.xyz.testphotoviewer.listResponseDataClass.Photo
import kktyu.xyz.testphotoviewer.listResponseDataClass.Rsp
import retrofit2.Response

class PhotoListFragment : Fragment() {
    private lateinit var binding: FragmentPhotoListBinding
    private lateinit var baseUrl: String

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        baseUrl = requireContext().getString(R.string.search_base_url)
    }

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

        val adapter = GroupAdapter<ViewHolder>()

        binding.photoList.adapter = adapter

        val viewModel by viewModels<PhotoListViewModel>()

        binding.loading = true
        val param = requireArguments().getString(this.getString(R.string.SEARCH_WORD)) ?: ""
        viewModel.getApi(param)

        viewModel.photoResponse.observe(viewLifecycleOwner, {
            listCreate(adapter, it)
            binding.loading = false
        })

        binding.photoList.layoutManager = GridLayoutManager(this.requireContext(), 2)
    }

    private fun listCreate(adapter: GroupAdapter<ViewHolder>, photoResponse: Response<Rsp>) {
        // 処理に時間がかかることがあるので別スレッドで実行
        val mainHandler = Handler(Looper.getMainLooper())
        // API処理
        val photoInfo = mutableListOf<Photo>()

        if (photoResponse.isSuccessful) {
            if (photoResponse.body() != null) {
                val photos = photoResponse.body()!!.photos

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
                                requireContext().applicationContext,
                                "検索結果 0件",
                                Toast.LENGTH_LONG
                        ).show()
                    }
                    // サーチ画面に戻る
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        } else {
            // API失敗
            // トースト表示
            mainHandler.post {
                Toast.makeText(
                        requireContext(),
                        "ステータスコード:${photoResponse.code()}",
                        Toast.LENGTH_LONG
                ).show()
            }
            // サーチ画面に戻る
            requireActivity().supportFragmentManager.popBackStack()
        }

        val itemList = mutableListOf<ListItem>()

        photoInfo.forEach {
            itemList.add(
                    ListItem(
                            it.id,
                            it.title,
                            Url(
                                    requireContext().getString(R.string.photo_base_url_1) +
                                            it.farm +
                                            requireContext().getString(R.string.photo_base_url_2) +
                                            "/" +
                                            it.server +
                                            "/" +
                                            it.id + "_" + it.secret,
                                    requireContext().applicationContext
                            )
                    )
            )
        }

        // メインスレッドへ処理を移譲
        mainHandler.post {
            adapter.update(
                    mutableListOf<Group>().apply {
                        itemList.forEach {
                            add(
                                    PhotoItem(
                                            it,
                                            requireActivity(),
                                            requireActivity().supportFragmentManager
                                    )
                            )
                        }
                    }
            )
        }
    }
}
