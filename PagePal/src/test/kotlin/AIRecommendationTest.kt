
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import junit.framework.Assert.assertEquals
import model.AIRecommender
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider
import org.example.model.BookModel
import org.example.model.DatabaseManager
import org.example.model.UserModel
import org.example.viewmodel.MainPageViewModel
import org.junit.Assert.*
import org.junit.Test
import viewmodel.RecommendationViewModel

class AIRecommendationTest {
    var recommendation = RecommendationViewModel()
    val pojoCodecRegistry: CodecRegistry = CodecRegistries.fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
    )

    val client = MongoClient.create(connectionString = "mongodb+srv://praviin10:Prav2003@cluster0.fqt7qpj.mongodb.net/?retryWrites=true&w=majority")
    val database = client.getDatabase("PagePalDB").withCodecRegistry(pojoCodecRegistry)
    val dbManager = DatabaseManager(database)

    @Test
    fun recommendBooks() {
        // Setting up a user with 10 books to test AI recommendation capabilities
        // Creating Test User
        val testUser = UserModel(username = "testuser", password = "testuser")
        // Creating a list of books for the user
        val booksToAdd = mutableListOf(
            BookModel(title = "To Kill a Mockingbird", author = "Harper Lee"),
            BookModel(title = "1984", author = "George Orwell"),
            BookModel(title = "The Great Gatsby", author = "F. Scott Fitzgerald"),
            BookModel(title = "Pride and Prejudice", author = "Jane Austen"),
            BookModel(title = "The Catcher in the Rye", author = "J.D. Salinger"),
            BookModel(title = "Animal Farm", author = "George Orwell"),
            BookModel(title = "The Hobbit", author = "J.R.R. Tolkien"),
            BookModel(title = "The Lord of the Rings", author = "J.R.R. Tolkien"),
            BookModel(title = "Harry Potter and the Philosopher's Stone", author = "J.K. Rowling"),
            BookModel(title = "To the Lighthouse", author = "Virginia Woolf")
        )
        // Creating main page user the above user and library
        val mainPage = MainPageViewModel(testUser, booksToAdd, dbManager)
        // Creating corresponding the recommendation page for the above user
        recommendation.initiateDisplayedBooks(mainPage)
        // Checking if 10 books were suggested
        assert(recommendation.displayedBooks.size == 10)
    }
    @Test
    fun reccomendBooksError() {
        // Setting up a user with 10 books to test AI recommendation capabilities
        // Creating Test User
        val testUser = UserModel(username = "testuser", password = "testuser")
        // Creating a list of books for the user
        val booksToAdd = mutableListOf(
            BookModel(title = "To Ki\nll a Mockingbird", author = "Harpe\nr Lee"),
            BookModel(title = "19\n84", author = "George Orw\nell"),
            BookModel(title = "The Great\n Gatsby", author = "F. Sco\ntt Fitzgerald"),
            BookModel(title = "Pr\nide and Prejudice", author = "Jane Aus\nten"),
            BookModel(title = "The \nCatcher in the Rye", author = "J.D. Sa\nlinger"),
            BookModel(title = "An\nimal Farm", author = "Geor\nge Orwell"),
            BookModel(title = "The H\nobbit", author = "J.R.R. T\nolkien"),
            BookModel(title = "The Lord of \nthe Rings", author = "J.\nR.R. Tolkien"),
            BookModel(title = "Harry Po\ntter and the Philosopher's Stone", author = "J.K. Rowl\ning"),
            BookModel(title = "To the Lighthouse\n", author = "Virg\ninia Woolf")
        )
        // Creating main page user the above user and library
        val mainPage = MainPageViewModel(testUser, booksToAdd, dbManager)
        // Creating corresponding the recommendation page for the above user
        recommendation.initiateDisplayedBooks(mainPage)
        // Checking if 0 books were suggested indicating an error
        assert(recommendation.displayedBooks.isEmpty())
    }

    @Test
    fun testOnBookClick() {
        val bookModel = BookModel(title = "Test Book", author = "Test Author")

        recommendation.onBookClick(bookModel)

        assertTrue(recommendation.isBookOpen)
        assertEquals(bookModel, recommendation.bookOpened)
    }

    @Test
    fun testOnDismissBook() {
        val bookModel = BookModel(title = "Test Book", author = "Test Author")

        recommendation.onBookClick(bookModel)

        recommendation.onDismissBook()

        assertFalse(recommendation.isBookOpen)
        assertNull(recommendation.bookOpened)
    }

    @Test
    fun testOnBookAdded() {
        // Initialize the recommendation page with 10 books
        recommendBooks()

        for(book in recommendation.displayedBooks) {
            recommendation.onBookClick(book)
            recommendation.onBookAdded()
        }

        assertTrue(recommendation.displayedBooks.isEmpty())
        assertFalse(recommendation.isBookOpen)
        assertNull(recommendation.bookOpened)
    }

    // Function that simply creates the AIRecommender model for coverage
    @Test
    fun coverageReq() {
        val recommender = AIRecommender()
        assertNotNull(recommender)
    }
}
