package org.example.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import org.example.model.BookModel
import org.example.model.UserModel

class MainPageViewModel (val userModel: UserModel){
    var isHamburgerOpen by mutableStateOf(false)
        private set

    fun onHamburgerClick() {
        isHamburgerOpen = true;
    }

    fun onDismissHamburger() {
        isHamburgerOpen = false;
    }

    fun getUserLibrary(): List<BookModel> {
        return userModel.library
    }
}