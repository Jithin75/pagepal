package org.example.model

class UserModel(
    var username: String = "",
    var password: String = "",
    var library: MutableList<String> = mutableListOf(),
    var coverQuality: String = "1") {
    fun addBook(bookId: String) {
        library.add(bookId)
    }
}