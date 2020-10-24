package kktyu.xyz.testphotoviewer.listResponseDataClass

data class Photos(
    val page: String,
    val pages: String,
    val perpage: String,
    val total: String,
    val photo: List<Photo>
)
