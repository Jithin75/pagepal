package org.example.viewmodel

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
    var displayedBooks by mutableStateOf(bookLibrary.toMutableList())
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
            displayedBooks = bookLibrary.toMutableList()
        } else {
            displayedBooks = bookLibrary.filter { it.status.contains(status, ignoreCase = true) }.toMutableList()
        }
    }

    fun sortFilter(sortType: String) {
        if (sortType == "Sort" || sortType == "Recently Added") {
            displayedBooks = bookLibrary.toMutableList()
        } else if (sortType == "Title") {
            displayedBooks = bookLibrary.sortedBy { it.title }.toMutableList()
        } else {
            displayedBooks = bookLibrary.sortedBy { it.author }.toMutableList()
        }
    }

    fun getUserLibrary(): MutableList<BookModel> {
        return displayedBooks
    }

    fun addBook(book: BookModel) {

        runBlocking {
            val bookId = dbManager.addBook(book)
            dbManager.updateUserBookList(userModel.username, bookId)
            userModel.addBook(bookId)
        }

        bookLibrary.add(book)
        displayedBooks.add(book)
    }

    fun searchResults(searchValue: String) {
        if (searchValue.isNotBlank()) {
            displayedBooks = bookLibrary.filter { it.title.contains(searchValue, ignoreCase = true) }.toMutableList()
        } else {
            displayedBooks = bookLibrary.toMutableList() // Reset to original bookLibrary if search is empty
        }
    }

    fun refreshDisplay() {
        displayedBooks = bookLibrary.toMutableList()
    }
}