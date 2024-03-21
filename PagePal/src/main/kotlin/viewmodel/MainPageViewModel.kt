package org.example.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.runBlocking
import org.example.model.BookModel
import org.example.model.DatabaseManager
import org.example.model.UserModel


class MainPageViewModel (val userModel: UserModel,
                         val bookLibrary : MutableList<BookModel>,
                         val dbManager: DatabaseManager){
    var displayedBooks: MutableState<MutableList<BookModel>> = mutableStateOf(bookLibrary.toMutableList())
        private set

    var isHamburgerOpen by mutableStateOf(false)
        private set

    var isBookOpen by mutableStateOf(false)
        private set

    var bookOpened by mutableStateOf<BookModel?>(null)
        private set

    var isAddBookOpen by mutableStateOf(false)
        private set

    var isProfileOpen by mutableStateOf(false)
        private set

    var isRecommendOpen by mutableStateOf(false)
        private set

    fun toggleProfilePage() {
        isProfileOpen = !isProfileOpen
    }

    fun toggleRecommendPage() {
        isRecommendOpen = !isRecommendOpen
    }

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

    fun onAddBookClick() {
        isAddBookOpen = true
    }

    fun onDismissAddBook() {
        isAddBookOpen = false
    }

    fun statusFilter(status: String) {
        if (status == "Status") {
            displayedBooks.value = bookLibrary.toMutableList()
        } else {
            displayedBooks.value = bookLibrary.filter { it.status.contains(status, ignoreCase = true) }.toMutableList()
        }
    }

    fun sortFilter(sortType: String) {
        if (sortType == "Sort" || sortType == "Recently Added") {
            displayedBooks.value = bookLibrary.toMutableList()
        } else if (sortType == "Title") {
            displayedBooks.value = bookLibrary.sortedBy { it.title }.toMutableList()
        } else {
            displayedBooks.value = bookLibrary.sortedBy { it.author }.toMutableList()
        }
    }

    fun getUserLibrary(): MutableState<MutableList<BookModel>> {
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

        return displayedBooks
    }

    fun addBook(book: BookModel) {
        runBlocking {
            val bookId = dbManager.addBook(book)
            dbManager.updateUserBookList(userModel.username, bookId)
            userModel.addBook(bookId)
        }
        bookLibrary.add(book)
        displayedBooks.value.add(book)
    }

    fun searchResults(searchValue: String) {
        if (searchValue.isNotBlank()) {
            displayedBooks.value = bookLibrary.filter { it.title.contains(searchValue, ignoreCase = true) }.toMutableList()
        } else {
            displayedBooks.value = bookLibrary.toMutableList() // Reset to original bookLibrary if search is empty
        }
    }

    fun refreshDisplay() {
        displayedBooks.value = bookLibrary.toMutableList()
    }
}