package kktyu.xyz.testphotoviewer.listResponseDataClass

data class Photo(
    val title: String,
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: String
)
