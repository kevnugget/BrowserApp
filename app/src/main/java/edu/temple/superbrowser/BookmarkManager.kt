package edu.temple.superbrowser

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

object BookmarkManager {
    private const val FILE_NAME = "bookmarks.json"

    fun getBookmarks(context: Context): ArrayList<Page> {
        val file = File(context.filesDir, FILE_NAME)
        val pages = ArrayList<Page>()

        if (file.exists()) {
            try {
                val jsonString = file.readText()
                val jsonArray = JSONArray(jsonString)
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    pages.add(Page(obj.getString("title"), obj.getString("url")))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return pages
    }

    fun saveBookmark(context: Context, page: Page) {
        val currentList = getBookmarks(context)
        currentList.add(page)
        writeList(context, currentList)
    }

    fun deleteBookmark(context: Context, position: Int) {
        val currentList = getBookmarks(context)
        if (position in 0 until currentList.size) {
            currentList.removeAt(position)
            writeList(context, currentList)
        }
    }

    private fun writeList(context: Context, list: ArrayList<Page>) {
        val jsonArray = JSONArray()
        list.forEach {
            val obj = JSONObject()
            obj.put("title", it.title)
            obj.put("url", it.url)
            jsonArray.put(obj)
        }

         context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {
            it.write(jsonArray.toString().toByteArray())
        }
    }
}