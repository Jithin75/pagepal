package org.example.model

class UserModel (val username: String,
                 val password: String,
                 val library: MutableList<BookModel> = mutableListOf())
{
    fun addBook(book: BookModel) {
        library.add(book);
    }
}