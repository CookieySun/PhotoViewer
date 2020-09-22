package kktyu.xyz.testphotoviewer.photo_list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kktyu.xyz.testphotoviewer.R
import kktyu.xyz.testphotoviewer.databinding.FragmentPhotoListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class PhotoListFragment : Fragment() {
    private lateinit var binding: FragmentPhotoListBinding
    private lateinit var baseUrl: String

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        baseUrl = activity!!.getString(R.string.search_base_url)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoListBinding.inflate(inflater, container, false)
        binding.loading = true
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = GroupAdapter<ViewHolder>()

        binding.photoList.adapter = adapter

        val viewModel = PhotoListViewModel(this)
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val response = viewModel.getApi()
                viewModel.listCreate(adapter, response)
                binding.loading = false
            }
        }

        binding.photoList.layoutManager = GridLayoutManager(this.activity, 2)
    }
}
