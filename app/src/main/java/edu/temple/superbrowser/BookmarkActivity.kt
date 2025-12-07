package edu.temple.superbrowser

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BookmarkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadList(recyclerView)
        findViewById<View>(R.id.close).setOnClickListener {
            finish()
        }
    }

    private fun loadList(recyclerView: RecyclerView) {
        val bookmarks = BookmarkManager.getBookmarks(this)

        recyclerView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

            inner class Holder(view: android.view.View) : RecyclerView.ViewHolder(view) {
                val title = view.findViewById<TextView>(R.id.titleTv)
                val delete = view.findViewById<ImageView>(R.id.deleteIv)
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                return Holder(layoutInflater.inflate(R.layout.item_bookmark, parent, false))
            }

            override fun getItemCount() = bookmarks.size

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val item = bookmarks[position]
                (holder as Holder).title.text = item.title

                holder.itemView.setOnClickListener {
                    val resultIntent = Intent()
                    resultIntent.putExtra("url", item.url)
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }

                holder.delete.setOnClickListener {
                    AlertDialog.Builder(this@BookmarkActivity)
                        .setTitle("Delete Bookmark")
                        .setMessage("Are you sure you want to delete ${item.title}?")
                        .setPositiveButton("Yes") { _, _ ->
                            BookmarkManager.deleteBookmark(this@BookmarkActivity, holder.adapterPosition)
                            loadList(recyclerView) // Refresh the list
                        }
                        .setNegativeButton("No", null)
                        .show()
                }
            }
        }
    }
}