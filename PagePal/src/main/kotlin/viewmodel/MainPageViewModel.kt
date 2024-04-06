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

    var errorMessage by mutableStateOf("")
        private set

    fun toggleProfilePage() {
        isProfileOpen = !isProfileOpen
    }

    fun onRecommendPageClick() {
        isRecommendOpen = true
    }

    fun onDismissRecommend() {
        isRecommendOpen = false
        isHamburgerOpen = false
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

    fun changeCoverQuality (value: String) {
        userModel.coverQuality = value
        runBlocking {
            dbManager.setCoverQuality(userModel, value)
        }
    }

    /*
    fun statusFilter(status: String) {
        if (status == "All") {
            displayedBooks = bookLibrary.toMutableList()
        } else {
            displayedBooks = bookLibrary.filter { it.status.contains(status, ignoreCase = true) }.toMutableList()
        }
    }*/

    fun filter(sortType: String, status: String, searchValue: String) {
        displayedBooks = when(sortType) {
            "Title" -> bookLibrary.sortedBy { it.title }.toMutableList()
            "Author" -> bookLibrary.sortedBy { it.author }.toMutableList()
            "Recently Added" -> bookLibrary.reversed().toMutableList()
            else -> bookLibrary.toMutableList()
        }
        if (status != "All") {
            displayedBooks = displayedBooks.filter { it.status.contains(status, ignoreCase = true) }.toMutableList()
        }
        if(searchValue.isNotBlank()) {
            displayedBooks = displayedBooks.filter { it.title.contains(searchValue, ignoreCase = true) }.toMutableList()
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

    /*
    fun searchResults(searchValue: String) {
        if (searchValue.isNotBlank()) {
            displayedBooks = bookLibrary.filter { it.title.contains(searchValue, ignoreCase = true) }.toMutableList()
        } else {
            displayedBooks = bookLibrary.toMutableList() // Reset to original bookLibrary if search is empty
        }
    }*/

    fun refreshDisplay() {
        displayedBooks = bookLibrary.toMutableList()
    }

    fun toggleErrorMessage(str: String) {
        errorMessage = str
    }
}