package edu.temple.superbrowser

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider


class ControlFragment : Fragment() {

    private lateinit var urlEditText: EditText
    private lateinit var browser: ControlInterface
    private var currentTitle: String = ""
    private var currentUrl: String = ""

    // Fetch ViewModel instance scoped against parent fragment
    private val pageDataViewModel: PageDataViewModel by lazy {
        ViewModelProvider(requireParentFragment())[PageDataViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        browser = requireParentFragment() as ControlInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_control, container, false).apply {
            urlEditText = findViewById(R.id.urlET)
            findViewById<View>(R.id.goIV).setOnClickListener { browser.loadUrl(urlEditText.text.toString()) }
            findViewById<View>(R.id.backIV).setOnClickListener { browser.backPage() }
            findViewById<View>(R.id.nextIV).setOnClickListener { browser.nextPage() }
            findViewById<View>(R.id.new_page).setOnClickListener { browser.newPage() }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pageDataViewModel.getCurrentUrl().observe(viewLifecycleOwner){
            urlEditText.setText(it)
        }
    }

    private fun saveBookmark() {
        if (currentUrl.isEmpty() || currentUrl == "about:blank" || currentTitle.isEmpty()) {
            Toast.makeText(context, "Cannot bookmark this page", Toast.LENGTH_SHORT).show()
            return
        }

        val bookmarks = BookmarkManager.getBookmarks(requireContext())

        if (bookmarks.any { it.url == currentUrl }) {
            Toast.makeText(context, "Bookmark already exists!", Toast.LENGTH_SHORT).show()
        }
        else {
            BookmarkManager.saveBookmark(requireContext(), Page(currentTitle, currentUrl))
            Toast.makeText(context, "Bookmark Saved", Toast.LENGTH_SHORT).show()
        }
    }

    interface ControlInterface {
        fun loadUrl(url: String)
        fun nextPage()
        fun backPage()
        fun newPage()
        fun loadUrlInNewTab(url: String)
    }

}