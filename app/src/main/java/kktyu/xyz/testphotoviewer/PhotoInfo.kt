package kktyu.xyz.testphotoviewer

data class PhotoInfo(
    val title: String,
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: String
)