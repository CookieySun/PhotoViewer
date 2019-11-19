package kktyu.xyz.testphotoviewer

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

// マスターAPIインターフェース
interface SearchApiInterface {
    @GET(".")
    fun getMaster(@QueryMap map: Map<String, String>): Call<Rsp>
}
