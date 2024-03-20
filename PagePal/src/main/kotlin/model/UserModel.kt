package org.example.model

import org.bson.BsonValue

class UserModel(
    var username: String = "",
    var password: String = "",
    var library: MutableList<BsonValue> = mutableListOf(),
    var name: String = "Name not Provided",
    var email: String = "Email not Provided",
    var dateOfBirth: String = "Date of Birth not Provided") {
    fun addBook(bookId: BsonValue) {
        library.add(bookId)
    }
}