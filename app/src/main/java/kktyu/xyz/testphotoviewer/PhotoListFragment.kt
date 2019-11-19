package kktyu.xyz.testphotoviewer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kktyu.xyz.testphotoviewer.databinding.FragmentPhotoListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class PhotoListFragment : Fragment() {
    lateinit var binding: FragmentPhotoListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val clickListener: (View) -> Unit = {
            Log.d("loglog", "click:$it")
        }

        val itemList = getApi()
        val adapter = GroupAdapter<ViewHolder>()

        binding.photoList.adapter = adapter

        adapter.update(mutableListOf<Group>().apply {
            itemList.forEach {
                add(PhotoItem(it, clickListener))
            }
        })

        binding.photoList.layoutManager = GridLayoutManager(this.activity, 2)
    }

    private fun getApi() = runBlocking {
        return@runBlocking withContext(Dispatchers.IO) {
            val list = mutableListOf<Photo>()

            for (n in 0..10) {
                list.add(
                    Photo(
                        "title",
                        "https://pbs.twimg.com/media/EJiz6CQUwAE-M5W?format=jpg&name=360x360",
//                        "https://pbs.twimg.com/media/EJi-OG-VUAARmLC?format=jpg&name=360x360",
                        "date"
                    )
                )
                list.add(
                    Photo(
                        "title",
                        "https://pbs.twimg.com/media/EJfyPUCUcAA0-Nx?format=jpg&name=360x360",
                        "date"
                    )
                )
            }
            list
        }

    }
}
