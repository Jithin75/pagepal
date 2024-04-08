package viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import model.AIRecommender
import model.api.BookApiClient
import model.BookModel
import org.json.JSONArray
import org.json.JSONObject


class RecommendationViewModel () {
    var isBookOpen by mutableStateOf(false)
        private set

    var bookOpened by mutableStateOf<BookModel?>(null)
        private set

    var isLoading by mutableStateOf(true)
        private set

    var displayedBooks by mutableStateOf<List<BookModel>>(emptyList())
        private set

    fun initiateDisplayedBooks(mainPageViewModel: MainPageViewModel) {
        try {
            val userBooks = mainPageViewModel.getUserBookList()

            val question = StringBuilder()

            userBooks.forEachIndexed { index, book ->
                // Append the title and authors to the question string
                val strippedTitle = book.title.replace("\"","\\\"")
                val strippedAuthor = book.author.replace("\"","\\\"")
                question.append("$strippedTitle: $strippedAuthor")

                // Append newline character except for the last book
                if (index < userBooks.size - 1) {
                    question.append("\\n")
                }
            }

            // Pass the constructed question string to the getResponse function
            val response = AIRecommender.getResponse(question.toString(), 10)
            val jsonObject = JSONObject(response)
            val jsonArray: JSONArray = jsonObject.getJSONArray("recommendations")
            val bookTitles = mutableListOf<String>()
            for (i in 0 until jsonArray.length()) {
                val bookObject = jsonArray.getJSONObject(i)
                val title = bookObject.getString("title")
                bookTitles.add(title)
            }

            val bookClient = BookApiClient()
            val books = mutableListOf<BookModel>()

            for (title in bookTitles) {
                val book : BookModel = bookClient.searchBook(title)
                val pattern = Regex("&zoom=\\d+")
                val replacement = "&zoom=${mainPageViewModel.userModel.coverQuality}"
                val cover = book.cover
                book.cover = cover.replace(pattern, replacement)
                books.add(book)
            }

            displayedBooks = books // Update displayedBooks
        } catch (e: Exception) {
            println(e)
        } finally {
            isLoading = false // Hide loading screen when tasks complete
        }
    }

    fun onBookClick(bookModel: BookModel) {
        isBookOpen = true
        bookOpened = bookModel
    }

    fun onDismissBook() {
        isBookOpen = false
        bookOpened = null
    }

    fun onBookAdded() {
        displayedBooks = displayedBooks.minus(bookOpened!!)
        isBookOpen = false
        bookOpened = null
    }
}