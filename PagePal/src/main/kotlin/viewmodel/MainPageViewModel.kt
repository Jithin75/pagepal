package org.example.viewmodel

import androidx.compose.runtime.*
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.runBlocking
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider
import org.example.model.BookModel
import org.example.model.DatabaseManager
import org.example.model.UserModel
import org.example.view.SearchBar
import java.io.File
import java.io.FileOutputStream
import javax.imageio.ImageIO
import java.net.URL
import java.nio.channels.Channels
import java.awt.Image
import java.awt.image.BufferedImage


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

    fun toggleProfilePage() {
        isProfileOpen = !isProfileOpen
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