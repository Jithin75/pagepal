package viewmodel

import LoginViewState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.runBlocking
import org.example.model.DatabaseManager
import org.example.model.UserModel

class SignupViewModel (val setCurrentState: (LoginViewState) -> Unit, val dbManager: DatabaseManager){
    var username by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var password_confirm by mutableStateOf("")
        private set

    var showDialog by mutableStateOf(0)
        private set

    fun modifyShowDialog(value: Int) {
        showDialog = value
    }

    fun usernameEntered(entry: String) {
        username = entry
    }

    fun passwordEntered(entry: String) {
        password = entry
    }

    fun passwordConfirmEntered(entry: String) {
        password_confirm = entry
    }

    fun signupUser() {
        val userExist = runBlocking {
            dbManager.getUserByUsername(username)
        }
        if (userExist != null) {
            showDialog = 2
            username = ""
        } else {
            if (username.isNotEmpty() && password.isNotEmpty() && password_confirm.isNotEmpty()) {
                if (password.trim() != password_confirm.trim()) {
                    showDialog = 1
                    password = ""
                    password_confirm = ""
                } else {
                    runBlocking {
                        dbManager.addUser(
                            UserModel(
                                username.trim(),
                                password.trim(),
                                mutableListOf()
                            )
                        )
                    }
                    setCurrentState(LoginViewState(null, "login"))
                }
            }
        }
    }

    fun switchLogIn() {
        setCurrentState(LoginViewState(null, "login"))
    }
}