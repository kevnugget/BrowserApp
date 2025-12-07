package edu.temple.superbrowser

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

class TabFragment : Fragment(), ControlFragment.ControlInterface, PageFragment.PageInterface {

    private val TAB_ID = "tabId"

    private var tabId = 0

    private lateinit var browserActivity: ControlInterface

    private lateinit var pageFragment: PageFragment

    private val browserViewModel : BrowserViewModel by lazy {
        ViewModelProvider(requireActivity())[BrowserViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        browserActivity = context as ControlInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.apply {
            tabId = getInt(TAB_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tab, container, false).apply {
            pageFragment = childFragmentManager.findFragmentById(R.id.page) as PageFragment
        }
    }

    override fun onResume() {
        super.onResume()
        val page = browserViewModel.getPage(tabId)
        if (page.url.isNotEmpty()) {
            pageFragment.loadUrl(page.url)
        }
    }

    override fun loadUrl(url: String) {
        pageFragment.loadUrl(url)
    }

    override fun nextPage() {
        pageFragment.goNext()
    }

    override fun backPage() {
        pageFragment.goBack()
    }

    // route invocation from child fragment to parent activity
    override fun newPage() {
        browserActivity.newPage()
    }

    override fun loadUrlInNewTab(url: String) {
        browserActivity.loadUrlInNewTab(url)
    }

    interface ControlInterface {
        fun newPage()
        fun loadUrlInNewTab(url: String)
    }

    companion object {
        fun newInstance(tabId: Int) = TabFragment().apply {
            arguments = Bundle().apply {
                putInt(TAB_ID, tabId)
            }
        }
    }

    override fun updatePage(page: Page) {
        host?.run {
            browserViewModel.updatePage(tabId, page)
        }
    }
}