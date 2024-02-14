package org.example

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.runBlocking

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val database = getDatabase()
    val collection = database.getCollection<UserInfo>(collectionName = "PagePalCollection")

    runBlocking {
        /*addUser(collection, "Smith",
            listOf("Naruto", "One Punch Man", "Spider-man"),
            listOf("Bob")
        )
        readUser(collection)*/
        updateUserName(collection, "Ross", "Bill")
        deleteUser(collection, "Smith")
    }
}

fun getDatabase() : MongoDatabase{
    val client = MongoClient.create(connectionString = "mongodb+srv://praviin10:Prav2003@cluster0.fqt7qpj.mongodb.net/?retryWrites=true&w=majority")
    return client.getDatabase(databaseName = "PagePalDB")
}

data class UserInfo(
    val userName: String,
    val bookList: List<String>,
    val friendList: List<String>
)
suspend fun addUser(collection: MongoCollection<UserInfo>, userName: String, bookList: List<String>, friendList: List<String>){
    val info = UserInfo(
        userName = userName,
        bookList = bookList,
        friendList = friendList
    )

    collection.insertOne(info).also {
        println("Inserted User - ${it.insertedId}")
    }
}

suspend fun readUser(collection: MongoCollection<UserInfo>) {
    val query = Filters.or(
        listOf(
            Filters.eq("userName", "Bob"),
            Filters.eq(UserInfo::userName.name, "Jack")

        )
    )

    collection.find<UserInfo>(filter = query).limit(2).collect {
        println(it)
    }
}

suspend fun updateUserName(collection: MongoCollection<UserInfo>, curUserName: String, newUserName: String) {
    val query = Filters.eq(UserInfo::userName.name, curUserName)
    val updateSet = Updates.set(UserInfo::userName.name, newUserName)

    // can change to updateOne if only want to update first occurance
    collection.updateMany(filter = query, update = updateSet).also {
        println("Matched docs ${it.matchedCount} and updated docs ${it.modifiedCount}")
    }
}

suspend fun deleteUser(collection: MongoCollection<UserInfo>, userName: String) {
    val query = Filters.eq(UserInfo::userName.name, userName)
    collection.deleteMany(query).also {
        println("Deleted count ${it.deletedCount}")
    }
}

