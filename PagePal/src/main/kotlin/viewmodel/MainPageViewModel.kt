package org.example.viewmodel

import org.example.model.BookModel
import org.example.model.UserModel

class MainPageViewModel (val userModel: UserModel){
    fun getUserLibrary(): List<BookModel> {
        return userModel.library
    }
}