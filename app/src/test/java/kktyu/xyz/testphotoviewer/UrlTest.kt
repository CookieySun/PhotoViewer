package kktyu.xyz.testphotoviewer

import android.os.Build
import androidx.test.core.app.ApplicationProvider
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class UrlTest {
    private lateinit var url: Url
    @Before
    fun setUp() {
        url = Url("hogehoge", ApplicationProvider.getApplicationContext())
    }

    @Test
    fun getListUrlTest() {
        val listUrl = url.getListUrl()
        Assertions.assertThat(listUrl).isEqualTo("hogehoge_q.jpg")
    }

    @Test
    fun getDetailUrlTest() {
        val detailUrl = url.getDetailUrl()
        Assertions.assertThat(detailUrl).isEqualTo("hogehoge.jpg")
    }
}
