package kktyu.xyz.testphotoviewer.ui.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kktyu.xyz.testphotoviewer.GetApiData
import kktyu.xyz.testphotoviewer.R
import kktyu.xyz.testphotoviewer.Url
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class PhotoListViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>()

    private val _itemList = MutableLiveData<List<ListItem>>()
    val itemList: LiveData<List<ListItem>> get() = _itemList

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private fun addParameter(param: String): Map<String, String> {
        return mutableMapOf<String, String>().apply {
            put(
                getApplication<Application>().getString(R.string.search_parameter_method),
                getApplication<Application>().getString(R.string.search_parameter_method_value)
            )
            put(
                getApplication<Application>().getString(R.string.search_parameter_api_key),
                getApplication<Application>().getString(R.string.api_key)
            )
            put(
                    getApplication<Application>().getString(R.string.search_parameter_format),
                    getApplication<Application>().getString(R.string.search_parameter_format_value)
            )
            put(
                    getApplication<Application>().getString(R.string.search_parameter_nojsoncallback),
                    getApplication<Application>().getString(R.string.search_parameter_nojsoncallback_value)
            )

            val calendar = Calendar.getInstance().apply {
                time = Date(System.currentTimeMillis())
                add(Calendar.DATE, -14)
            }
            put(
                    getApplication<Application>().getString(R.string.search_parameter_min_upload_date),
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            )
            // 検索単語取得
            put(
                    getApplication<Application>().getString(R.string.search_parameter_text),
                    param
            )
        }
    }

    fun getApi(param: String) {
        viewModelScope.launch {
            val response = GetApiData(
                getApplication<Application>().getString(
                    R.string.search_base_url
                )
            ).getPhotoList(addParameter(param))

            if (!response.isSuccessful) {
                _errorMessage.postValue("ステータスコード:${response.code()}")
                return@launch
            }

            val body = response.body()

            if (body == null) {
                _errorMessage.postValue("検索結果 0件")
                return@launch
            }

            val photoList = body.photos.photo

            if (photoList.isEmpty()) {
                _errorMessage.postValue("検索結果 0件")
                return@launch
            }

            _itemList.postValue(
                photoList.map {
                    ListItem(
                        it.id,
                        it.title,
                        Url(
                            context.getString(R.string.photo_base_url_1) +
                                    it.farm +
                                    context.getString(R.string.photo_base_url_2) +
                                    "/" +
                                    it.server +
                                    "/" +
                                    it.id + "_" + it.secret,
                            context.applicationContext
                        )
                    )
                }
            )
        }
    }
}
