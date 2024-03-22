package org.example.model

import model.api.Book

data class BookModel (
    val title: String,
    val author: String = "unspecified Authors",
    val cover: String = "coverNotAvailable.png",
    val publisher: String = "Publisher not available",
    val publishYear: String = "Publish Year not available",
    val description: String = "Description not available",
    val categories: String = "Categories not available",
    var status: String = "In Progress")
