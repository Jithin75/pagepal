package viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import org.example.model.BookModel


class RecommendationViewModel () {
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
}