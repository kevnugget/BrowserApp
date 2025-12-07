package edu.temple.superbrowser

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity(), TabFragment.ControlInterface{

    private val viewPager: ViewPager2 by lazy {
        findViewById(R.id.viewPager)
    }

    private val recyclerView: RecyclerView? by lazy {
        findViewById(R.id.recyclerView)
    }

    private val tabLayout: TabLayout? by lazy {
        findViewById(R.id.tabLayout)
    }

    private val browserViewModel : BrowserViewModel by lazy {
        ViewModelProvider(this)[BrowserViewModel::class.java]
    }

    private val bookmarkLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val urlToOpen = result.data?.getStringExtra("url")
                urlToOpen?.let {
                    loadUrlInNewTab(it)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = browserViewModel.getNumberOfTabs()

            // Each TabFragment maintains an ID assigned at instantiation.
            // This is used to notify the parent that a specific tab wants to update its title
            override fun createFragment(position: Int) = TabFragment.newInstance(position)
        }


        // Only if present (portrait)
        tabLayout?.run{

            // Keeps ViewPager and TabLayout selection in sync automatically
            // lambda updates title
            TabLayoutMediator(this, viewPager) { tab, position ->
                tab.text = browserViewModel.getPage(position).title
            }.attach()
        }

        // Only if present (landscape)
        recyclerView?.run{
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = PageAdapter(browserViewModel.tabs){
                viewPager.setCurrentItem(it, true)
            }
        }

        // Observe titles and update TabLayout or RecyclerView
        browserViewModel.getUpdate().observe(this) {
            viewPager.adapter?.notifyItemChanged(it)
            recyclerView?.adapter?.notifyItemChanged(it)
        }

    findViewById<View>(R.id.share).setOnClickListener {
        val currentPage = browserViewModel.getPage(viewPager.currentItem)
        if (currentPage.url.isNotEmpty()) {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, currentPage.url)
            }
            startActivity(Intent.createChooser(shareIntent, "Share URL"))
        }
    }

    findViewById<View>(R.id.addBookmark).setOnClickListener {
        val currentPage = browserViewModel.getPage(viewPager.currentItem)
        if (currentPage.title.isNotEmpty() && currentPage.url.isNotEmpty()) {
            val bookmarks = BookmarkManager.getBookmarks(this)
            if (bookmarks.any { it.url == currentPage.url }) {
                Toast.makeText(this, "Bookmark already exists!", Toast.LENGTH_SHORT).show()
            } else {
                BookmarkManager.saveBookmark(this, currentPage)
                Toast.makeText(this, "Bookmark Saved", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Cannot bookmark empty page", Toast.LENGTH_SHORT).show()
        }
    }

    findViewById<View>(R.id.bookmarks).setOnClickListener {
        val intent = Intent(this, BookmarkActivity::class.java)
        bookmarkLauncher.launch(intent)
    }
}

    // TabFragment.ControlInterface callback
    override fun newPage() {
        browserViewModel.addTab()
        viewPager.setCurrentItem(browserViewModel.getNumberOfTabs() - 1, true)
    }

    override fun loadUrlInNewTab(url: String) {
        browserViewModel.addTab(Page("Loading...", url))
        viewPager.adapter?.notifyDataSetChanged()
        viewPager.setCurrentItem(browserViewModel.getNumberOfTabs()-1, true)
    }
}