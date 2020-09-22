package kktyu.xyz.testphotoviewer.photo_list

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kktyu.xyz.testphotoviewer.GetApiData
import kktyu.xyz.testphotoviewer.ListItem
import kktyu.xyz.testphotoviewer.PhotoItem
import kktyu.xyz.testphotoviewer.R
import kktyu.xyz.testphotoviewer.Url
import kktyu.xyz.testphotoviewer.listResponseDataClass.Photo
import kktyu.xyz.testphotoviewer.listResponseDataClass.Photos
import kktyu.xyz.testphotoviewer.listResponseDataClass.Rsp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class PhotoListViewModel(private val fragment: Fragment) :
    AndroidViewModel(fragment.activity!!.application) {
    private val activity: FragmentActivity = fragment.activity!!

    private fun addParameter(): Map<String, String> {
        return mutableMapOf<String, String>().apply {
            put(
                activity.getString(R.string.search_parameter_method),
                activity.getString(R.string.search_parameter_method_value)
            )
            put(
                activity.getString(R.string.search_parameter_api_key),
                activity.getString(R.string.api_key)
            )
            put(
                activity.getString(R.string.search_parameter_format),
                activity.getString(R.string.search_parameter_format_value)
            )
            put(
                activity.getString(R.string.search_parameter_nojsoncallback),
                activity.getString(R.string.search_parameter_nojsoncallback_value)
            )

            val calendar = Calendar.getInstance().apply {
                time = Date(System.currentTimeMillis())
                add(Calendar.DATE, -14)
            }
            put(
                activity.getString(R.string.search_parameter_min_upload_date),
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            )
            // 検索単語取得
            put(
                activity.getString(R.string.search_parameter_text),
                fragment.arguments?.getString(activity.getString(R.string.SEARCH_WORD)) ?: ""
            )
        }
    }

    fun listCreate(
        adapter: GroupAdapter<ViewHolder>,
        response: Response<Rsp>
    ) {
        // 処理に時間がかかることがあるので別スレッドで実行
        val mainHandler = Handler(Looper.getMainLooper())
        // API処理
        lateinit var photos: Photos
        val photoInfo = mutableListOf<Photo>()

        if (response.isSuccessful) {
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
                            activity.applicationContext,
                            "検索結果 0件",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    // サーチ画面に戻る
                    activity.supportFragmentManager.popBackStack()
                }
            }
        } else {
            // API失敗
            // トースト表示
            mainHandler.post {
                Toast.makeText(
                    activity.applicationContext,
                    "ステータスコード:${response.code()}",
                    Toast.LENGTH_LONG
                ).show()
            }
            // サーチ画面に戻る
            activity.supportFragmentManager.popBackStack()
        }

        val itemList = mutableListOf<ListItem>()

        photoInfo.forEach {
            itemList.add(
                ListItem(
                    it.id,
                    it.title,
                    Url(
                        activity.getString(R.string.photo_base_url_1) +
                                it.farm +
                                activity.getString(R.string.photo_base_url_2) +
                                "/" +
                                it.server +
                                "/" +
                                it.id + "_" + it.secret,
                        activity.applicationContext
                    )
                )
            )
        }

        // メインスレッドへ処理を移譲
        mainHandler.post {
            adapter.update(mutableListOf<Group>().apply {
                itemList.forEach {
                    add(
                        PhotoItem(
                            it,
                            activity,
                            activity.supportFragmentManager
                        )
                    )
                }
            })
        }
    }

    suspend fun getApi(): Response<Rsp> =
        coroutineScope {
            withContext(Dispatchers.IO) {
                GetApiData(
                    activity.getString(
                        R.string.search_base_url
                    )
                ).getPhotoList(addParameter())
            }
        }
}