package viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.runBlocking
import model.BookModel
import model.DatabaseManager
import model.UserModel


class MainPageViewModel (val userModel: UserModel,
                         val bookLibrary : MutableList<BookModel>,
                         val dbManager: DatabaseManager) {
    var displayedBooks by mutableStateOf(mutableListOf<BookModel>())
        private set

    var isFilterChanged by mutableStateOf(true)

    var sortSelected by mutableStateOf("Default")
        private set

    var statusSelected by mutableStateOf("All")
        private set

    var searchContent by mutableStateOf("")
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

    fun searchContentEntered(entry: String) {
        searchContent = entry
    }

    fun newSortSelected(entry: String) {
        sortSelected = entry
    }

    fun newStatusSelected(entry: String) {
        statusSelected = entry
    }

    fun toggleProfilePage() {
        isProfileOpen = !isProfileOpen
        if(!isProfileOpen) {
            isHamburgerOpen = false
        }
    }

    fun onRecommendPageClick() {
        isRecommendOpen = true
    }

    fun onDismissRecommend() {
        isRecommendOpen = false
        isHamburgerOpen = false
        isFilterChanged = true
        filter()
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

    fun filterChanged() {
        isFilterChanged = true
    }

    fun filter() {
        if(isFilterChanged) {
            displayedBooks = when (sortSelected) {
                "Title" -> bookLibrary.sortedBy { it.title }.toMutableList()
                "Author" -> bookLibrary.sortedBy { it.author }.toMutableList()
                "Recently Added" -> bookLibrary.reversed().toMutableList()
                else -> bookLibrary.toMutableList()
            }
            if (statusSelected != "All") {
                val filteredBooks = displayedBooks.filter { it.status.contains(statusSelected, ignoreCase = true) }
                displayedBooks.clear()
                displayedBooks.addAll(filteredBooks)
            }
            if (searchContent != "") {
                val filteredBooks = displayedBooks.filter { it.title.contains(searchContent, ignoreCase = true) }
                displayedBooks.clear()
                displayedBooks.addAll(filteredBooks)
            }
            isFilterChanged = false
        }
    }

    fun getUserLibrary(): MutableList<BookModel> {
        return displayedBooks
    }

    fun getUserBookList(): MutableList<BookModel> {
        return bookLibrary
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

    fun refreshDisplay() {
        displayedBooks = bookLibrary.toMutableList()
    }

    fun toggleErrorMessage(str: String) {
        errorMessage = str
    }
}