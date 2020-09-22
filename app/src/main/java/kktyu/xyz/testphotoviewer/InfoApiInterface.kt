package kktyu.xyz.testphotoviewer

import kktyu.xyz.testphotoviewer.photoInfoResponseDataClass.RspInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface InfoApiInterface {
    @GET(".")
    fun getInfo(@QueryMap map: Map<String, String>): Call<RspInfo>
}
