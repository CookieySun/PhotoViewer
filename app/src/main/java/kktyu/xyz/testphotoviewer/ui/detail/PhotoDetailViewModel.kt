package kktyu.xyz.testphotoviewer.ui.detail

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kktyu.xyz.testphotoviewer.GetApiData
import kktyu.xyz.testphotoviewer.R
import kktyu.xyz.testphotoviewer.Url
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class PhotoDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val context: Context = getApplication()

    private val _viewModel = MutableLiveData<PhotoDetail>()
    val viewModel: LiveData<PhotoDetail> get() = _viewModel

    private val _errorResponse = MutableLiveData<String>()
    val errorResponse: LiveData<String> get() = _errorResponse

    fun getApi(parameter: Map<String, String>, url: String) {
        viewModelScope.launch(IO) {
            val response =
                GetApiData(context.getString(R.string.search_base_url)).getPhotoInfo(parameter)

            if (!response.isSuccessful) {
                _errorResponse.value = response.code().toString()
            }
            val photoInfo = response.body()?.photo ?: return@launch

            _viewModel.postValue(PhotoDetail().apply {
                item = PhotoDetailModel(
                    photoInfo.title._content,
                    photoInfo.description._content,
                    photoInfo.dates.taken,
                    Url(url, context)
                )
            })
        }
    }
}