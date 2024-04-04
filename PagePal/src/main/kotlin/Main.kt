
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
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
import org.example.model.BookModel
import org.example.model.DatabaseManager
import org.example.model.UserModel
import org.example.view.MainPageView
import org.example.viewmodel.MainPageViewModel
import view.LoginView
import view.SignupView
import java.awt.Dimension

data class LoginViewState(val login: UserModel?, val view: String)

@Composable
fun App(currentView: MutableState<String>) {
    var library: MutableList<String> = mutableListOf()
    var bookLibrary : MutableList<BookModel> = mutableListOf()
    val pojoCodecRegistry: CodecRegistry = fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        fromProviders(PojoCodecProvider.builder().automatic(true).build())
    )

    val client = MongoClient.create(connectionString = "mongodb+srv://praviin10:Prav2003@cluster0.fqt7qpj.mongodb.net/?retryWrites=true&w=majority")
    val database = client.getDatabase("PagePalDB").withCodecRegistry(pojoCodecRegistry);
    val dbManager = DatabaseManager(database)

    val user = UserModel("Achille59", "complicatedPassw0rd", library )

    val mainPageViewModel = remember { MainPageViewModel(user, bookLibrary, dbManager) }
    val (currentState, setCurrentState) = remember { mutableStateOf(LoginViewState(null, "login")) }
    if (currentState.view == "login") {
        LoginView(setCurrentState)
    } else if (currentState.view == "signup") {
        SignupView(setCurrentState)
    } else if (currentState.view == "main") {
        // Get user using loginID to get mainPageViewModel
        //MainPageView(mainPageViewModel)
        if(currentState.login == null) {
            MainPageView(mainPageViewModel, setCurrentState)
        } else {
            val newBookLibrary : MutableList<BookModel> = runBlocking { dbManager.getUserLibraryDB(currentState.login) }
            MainPageView(MainPageViewModel(currentState.login, newBookLibrary, dbManager), setCurrentState)
        }
    }
}

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() = application {

    Window(
        title = "PagePal",
        state = WindowState(
            position = WindowPosition(Alignment.Center),
            size = DpSize(1024.dp, 576.dp)
        ),
        resizable = true,
        onCloseRequest = ::exitApplication
    ) {
        window.minimumSize = Dimension(1280, 720)
        App(mutableStateOf("login"))
    }
}


