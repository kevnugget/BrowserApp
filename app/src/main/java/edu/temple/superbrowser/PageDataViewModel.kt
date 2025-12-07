package edu.temple.superbrowser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PageDataViewModel  : ViewModel(){
    private val currentUrl : MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    private val currentTitle : MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    fun getCurrentUrl() : LiveData<String> = currentUrl

    fun setCurrentUrl(url: String) {
        currentUrl.value = url
    }

    fun getCurrentTitle() : LiveData<String> = currentTitle

    fun setCurrentTitle(url: String) {
        currentTitle.value = url
    }
}