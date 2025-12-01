package edu.temple.superbrowser

data class Page (var title: String, var url: String){
    constructor() : this("_blank", "")
}