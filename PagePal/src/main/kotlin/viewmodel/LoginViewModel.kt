package viewmodel

import LoginViewState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.runBlocking
import model.DatabaseManager

class LoginViewModel (val setCurrentState: (LoginViewState) -> Unit, val dbManager: DatabaseManager) {
    var username by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var showDialog by mutableStateOf(false)
        private set

    fun toggleShowDialog() {
        showDialog = !showDialog
    }

    fun usernameEntered(entry: String) {
        username = entry
    }

    fun passwordEntered(entry: String) {
        password = entry
    }

    fun loginUser() {
        if (username.isNotEmpty() && password.isNotEmpty()) {
            val isValid = runBlocking {
                dbManager.isValidCredentials(username, password)
            }
            if (isValid) {
                val User = runBlocking {
                    dbManager.getUserByUsername(username)
                }
                setCurrentState(LoginViewState(User, "main"))
            } else {
                showDialog = true // Show dialog for incorrect credentials
                username = ""
                password = ""
            }
        } else {
            showDialog = true // Show dialog for incorrect credentials
            username = ""
            password = ""
        }
    }

    fun switchSignUp() {
        setCurrentState(LoginViewState(null, "signup"))
    }
}