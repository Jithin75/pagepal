package org.example.model

import org.bson.BsonValue

class UserModel(
    var username: String = "",
    var password: String = "",
    var library: MutableList<BsonValue> = mutableListOf()) {
    fun addBook(bookId: BsonValue) {
        library.add(bookId)
    }
}