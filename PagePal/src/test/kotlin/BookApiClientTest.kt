import model.api.Book
import model.api.BookApiClient
import model.BookModel
import org.junit.Test


class BookApiClientTest {
    private val apiClient = BookApiClient()
    @Test
    fun searchBooksTest() {
        val query1 = "dune"
        val query2 = ""
        val query3 = "sdklcmkscmkd"

        val bookList1 = apiClient.searchBooks(query1)
        val bookList2 = apiClient.searchBooks(query2)
        val bookList3 = apiClient.searchBooks(query3)

        assert(bookList1.size == 10)
        assert(bookList2.isEmpty())
        assert(bookList3.isEmpty())
    }

    @Test
    fun searchBookTest() {
        val query1 = "dune"
        val query2 = ""
        val query3 = "sdklcmkscmkd"

        val book1 = apiClient.searchBook(query1)
        val book2 = apiClient.searchBook(query2)
        val book3 = apiClient.searchBook(query3)

        assert(book1.title == "Dune")
        assert(book2.title == "")
        assert(book3.title == "")
    }
}