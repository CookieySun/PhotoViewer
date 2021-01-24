package kktyu.xyz.testphotoviewer.ui.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kktyu.xyz.testphotoviewer.R
import kktyu.xyz.testphotoviewer.databinding.FragmentSearchBinding
import kktyu.xyz.testphotoviewer.ui.list.PhotoListFragment
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        search_box.setOnKeyListener { _, keyCode, event ->
            if (event.action != KeyEvent.ACTION_UP || keyCode != KeyEvent.KEYCODE_ENTER) {
                return@setOnKeyListener false
            }
            val searchWord = binding.searchBox.text.toString()

            if (searchWord.isEmpty()) {
                return@setOnKeyListener false
            }

            val bundle = Bundle()
            bundle.putString(requireActivity().getString(R.string.SEARCH_WORD), searchWord)

            val fragment = PhotoListFragment()
            fragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("SearchFragment")
                .commit()

            return@setOnKeyListener true
        }
    }
}
