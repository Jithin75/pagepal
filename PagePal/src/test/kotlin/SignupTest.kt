
import androidx.compose.runtime.mutableStateOf
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.runBlocking
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider
import model.DatabaseManager
import model.UserModel
import org.junit.Assert.*
import org.junit.Test
import viewmodel.SignupViewModel
import java.lang.Thread.sleep

class SignupTest {

    val pojoCodecRegistry: CodecRegistry = CodecRegistries.fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
    )

    val client = MongoClient.create(connectionString = "mongodb+srv://praviin10:Prav2003@cluster0.fqt7qpj.mongodb.net/?retryWrites=true&w=majority")
    val database = client.getDatabase("PagePalDB").withCodecRegistry(pojoCodecRegistry)
    val dbManager = DatabaseManager(database)

    @Test
    fun testSignupUser_UserExists() {

        // Create ViewModel instance
        val (signupState, setSignupState) = mutableStateOf(LoginViewState(null, "signup"))
        val signupView = SignupViewModel(setSignupState, dbManager)

        runBlocking {
            // Create testuser
            val user = UserModel(username = "testuser", password = "testpassword")
            dbManager.addUser(user)

            // Set username and password
            signupView.usernameEntered("testuser")
            signupView.passwordEntered("testpassword")
            signupView.passwordConfirmEntered("testpassword")

            // Call loginUser function
            signupView.signupUser()

            // Assert that showDialog is 2 (Existing User)
            assertEquals(2, signupView.showDialog)
            assertEquals("", signupView.username)

            // Deleting testuser
            dbManager.deleteUser(user.username)
        }
    }

    @Test
    fun testSignupUser_PasswordMismatch() {
        // Create ViewModel instance
        val (signupState, setSignupState) = mutableStateOf(LoginViewState(null, "signup"))
        val signupView = SignupViewModel(setSignupState, dbManager)

        // Set username and password
        signupView.usernameEntered("testuser")
        signupView.passwordEntered("testpassword1")
        signupView.passwordConfirmEntered("testpassword2")

        // Call loginUser function
        signupView.signupUser()

        // Assert that showDialog is 1 (Password Mismatch)
        assertEquals(1, signupView.showDialog)
        assertEquals("", signupView.password)
        assertEquals("", signupView.password_confirm)

    }

    @Test
    fun testSignupUser_Success() {
        // Create SignupViewModel instance
        // Initialize signupState to SignupViewState(null, "signup")
        var signupState: LoginViewState? = LoginViewState(null, "signup")
        // Create a lambda function to set the signup state
        val setSignupState: (LoginViewState) -> Unit = { state ->
            signupState = state
        }
        val signupView = SignupViewModel(setSignupState, dbManager)

        // Set username and password
        signupView.usernameEntered("testuser")
        signupView.passwordEntered("testpassword")
        signupView.passwordConfirmEntered("testpassword")

        // Call loginUser function
        signupView.signupUser()

        // Allow the changes to take place
        sleep(1000)

        // Assert that redirection occurs
        assertNull(signupState?.login)
        assertEquals("login", signupState?.view)

        runBlocking{
            // Assert the user has been added
            assertNotNull(dbManager.getUserByUsername("testuser"))

            // Delete User
            dbManager.deleteUser("testuser")
        }

    }

    @Test
    fun testSwitchLogIn() {
        // Create SignupViewModel instance
        // Initialize singupState to SignupViewState(null, "signup")
        var signupState: LoginViewState? = LoginViewState(null, "signup")
        // Create a lambda function to set the signup state
        val setSignupState: (LoginViewState) -> Unit = { state ->
            signupState = state
        }
        val signupView = SignupViewModel(setSignupState, dbManager)

        signupView.switchLogIn()

        assertNull(signupState?.login)
        assertEquals("login", signupState?.view)
    }

    @Test
    fun testSignupUser_dialogToggle() {

        // Create loginView instance
        val (signupState, setSignupState) = mutableStateOf(LoginViewState(null, "signup"))
        val signupView = SignupViewModel(setSignupState, dbManager)

        // Switch Dialog box to 1
        signupView.modifyShowDialog(1)

        // Assert Dialog Box is 1
        assertEquals(1, signupView.showDialog)

        // Switch Dialog box to 2
        signupView.modifyShowDialog(2)

        // Assert Dialog Box is 2
        assertEquals(2, signupView.showDialog)
    }
}