
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.runBlocking
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider
import model.BookModel
import model.DatabaseManager
import model.UserModel
import org.junit.Assert.*
import org.junit.Test

class DatabaseManagerTest {
    val pojoCodecRegistry: CodecRegistry = CodecRegistries.fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
    )

    val client = MongoClient.create(connectionString = "mongodb+srv://praviin10:Prav2003@cluster0.fqt7qpj.mongodb.net/?retryWrites=true&w=majority")
    val database = client.getDatabase("PagePalDB").withCodecRegistry(pojoCodecRegistry);
    val dbManager = DatabaseManager(database)


    @Test
    fun addBook() {
        runBlocking {
            // Add a book to the database
            val bookId = dbManager.addBook(BookModel(title = "Test Book", author = "Test Author"))
            val book = dbManager.getBookById(bookId)
            assertEquals("Test Book", book?.title)
            assertEquals("Test Author", book?.author)
            dbManager.removeBook(bookId)
        }
    }

    @Test
    fun addUser() {
        runBlocking {
            val user = UserModel(username = "testUser", password = "password")
            dbManager.addUser(user)
            val retrievedUser = dbManager.getUserByUsername("testUser")
            assertEquals(user.username, retrievedUser?.username)
            dbManager.deleteUser(user.username)
        }
    }

    @Test
    fun updateUserBookList() {
        runBlocking {
            val user = UserModel(username = "testUser", password = "password")
            val bookId = dbManager.addBook(BookModel(title = "Test Book", author = "Test Author"))
            dbManager.addUser(user)
            dbManager.updateUserBookList("testUser", bookId)
            val updatedUser = dbManager.getUserByUsername("testUser")
            assertTrue(updatedUser?.library?.contains(bookId) ?: false)
            dbManager.deleteUser(user.username)
        }
    }

    @Test
    fun removeBookFromUser() {
        runBlocking {
            val user = UserModel(username = "testUser", password = "password")
            val bookId = dbManager.addBook(BookModel(title = "Test Book", author = "Test Author"))
            dbManager.addUser(user)
            dbManager.updateUserBookList("testUser", bookId)
            dbManager.removeBookFromUser("testUser", bookId)
            val updatedUser = dbManager.getUserByUsername("testUser")
            assertFalse(updatedUser?.library?.contains(bookId) ?: false)
            dbManager.deleteUser(user.username)
        }
    }

    @Test
    fun removeBook() {
        runBlocking {
            val bookId = dbManager.addBook(BookModel(title = "Test Book", author = "Test Author"))
            dbManager.removeBook(bookId)
            val book = dbManager.getBookById(bookId)
            assertNull(book)
        }
    }

    @Test
    fun deleteUser() {
        runBlocking {
            val user = UserModel(username = "testUser", password = "password")
            dbManager.addUser(user)
            dbManager.deleteUser("testUser")
            var retrievedUser = dbManager.getUserByUsername("testUser")
            assertNull(retrievedUser)
            dbManager.deleteUser("testUser")
            retrievedUser = dbManager.getUserByUsername("testUser")
            assertNull(retrievedUser)
        }
    }

    @Test
    fun getUserByUsername() {
        runBlocking {
            val user = UserModel(username = "testUser", password = "password")
            dbManager.addUser(user)
            var retrievedUser = dbManager.getUserByUsername("testUser")
            assertEquals(user.username, retrievedUser?.username)
            retrievedUser = dbManager.getUserByUsername("nonExistingUser")
            assertNull(retrievedUser?.username)
            dbManager.deleteUser(user.username)
        }
    }

    @Test
    fun getBooksByAuthor() {
        runBlocking {
            val book1 = BookModel(title = "Book 1", author = "Author A")
            val book2 = BookModel(title = "Book 2", author = "Author A")
            val book3 = BookModel(title = "Book 3", author = "Author B")
            val bookId1 = dbManager.addBook(book1)
            val bookId2 = dbManager.addBook(book2)
            val bookId3 = dbManager.addBook(book3)
            val booksByAuthorA = dbManager.getBooksByAuthor("Author A")
            val booksByAuthorB = dbManager.getBooksByAuthor("Author B")
            val booksByAuthorC = dbManager.getBooksByAuthor("Author C")
            assertEquals(2, booksByAuthorA.size)
            assertEquals(1, booksByAuthorB.size)
            assertEquals(0, booksByAuthorC.size)
            dbManager.removeBook(bookId1)
            dbManager.removeBook(bookId2)
            dbManager.removeBook(bookId3)

        }
    }


    @Test
    fun updateUsername() {
        runBlocking {
            val user = UserModel(username = "oldUsername", password = "password")
            dbManager.addUser(user)
            dbManager.updateUsername("oldUsername", "newUsername")
            val updatedUser = dbManager.getUserByUsername("newUsername")
            assertEquals("newUsername", updatedUser?.username)
            dbManager.deleteUser("newUsername")
        }
    }

    @Test
    fun changePassword() {
        runBlocking {
            val user = UserModel(username = "testUser", password = "oldPassword")
            dbManager.addUser(user)
            dbManager.changePassword("testUser", "oldPassword", "newPassword")
            val updatedUser = dbManager.getUserByUsername("testUser")
            val valid = updatedUser?.let { dbManager.isValidCredentials(it.username, "newPassword") }
            val notValid = updatedUser?.let { dbManager.isValidCredentials(it.username, "oldPassword") }
            if (valid != null) {
                assertTrue(valid)
            }
            if (notValid != null) {
                assertFalse(notValid)
            }
            dbManager.deleteUser(user.username)
        }
    }

    @Test
    fun isValidCredentials() {
        runBlocking {
            val user = UserModel(username = "testUser", password = "password")
            dbManager.addUser(user)
            val trueValidCredentials = dbManager.isValidCredentials("testUser", "password")
            val falseValidCredentials = dbManager.isValidCredentials("testUser2", "password")
            val falseValidCredentials2 = dbManager.isValidCredentials("testUser", "password2")
            assertTrue(trueValidCredentials)
            assertFalse(falseValidCredentials)
            assertFalse(falseValidCredentials2)
            dbManager.deleteUser(user.username)
        }
    }

    @Test
    fun getBookById() {
        runBlocking {
            val book = BookModel(title = "Test Book", author = "Test Author")
            val bookId = dbManager.addBook(book)
            val retrievedBook = dbManager.getBookById(bookId)
            assertEquals(book.title, retrievedBook?.title)
            assertEquals(book.author, retrievedBook?.author)
            assertEquals(book.bookId, retrievedBook?.bookId)
            dbManager.removeBook(bookId)
        }
    }

    @Test
    fun getUserLibraryDB() {
        runBlocking {
            val user = UserModel(username = "testUser2", password = "password")
            dbManager.addUser(user)
            val userLibraryBefore = dbManager.getUserLibraryDB(user)
            assertEquals(0, userLibraryBefore.size)
            val book1 = BookModel(title = "Book1", author = "Author A")
            val book2 = BookModel(title = "Book2", author = "Author B")
            val book1Id = dbManager.addBook(book1)
            val book2Id = dbManager.addBook(book2)
            dbManager.updateUserBookList("testUser2", book1Id)
            dbManager.updateUserBookList("testUser2", book2Id)
            val updatedUser = dbManager.getUserByUsername("testUser2")
            val userLibrary = updatedUser?.let { dbManager.getUserLibraryDB(it) }
            if (userLibrary != null) {
                assertEquals(2, userLibrary.size)
            }
            dbManager.deleteUser(user.username)
        }
    }

    @Test
    fun setBookStatus() {
        runBlocking {
            val book = BookModel(title = "Test Book", author = "Test Author")
            val bookId = dbManager.addBook(book)
            dbManager.setBookStatus(book, "In Progress")
            var updatedBook = dbManager.getBookById(bookId)
            assertEquals("In Progress", updatedBook?.status)
            dbManager.setBookStatus(book, "New")
            updatedBook = dbManager.getBookById(bookId)
            assertEquals("New", updatedBook?.status)
            dbManager.setBookStatus(book, "Completed")
            updatedBook = dbManager.getBookById(bookId)
            assertEquals("Completed", updatedBook?.status)
            dbManager.removeBook(bookId)
        }
    }

    @Test
    fun setBookChapter() {
        runBlocking {
            val book = BookModel(title = "Test Book", author = "Test Author")
            val bookId = dbManager.addBook(book)
            dbManager.setBookChapter(book, "1")
            var updatedBook = dbManager.getBookById(bookId)
            assertEquals("1", updatedBook?.chapter)
            dbManager.setBookChapter(book, "24")
            updatedBook = dbManager.getBookById(bookId)
            assertEquals("24", updatedBook?.chapter)
            dbManager.removeBook(bookId)
        }
    }

    @Test
    fun setBookPage() {
        runBlocking {
            val book = BookModel(title = "Test Book", author = "Test Author")
            val bookId = dbManager.addBook(book)
            dbManager.setBookPage(book, "10")
            var updatedBook = dbManager.getBookById(bookId)
            assertEquals("10", updatedBook?.page)
            dbManager.setBookPage(book, "3")
            updatedBook = dbManager.getBookById(bookId)
            assertEquals("3", updatedBook?.page)
            dbManager.removeBook(bookId)
        }
    }

    @Test
    fun setCoverQuality() {
        runBlocking {
            val user = UserModel(username = "testUser", password = "password")
            dbManager.addUser(user)
            assertEquals(user.coverQuality, "1")
            dbManager.setCoverQuality(user, "2")
            var updatedUser = dbManager.getUserByUsername("testUser")
            assertEquals(updatedUser?.coverQuality, "2")
            dbManager.setCoverQuality(user, "3")
            updatedUser = dbManager.getUserByUsername("testUser")
            assertEquals(updatedUser?.coverQuality, "3")
            dbManager.deleteUser(user.username)
        }
    }
}