package org.example.model

data class BookModel (val title: String,
                      val author: String = "unspecified author",
                      val cover: String = "coverNotAvailable.png")