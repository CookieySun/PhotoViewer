package kktyu.xyz.testphotoviewer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kktyu.xyz.testphotoviewer.ui.search.SearchFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(
            R.id.container,
            SearchFragment()
        ).commit()
    }
}
