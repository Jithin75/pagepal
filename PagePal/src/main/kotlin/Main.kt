import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import org.example.model.BookModel
import org.example.model.UserModel
import org.example.view.MainPageView
import org.example.viewmodel.MainPageViewModel
import view.LoginView
import view.SignupView
import java.awt.Dimension

@Composable
fun App(currentView: MutableState<String>) {
    // Temporary, will be removed once the login authorization is fully functional
    val library = mutableListOf(
        BookModel("Les Miserables", "Victor Hugo", "lesMiserables.jpg"),
        BookModel("La Bete humaine", "Guy de Maupassant", "laBeteHumaine.jpg"),
        BookModel("Voyage au Centre de la Terre", "Jules Verne"),
        BookModel("I, Robot", "Isaac Asimov", "IRobot.jpg"),
        BookModel("Murder on the Orient Express", "Agatha Christie", "murderOrientExpress.jpg"),
        BookModel("Le Rhin", "Victor Hugo"),
        BookModel("La Metamorphose", "Franz Kafka", "laMetamorphose.jpg"),
        BookModel("L'Etranger", "Albert Camus", "lEtranger.jpg"),
        BookModel("L'Avare", "Moliere"),
        BookModel("The Recruit", "Robert Muchamore", "theRecruit.jpg")
    )
    val user = UserModel("Achille59", "complicatedPassw0rd", library)
    val mainPageViewModel = MainPageViewModel(user)

    val (view, setView) = remember { mutableStateOf("login") }
    val (loginID, setLogin) = remember { mutableIntStateOf(0) }
    if (view == "login") {
        LoginView(setView, setLogin)
    } else if (view == "signup"){
        SignupView(setView)
    } else if (view == "main") {
        // Get user using loginID to get mainPageViewModel
        MainPageView(mainPageViewModel)
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
