package kktyu.xyz.testphotoviewer.ui.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kktyu.xyz.testphotoviewer.GetApiData
import kktyu.xyz.testphotoviewer.R
import kktyu.xyz.testphotoviewer.listResponseDataClass.Rsp
import kotlinx.coroutines.launch
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class PhotoListViewModel(application: Application) : AndroidViewModel(application) {
    private var _photoResponse = MutableLiveData<Response<Rsp>>()
    val photoResponse: LiveData<Response<Rsp>> get() = _photoResponse

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
            _photoResponse.value = GetApiData(
                    getApplication<Application>().getString(
                            R.string.search_base_url
                    )
            ).getPhotoList(addParameter(param))
        }
    }
}
