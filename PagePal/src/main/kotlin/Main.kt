
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
import org.bson.BsonValue
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
    /*var library: MutableList<BookModel> = mutableListOf()
    runBlocking {
        library.add(BookModel("La Bete humaine", "Guy de Maupassant", "laBeteHumaine.jpg"))
        library.add(BookModel("Voyage au Centre de la Terre", "Jules Verne"))
        library.add(BookModel("I, Robot", "Isaac Asimov", "IRobot.jpg"))
        library.add(BookModel("Murder on the Orient Express", "Agatha Christie", "murderOrientExpress.jpg"))
        library.add(BookModel("Le Rhin", "Victor Hugo"))
        library.add(BookModel("La Metamorphose", "Franz Kafka", "laMetamorphose.jpg"))
        library.add(BookModel("L'Etranger", "Albert Camus", "lEtranger.jpg"))
        library.add(BookModel("L'Avare", "Moliere"))
        library.add(BookModel("The Recruit", "Robert Muchamore", "theRecruit.jpg"))
    }
    val user = UserModel("Achille59", "complicatedPassw0rd", library)
    */
    var library: MutableList<BsonValue> = mutableListOf()
    var bookLibrary : MutableList<BookModel> = mutableListOf()
    val pojoCodecRegistry: CodecRegistry = fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        fromProviders(PojoCodecProvider.builder().automatic(true).build())
    )

    val client = MongoClient.create(connectionString = "mongodb+srv://praviin10:Prav2003@cluster0.fqt7qpj.mongodb.net/?retryWrites=true&w=majority")
    val database = client.getDatabase("PagePalDB").withCodecRegistry(pojoCodecRegistry);
    val dbManager = DatabaseManager(database)
    runBlocking {
        // Temporary, will be removed once the login authorization is fully functional
        val book = BookModel("Les Miserables", "Victor Hugo", "lesMiserables.jpg")
        val bookid = dbManager.addBook(book)
        library.add(bookid)
        bookLibrary.add(book)
    }
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
            MainPageView(mainPageViewModel)
        } else {
            val newBookLibrary : MutableList<BookModel> = mutableListOf()
            MainPageView(MainPageViewModel(currentState.login, newBookLibrary, dbManager))
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

    val pojoCodecRegistry: CodecRegistry = fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        fromProviders(PojoCodecProvider.builder().automatic(true).build())
    )

    val client = MongoClient.create(connectionString = "mongodb+srv://praviin10:Prav2003@cluster0.fqt7qpj.mongodb.net/?retryWrites=true&w=majority")
    val database = client.getDatabase("PagePalDB").withCodecRegistry(pojoCodecRegistry);
    val dbManager = DatabaseManager(database)

    runBlocking {
//        dbManager.clearBookCollection()
//        dbManager.clearUserCollection()

        val book = BookModel("Percy Jackson Bk 1", "Rick Riordian")
        val bookId = dbManager.addBook(book)
        println("Book added: $bookId")

        // Add a user
        val userBookList = mutableListOf(bookId)
        dbManager.addUser(UserModel("Bob", "password", userBookList))
        println("User added: Bob")

        // Read the user
        println("User's book list before update:")
        dbManager.readUser()

        // Add a new book
        val newBook = BookModel("One Punch Man", "ONE")
        val newBookId = dbManager.addBook(newBook)
        println("New book added: $newBookId")

        // Update user's book list
        dbManager.updateUserBookList("Bob", newBookId)
        println("User's book list after update:")
        dbManager.readUser()

        // Remove a book from user's collection
        dbManager.removeBookFromUser("Bob", newBookId)
        println("User's book list after removing $newBook:")
        dbManager.readUser()

        // Delete the user
        dbManager.deleteUser("Bob")
        println("User deleted: Bob")

        // Test username update
        dbManager.addUser(UserModel("Alice", "password", mutableListOf()))
        println("User added: Alice")
        dbManager.updateUsername("Alice", "NewAlice")
        println("User's username updated to NewAlice")

        // Test credentials validation
        var exists = dbManager.getUserByUsername("NewAlice")
        if (exists != null) {
            println("NewAlice exists")
        } else {
            println("NewAlice does not exist")
        }
        exists = dbManager.getUserByUsername("Alice")
        if (exists != null) {
            println("Alice exists")
        } else {
            println("Alice does not exist")
        }
        var isValid = dbManager.isValidCredentials("NewAlice", "password")
        println("Is NewAlice's credentials valid? $isValid")
        isValid = dbManager.isValidCredentials("NewAlice", "wrongPassword")
        println("Is NewAlice's credentials valid? $isValid")
    }
}


