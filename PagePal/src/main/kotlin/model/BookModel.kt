package org.example.model

data class BookModel (
    val title: String,
    val author: String = "unspecified Authors",
    var cover: String = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/2048px-No_image_available.svg.png",
    val publisher: String = "Publisher not available",
    val publishYear: String = "Publish Year not available",
    val description: String = "Description not available",
    val categories: String = "Categories not available",
    var status: String = "In Progress",
    var chapter: String = "1",
    var page: String = "1",
    var bookId: String = "") {

    fun newBook(Books: MutableList<BookModel>) : Boolean {
        for(book in Books) {
            if (book.title == title && book.author == author && book.description == description) {
                return false
            }
        }
        return true
    }
}
