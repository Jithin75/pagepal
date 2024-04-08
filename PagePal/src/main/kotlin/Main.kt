
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.runBlocking
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider
import model.BookModel
import model.DatabaseManager
import model.UserModel
import view.MainPageView
import viewmodel.MainPageViewModel
import view.LoginView
import view.SignupView
import viewmodel.LoginViewModel
import viewmodel.SignupViewModel
import java.awt.Dimension

data class LoginViewState(val login: UserModel?, val view: String)

@Composable
fun App() {
    val pojoCodecRegistry: CodecRegistry = fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        fromProviders(PojoCodecProvider.builder().automatic(true).build())
    )

    val client = MongoClient.create(connectionString = "mongodb+srv://praviin10:Prav2003@cluster0.fqt7qpj.mongodb.net/?retryWrites=true&w=majority")
    val database = client.getDatabase("PagePalDB").withCodecRegistry(pojoCodecRegistry);
    val dbManager = DatabaseManager(database)

    val (currentState, setCurrentState) = remember { mutableStateOf(LoginViewState(null, "login")) }
    if (currentState.view == "login") {
        LoginView(loginViewModel = LoginViewModel(setCurrentState, dbManager))
    } else if (currentState.view == "signup") {
        SignupView(signupViewModel = SignupViewModel(setCurrentState, dbManager))
    } else if (currentState.view == "main") {
        // Get user using loginID to get mainPageViewModel
        if(currentState.login != null){
            val BookLibrary : MutableList<BookModel> = runBlocking { dbManager.getUserLibraryDB(currentState.login) }
            MainPageView(MainPageViewModel(currentState.login, BookLibrary, dbManager), setCurrentState)
        } else {
            println("Error: No user provided")
        }
    }
}

fun main() = application {

    Window(
        title = "PagePal",
        state = WindowState(
            position = WindowPosition(Alignment.Center),
        ),
        resizable = true,
        onCloseRequest = ::exitApplication
    ) {
        window.minimumSize = Dimension(1280, 720)
        App()
    }
}


