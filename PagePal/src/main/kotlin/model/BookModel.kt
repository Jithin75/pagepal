package org.example.model

data class BookModel (
    val title: String,
    val author: String = "unspecified Authors",
    val cover: String = "coverNotAvailable.png",
    val publisher: String = "Publisher not available",
    val publishYear: String = "Publish Year not available",
    val description: String = "Description not available",
    val categories: String = "Categories not available",
    var status: String = "In Progress",
    var chapter: String = "1",
    var page: String = "1",
    var bookId: String = "")
