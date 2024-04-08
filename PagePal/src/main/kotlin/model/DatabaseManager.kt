package model

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.conversions.Bson
import org.bson.types.ObjectId

class DatabaseManager(private val database: MongoDatabase) {
    private val userCollection: MongoCollection<UserModel> = database.getCollection("UserCollection")
    private val bookCollection: MongoCollection<BookModel> = database.getCollection("BookCollection")

    suspend fun addBook(book: BookModel): String {
        book.bookId = ObjectId().toHexString()
        bookCollection.insertOne(book).also {
            println("Inserted Book - ${it.insertedId}")
        }
        return book.bookId
    }

    suspend fun addUser(user: UserModel) {
        user.password = PasswordEncryption.hashPassword(user.password)

        val result = userCollection.insertOne(user)
        println("Inserted User - ${result.insertedId}")
    }

    suspend fun updateUserBookList(username: String, newBook: String) {
        val filter: Bson = Filters.eq(UserModel::username.name, username)
        val update = Updates.addToSet(UserModel::library.name, newBook)
        val result = userCollection.updateOne(filter, update)
        println("Matched docs ${result.matchedCount} and updated docs ${result.modifiedCount}")
    }

    suspend fun removeBookFromUser(username: String, bookToRemove: String) {
        val filter: Bson = Filters.eq(UserModel::username.name, username)
        val update = Updates.pull(UserModel::library.name, bookToRemove)
        val result = userCollection.updateOne(filter, update)
        println("Matched docs ${result.matchedCount} and updated docs ${result.modifiedCount}")
    }

    suspend fun removeBook(bookId: String) {
        val filter = Filters.eq("bookId", bookId)
        val result = bookCollection.deleteOne(filter)
        println("Deleted ${result.deletedCount} document(s) from BookCollection")
    }

    suspend fun deleteUser(username: String) {
        val user = getUserByUsername(username)
        if (user != null) {
            for (bookId in user.library) {
                removeBook(bookId)
            }
            val filter: Bson = Filters.eq(UserModel::username.name, username)
            val result = userCollection.deleteOne(filter)
            println("Deleted count ${result.deletedCount}")
        } else {
            println("User with username $username not found.")
        }
    }

    suspend fun getUserByUsername(username: String): UserModel? {
        return userCollection.find(Filters.eq(UserModel::username.name, username)).firstOrNull()
    }

    suspend fun getBooksByAuthor(author: String): List<BookModel> {
        return bookCollection.find(Filters.eq(BookModel::author.name, author)).toList()
    }

    suspend fun updateUsername(currentUsername: String, newUsername: String) {
        val filter = Filters.eq(UserModel::username.name, currentUsername)
        val update = Updates.set(UserModel::username.name, newUsername)
        userCollection.updateOne(filter, update).also { result ->
            println("Updated ${result.modifiedCount} document(s)")
        }
    }

    suspend fun changePassword(username: String, currentPassword: String, newPassword: String): Boolean {
        if (!isValidCredentials(username, currentPassword)) {
            println("Invalid credentials")
            return false
        }
        val hashedNewPassword = PasswordEncryption.hashPassword(newPassword)
        val filter = Filters.eq(UserModel::username.name, username)
        val update = Updates.set(UserModel::password.name, hashedNewPassword)
        val result = userCollection.updateOne(filter, update)
        println("Updated ${result.modifiedCount} document(s)")
        return true
    }

    suspend fun isValidCredentials(username: String, password: String): Boolean {
        val filter = Filters.eq(UserModel::username.name, username)
        val user : UserModel? = userCollection.find(filter).limit(1).firstOrNull()
        return user != null && PasswordEncryption.verifyPassword(password, user.password)
    }

    suspend fun getBookById(bookId: String): BookModel? {
        return bookCollection.find(Filters.eq("bookId", bookId)).firstOrNull()
    }

    suspend fun getUserLibraryDB(user: UserModel): MutableList<BookModel> {
        val library = mutableListOf<BookModel>()
        for (bookId in user.library) {
            val book = getBookById(bookId)
            if (book != null) {
                library.add(book)
            } else {
                println("Book with ID $bookId not found.")
            }
        }
        return library
    }

    suspend fun setBookStatus(book: BookModel, newStatus: String) {
        val filter = Filters.eq(BookModel::bookId.name, book.bookId)
        val update = Updates.set(BookModel::status.name, newStatus)
        val result = bookCollection.updateOne(filter, update)
        println("Updated ${result.modifiedCount} document(s)")
    }

    suspend fun setBookChapter(book: BookModel, newChapter: String) {
        val filter = Filters.eq(BookModel::bookId.name, book.bookId)
        val update = Updates.set(BookModel::chapter.name, newChapter)
        val result = bookCollection.updateOne(filter, update)
        println("Updated ${result.modifiedCount} document(s)")
    }

    suspend fun setBookPage(book: BookModel, newPage: String) {
        val filter = Filters.eq(BookModel::bookId.name, book.bookId)
        val update = Updates.set(BookModel::page.name, newPage)
        val result = bookCollection.updateOne(filter, update)
        println("Updated ${result.modifiedCount} document(s)")
    }

    suspend fun setCoverQuality(user: UserModel, newQuality: String) {
        val filter = Filters.eq(UserModel::username.name, user.username)
        val update = Updates.set(UserModel::coverQuality.name, newQuality)
        val result = userCollection.updateOne(filter, update)
        println("Updated ${result.modifiedCount} document(s)")
    }
}
