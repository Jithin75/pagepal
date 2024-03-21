package viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import org.example.model.BookModel

class BookViewModel (val bookModel: BookModel){

    var isEditOpen by mutableStateOf(false)
        private set

    fun onEditClick() {
        isEditOpen = true
    }

    fun onDismissEdit() {
        isEditOpen = false
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
        return bookModel.summary
    }

    fun getStatus(): String {
        // to be implemented
        return "New"
    }
}