package org.example.model

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.BsonDocument
import org.bson.BsonValue
import org.bson.conversions.Bson

class DatabaseManager(private val database: MongoDatabase) {
    private val userCollection: MongoCollection<UserModel> = database.getCollection("UserCollection")
    private val bookCollection: MongoCollection<BookModel> = database.getCollection("BookCollection")

    suspend fun addBook(book: BookModel): BsonValue {
        var bookId: BsonValue
        bookCollection.insertOne(book).also {
            println("Inserted Book - ${it.insertedId}")
            bookId = it.insertedId
        }
        return bookId
    }

    suspend fun addUser(user: UserModel) {
        val result = userCollection.insertOne(user)
        println("Inserted User - ${result.insertedId}")
    }

    suspend fun readUser() {
        userCollection.find<UserModel>().collect { user ->
            println("${user.username}'s Book List:")
            user.library.forEach { bookId ->
                val book = bookCollection.find(Filters.eq("_id", bookId)).firstOrNull()
                println("${book?.title} by ${book?.author}")
            }
        }
    }

    suspend fun updateUserBookList(username: String, newBook: BsonValue) {
        val filter: Bson = Filters.eq(UserModel::username.name, username)
        val update = Updates.addToSet(UserModel::library.name, newBook)
        val result = userCollection.updateOne(filter, update)
        println("Matched docs ${result.matchedCount} and updated docs ${result.modifiedCount}")
    }

    suspend fun removeBookFromUser(username: String, bookToRemove: BsonValue) {
        val filter: Bson = Filters.eq(UserModel::username.name, username)
        val update = Updates.pull(UserModel::library.name, bookToRemove)
        val result = userCollection.updateOne(filter, update)
        println("Matched docs ${result.matchedCount} and updated docs ${result.modifiedCount}")
    }

    suspend fun deleteUser(username: String) {
        val filter: Bson = Filters.eq(UserModel::username.name, username)
        val result = userCollection.deleteOne(filter)
        println("Deleted count ${result.deletedCount}")
    }

    suspend fun getUserByUsername(username: String): UserModel? {
        return userCollection.find(Filters.eq(UserModel::username.name, username)).firstOrNull()
    }

    suspend fun getAllUsers(): List<UserModel> {
        return userCollection.find().toList()
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
        val filter = Filters.eq(UserModel::username.name, username)
        val update = Updates.set(UserModel::password.name, newPassword)
        val result = userCollection.updateOne(filter, update)
        println("Updated ${result.modifiedCount} document(s)")
        return true
    }

    suspend fun isValidCredentials(username: String, password: String): Boolean {
        val filter = Filters.eq(UserModel::username.name, username)
        val user : UserModel? = userCollection.find(filter).limit(1).firstOrNull()
        return user?.password == password
    }

    suspend fun getBookById(bookId: BsonValue): BookModel? {
        return bookCollection.find(Filters.eq("_id", bookId)).firstOrNull()
    }

    /*
    suspend fun addBooksToUser(username: String, bookIds: List<BsonValue>) {
        val user = getUserByUsername(username)
        if (user != null) {
            user.library.addAll(bookIds)
            updateUserLibrary(username, user.library)
        }
    }*/

    private suspend fun updateUserLibrary(username: String, updatedLibrary: MutableList<BsonValue>) {
        val filter: Bson = Filters.eq(UserModel::username.name, username)
        val update = Updates.set(UserModel::library.name, updatedLibrary)
        val result = userCollection.updateOne(filter, update)
        println("Matched docs ${result.matchedCount} and updated docs ${result.modifiedCount}")
    }

    suspend fun clearBookCollection() {
        val result = bookCollection.deleteMany(BsonDocument()) // Delete all documents from the BookCollection
        println("Deleted ${result.deletedCount} documents from BookCollection")
    }

    suspend fun clearUserCollection() {
        val result = userCollection.deleteMany(BsonDocument()) // Delete all documents from the UserCollection
        println("Deleted ${result.deletedCount} documents from UserCollection")
    }

}
