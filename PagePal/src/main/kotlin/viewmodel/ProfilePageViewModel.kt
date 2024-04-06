package viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.runBlocking
import org.example.model.DatabaseManager
import org.example.model.PasswordEncryption
import org.example.model.UserModel

class ProfilePageViewModel (val userModel: UserModel, val dbManager: DatabaseManager){
    var currentPassword by mutableStateOf("")
        private set
    var newPassword by mutableStateOf("")
        private set
    var verifyPassword by mutableStateOf("")
        private set
    var showPasswordDialog by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf("")
        private set
    var showConfirmationDialog by mutableStateOf(false)
        private set
    var newUsername by mutableStateOf("")
        private set
    var verifyUsername by mutableStateOf("")
        private set
    var showUsernameChangeDialog by mutableStateOf(false)
        private set

    fun toggleShowPasswordDialog(bool : Boolean) {
        showPasswordDialog = bool
    }
    fun toggleShowUsernameChangeDialog(bool : Boolean) {
        showUsernameChangeDialog = bool
    }
    fun toggleShowConfirmationDialog(bool : Boolean) {
        showConfirmationDialog = bool
    }
    fun isShowPasswordDialog() : Boolean {
        return showPasswordDialog
    }
    fun isShowUsernameChangeDialog() : Boolean {
        return showUsernameChangeDialog
    }
    fun isShowConfirmationDialog() : Boolean {
        return showConfirmationDialog
    }
    fun toggleCurrentPassword(str: String) {
        currentPassword = str
    }
    fun toggleNewPassword(str: String)  {
        newPassword = str
    }
    fun toggleVerifyPassword(str: String) {
        verifyPassword = str
    }
    fun toggleNewUsername(str: String) {
        newUsername = str
    }
    fun toggleVerifyUsername(str: String) {
        verifyUsername = str
    }
    fun toggleErrorMessage(str: String) {
        errorMessage = str
    }
    fun deleteAccount() {
        runBlocking {
            dbManager.deleteUser(userModel.username)
        }
    }

    fun updateUsername(){
        runBlocking {
            dbManager.updateUsername(userModel.username, newUsername)
        }
       userModel.username = newUsername
    }

    fun updatePassword() {
        runBlocking {
            dbManager.changePassword(userModel.username, currentPassword, newPassword)
        }
        userModel.password = PasswordEncryption.hashPassword(newPassword)
    }

}
