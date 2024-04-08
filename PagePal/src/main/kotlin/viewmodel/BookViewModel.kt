package viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.runBlocking
import model.BookModel
import model.DatabaseManager

class BookViewModel (val bookModel: BookModel, val dbManager: DatabaseManager){
    var isEditOpen by mutableStateOf(false)
        private set

    var showConfirmationDialog by mutableStateOf(false)
        private set

    var isAddOpen by mutableStateOf(false)
        private set

    fun onEditClick() {
        isEditOpen = true
    }

    fun onDismissEdit() {
        isEditOpen = false
    }

    fun onAddClick() {
        isAddOpen = true
    }

    fun onDismissAdd() {
        isAddOpen = false
    }

    fun getCover(): String {
        return bookModel.cover
    }

    fun getTitle(): String {
        return bookModel.title
    }

    fun getAuthor(): String {
        return bookModel.author
    }

    fun getSummary(): String {
        return bookModel.description
    }

    fun getStatus(): String {
        return bookModel.status
    }

    fun setStatus(status: String) {
        bookModel.status = status
        runBlocking {
            dbManager.setBookStatus(bookModel, status)
        }
    }

    fun getChapter(): String {
        return bookModel.chapter
    }

    fun setChapter(chapter: String) {
        bookModel.chapter = chapter
        runBlocking {
            dbManager.setBookChapter(bookModel, chapter)
        }
    }

    fun getPage(): String {
        return bookModel.page
    }

    fun setPage(page: String) {
        bookModel.page = page
        runBlocking {
            dbManager.setBookPage(bookModel, page)
        }
    }

    fun deleteBook(username: String) {
        runBlocking {
            dbManager.removeBookFromUser(username, bookModel.bookId)
            dbManager.removeBook(bookModel.bookId)
        }
    }
    fun toggleShowConfirmationDialog(bool : Boolean) {
        showConfirmationDialog = bool
    }
}