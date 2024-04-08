import androidx.compose.runtime.mutableStateOf
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.runBlocking
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider
import org.example.model.DatabaseManager
import org.example.model.UserModel
import org.junit.Assert.*
import org.junit.Test
import viewmodel.LoginViewModel

class LoginTest {

    val pojoCodecRegistry: CodecRegistry = CodecRegistries.fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
    )

    val client = MongoClient.create(connectionString = "mongodb+srv://praviin10:Prav2003@cluster0.fqt7qpj.mongodb.net/?retryWrites=true&w=majority")
    val database = client.getDatabase("PagePalDB").withCodecRegistry(pojoCodecRegistry)
    val dbManager = DatabaseManager(database)

    @Test
    fun testLoginUser_ValidCredentials() {

        // Create ViewModel instance
        val (loginState, setLoginState) = mutableStateOf(LoginViewState(null, "login"))
        val loginView = LoginViewModel(setLoginState, dbManager)

        runBlocking {
            // Create testuser
            val user = UserModel(username = "testuser", password = "testpassword")
            dbManager.addUser(user)

            // Set username and password
            loginView.usernameEntered("testuser")
            loginView.passwordEntered("testpassword")

            // Call loginUser function
            loginView.loginUser()

            // Assert that showDialog is false
            assertFalse(loginView.showDialog)

            // Deleting testuser
                dbManager.deleteUser(user.username)
        }
    }

    @Test
    fun testLoginUser_InvalidCredentials() {

        // Create loginView instance
        val (loginState, setLoginState) = mutableStateOf(LoginViewState(null, "login"))
        val loginView = LoginViewModel(setLoginState, dbManager)

        // Set username and password
        loginView.usernameEntered("****")
        loginView.passwordEntered("****")

        // Call loginUser function
        loginView.loginUser()

        // Assert that showDialog is true
        assertTrue(loginView.showDialog)

        // Assert that username and password are cleared
        assertEquals("", loginView.username)
        assertEquals("", loginView.password)

    }

    @Test
    fun testLoginUser_EmptyCredentials() {

        // Create loginView instance
        val (loginState, setLoginState) = mutableStateOf(LoginViewState(null, "login"))
        val loginView = LoginViewModel(setLoginState, dbManager)

        // Set username and password
        loginView.usernameEntered("")
        loginView.passwordEntered("")

        // Call loginUser function
        loginView.loginUser()

        // Assert that showDialog is true
        assertTrue(loginView.showDialog)

        // Assert that username and password are cleared
        assertEquals("", loginView.username)
        assertEquals("", loginView.password)

    }

    @Test
    fun testLoginUser_switchSignup() {

        // Create loginView instance
        // Initialize currentState to LoginViewState(null, "login")
        var loginState: LoginViewState? = LoginViewState(null, "login")
        // Create a lambda function to set the current state
        val setLoginState: (LoginViewState) -> Unit = { state ->
            loginState = state
        }
        val loginView = LoginViewModel(setLoginState, dbManager)

        // Switching to Signup Page
        loginView.switchSignUp()

        // Assert that loginState user is null and view is "signup"
        assertNull(loginState?.login)
        assertEquals("signup", loginState?.view)

    }

    @Test
    fun testLoginUser_dialogToggle() {

        // Create loginView instance
        val (loginState, setLoginState) = mutableStateOf(LoginViewState(null, "login"))
        val loginView = LoginViewModel(setLoginState, dbManager)

        // Switch Dialog box
        loginView.toggleShowDialog()

        // Assert Dialog Box is Open
        assertTrue(loginView.showDialog)
    }
}
