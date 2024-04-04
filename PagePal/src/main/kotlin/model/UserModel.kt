package org.example.model

class UserModel(
    var username: String = "",
    var password: String = "",
    var library: MutableList<String> = mutableListOf()) {
    fun addBook(bookId: String) {
        library.add(bookId)
    }
}