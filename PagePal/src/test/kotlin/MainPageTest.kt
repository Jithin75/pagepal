
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import model.BookModel
import model.DatabaseManager
import model.UserModel
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider
import org.junit.Test
import viewmodel.MainPageViewModel
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MainPageTest {
    val pojoCodecRegistry: CodecRegistry = CodecRegistries.fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
    )

    val client = MongoClient.create(connectionString = "mongodb+srv://praviin10:Prav2003@cluster0.fqt7qpj.mongodb.net/?retryWrites=true&w=majority")
    val database = client.getDatabase("PagePalDB").withCodecRegistry(pojoCodecRegistry);
    val dbManager = DatabaseManager(database)

    val testUser = UserModel(username = "testuser", password = "testuser")
    val booksToAdd = mutableListOf(
        BookModel(title = "To Kill a Mockingbird", author = "Harper Lee"),
        BookModel(title = "1984", author = "George Orwell"),
        BookModel(title = "The Great Gatsby", author = "F. Scott Fitzgerald"),
    )
    val mainPage = MainPageViewModel(testUser, booksToAdd, dbManager)

    @Test
    fun searchContentEntered() {
        mainPage.searchContentEntered("1984")
        assertEquals(mainPage.searchContent, "1984")
    }

    @Test
    fun newSortSelected() {
        mainPage.newSortSelected("Title")
        assertEquals(mainPage.sortSelected, "Title")
    }

    @Test
    fun newStatusSelected() {
        mainPage.newStatusSelected("Completed")
        assertEquals(mainPage.statusSelected, "Completed")
    }

    @Test
    fun toggleProfilePage() {
        assertFalse(mainPage.isProfileOpen)
        mainPage.toggleProfilePage()
        assertTrue(mainPage.isProfileOpen)
        assertFalse(mainPage.isHamburgerOpen)
    }

    @Test
    fun onRecommendPageClick() {
        mainPage.onRecommendPageClick()
        assertTrue(mainPage.isRecommendOpen)
    }

    @Test
    fun onDismissRecommend() {
        mainPage.onDismissRecommend()
        assertFalse(mainPage.isRecommendOpen)
        assertFalse(mainPage.isHamburgerOpen)
        assertFalse(mainPage.isFilterChanged)
    }

    @Test
    fun onBookClick() {
        val book = BookModel(title = "1984", author = "George Orwell")
        mainPage.onBookClick(book)
        assertEquals(book, mainPage.bookOpened)
        assertTrue(mainPage.isBookOpen)
    }

    @Test
    fun onDismissBook() {
        mainPage.onDismissBook()
        assertFalse(mainPage.isBookOpen)
        assertNull(mainPage.bookOpened)
    }

    @Test
    fun onHamburgerClick() {
        mainPage.onHamburgerClick()
        assertTrue(mainPage.isHamburgerOpen)
    }

    @Test
    fun onDismissHamburger() {
        mainPage.onDismissHamburger()
        assertFalse(mainPage.isHamburgerOpen)
    }

    @Test
    fun onAddBookClick() {
        mainPage.onAddBookClick()
        assertTrue(mainPage.isAddBookOpen)
    }

    @Test
    fun onDismissAddBook() {
        mainPage.onDismissAddBook()
        assertFalse(mainPage.isAddBookOpen)
    }

    @Test
    fun changeCoverQuality () {
        mainPage.changeCoverQuality("1")
        assertEquals(mainPage.userModel.coverQuality, "1")
        mainPage.changeCoverQuality("2")
        assertEquals(mainPage.userModel.coverQuality, "2")
        mainPage.changeCoverQuality("3")
        assertEquals(mainPage.userModel.coverQuality, "3")
    }

    @Test
    fun filterChanged() {
        mainPage.filterChanged()
        assertTrue(mainPage.isFilterChanged)
    }

    @Test
    fun filter() {
        mainPage.filter()
        assertEquals(booksToAdd.map { it.copy(page = "", chapter = "", status = "") }, mainPage.displayedBooks.map { it.copy(page = "", chapter = "", status = "") })
        mainPage.newSortSelected("Title")
        val sortByTitle = mutableListOf(
            BookModel(title = "1984", author = "George Orwell"),
            BookModel(title = "The Great Gatsby", author = "F. Scott Fitzgerald"),
            BookModel(title = "To Kill a Mockingbird", author = "Harper Lee"),
        )
        mainPage.filterChanged()
        mainPage.filter()
        assertEquals(sortByTitle.map { it.copy(page = "", chapter = "", status = "") }, mainPage.displayedBooks.map { it.copy(page = "", chapter = "", status = "") })
        mainPage.searchContentEntered("T")
        val sortByTitleAndSearchT = mutableListOf(
            BookModel(title = "The Great Gatsby", author = "F. Scott Fitzgerald"),
            BookModel(title = "To Kill a Mockingbird", author = "Harper Lee"),
        )
        mainPage.filterChanged()
        mainPage.filter()
        assertEquals(sortByTitleAndSearchT.map { it.copy(page = "", chapter = "", status = "") }, mainPage.displayedBooks.map { it.copy(page = "", chapter = "", status = "") })
    }

    @Test
    fun getUserLibrary(){
        mainPage.refreshDisplay()
        val bookList: MutableList<BookModel> = mainPage.getUserLibrary()
        assertEquals(bookList, booksToAdd)
    }

    @Test
    fun getUserBookList() {
        val bookList: MutableList<BookModel> = mainPage.getUserBookList()
        assertEquals(bookList, booksToAdd)
    }

    @Test
    fun addBook() {
        val testbook = BookModel(title = "test", author = "test")
        val newBooks = mutableListOf(
            BookModel(title = "To Kill a Mockingbird", author = "Harper Lee"),
            BookModel(title = "1984", author = "George Orwell"),
            BookModel(title = "The Great Gatsby", author = "F. Scott Fitzgerald"),
            BookModel(title = "test", author = "test"),
        )
        mainPage.addBook(testbook)
        assertEquals(newBooks.map { it.copy(page = "", chapter = "", status = "", bookId = "") }, mainPage.bookLibrary.map { it.copy(page = "", chapter = "", status = "", bookId = "") })
    }

    @Test
    fun refreshDisplay() {
        mainPage.refreshDisplay()
        assertEquals(mainPage.displayedBooks, mainPage.bookLibrary.toMutableList())
    }

    @Test
    fun toggleErrorMessage() {
        mainPage.toggleErrorMessage("Error")
        assertEquals(mainPage.errorMessage, "Error")
    }
}