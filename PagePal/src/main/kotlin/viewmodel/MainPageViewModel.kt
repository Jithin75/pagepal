package org.example.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.example.model.BookModel
import org.example.model.UserModel

class MainPageViewModel (val userModel: UserModel, val bookLibrary : MutableList<BookModel>){
    var isHamburgerOpen by mutableStateOf(false)
        private set

    var isBookOpen by mutableStateOf(false)
        private set

    var bookOpened by mutableStateOf<BookModel?>(null)
        private set

    fun onBookClick(bookModel: BookModel) {
        isBookOpen = true
        bookOpened = bookModel
    }

    fun onDismissBook() {
        isBookOpen = false
        bookOpened = null
    }

    fun onHamburgerClick() {
        isHamburgerOpen = true
    }

    fun onDismissHamburger() {
        isHamburgerOpen = false
    }

    fun getUserLibrary(): List<BookModel> {
        /*val library = mutableListOf<BookModel>()

        val client = MongoClient.create(connectionString = "mongodb+srv://praviin10:Prav2003@cluster0.fqt7qpj.mongodb.net/?retryWrites=true&w=majority")
        val database = client.getDatabase("PagePalDB")
        val dbManager = DatabaseManager(database)

        for (bookId in userModel.library) {
            val book = dbManager.getBookById(bookId)
            if (book != null) {
                library.add(book)
            } else {
                // Handle the case where the book corresponding to bookId is not found
                println("Book with ID $bookId not found.")
            }
        }*/

        return bookLibrary
    }
}