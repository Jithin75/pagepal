import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
/*
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.sun.tools.javac.Main

 */
//import kotlinx.coroutines.flow.firstOrNull
//import kotlinx.coroutines.runBlocking
import org.example.view.MainPageView
//import org.bson.BsonValue
import org.example.model.BookModel
import org.example.model.UserModel
import org.example.viewmodel.MainPageViewModel

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() = application {
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

    Window(
        title = "PagePal",
        state = WindowState(
            position = WindowPosition(Alignment.Center),
            size = DpSize(1024.dp, 576.dp)
        ),
        resizable = false,
        onCloseRequest = ::exitApplication
    ) {
        MainPageView(mainPageViewModel)
    }

    /*
    val database = getDatabase()
    val userCollection = database.getCollection<UserInfo>(collectionName = "UserCollection")
    val bookCollection = database.getCollection<BookInfo>(collectionName = "BookCollection")

    runBlocking {
        // Add a book to the book collection
        val bookId = addBook(bookCollection, "Percy Jackson Bk 1", "Rick Riordian")

        // Add a user referencing the book ID
        addUser(userCollection, "Bob", listOf(bookId), listOf())

        // Read the user
        readUser(userCollection, bookCollection)

        // Update user's book list with another book
        val newBookId = addBook(bookCollection, "One Punch Man", "ONE")
        updateUserBookList(userCollection, "Bob", newBookId)

        // Read the updated user
        readUser(userCollection, bookCollection)

        // Remove a book from a user's collection
        removeBookFromUser(userCollection, "Bob", newBookId)

        // Read the updated user
        readUser(userCollection, bookCollection)

        // Delete the user
        deleteUser(userCollection, "Bob")
    }

     */
}

/*
fun getDatabase() : MongoDatabase{
    val client = MongoClient.create(connectionString = "mongodb+srv://praviin10:Prav2003@cluster0.fqt7qpj.mongodb.net/?retryWrites=true&w=majority")
    return client.getDatabase(databaseName = "PagePalDB")
}

data class UserInfo(
    val userName: String,
    val bookList: List<BsonValue>,
    val friendList: List<String>
)

data class BookInfo(
    val title: String,
    val author: String
)

suspend fun addBook(collection: MongoCollection<BookInfo>, title: String, author: String): BsonValue {
    val book = BookInfo(title, author)
    var bookId: BsonValue
    collection.insertOne(book).also {
        println("Inserted Book - ${it.insertedId}")
        bookId = it.insertedId
    }
    return bookId
}

suspend fun addUser(collection: MongoCollection<UserInfo>, userName: String, bookList: List<BsonValue>, friendList: List<String>){
    val info = UserInfo(
        userName = userName,
        bookList = bookList,
        friendList = friendList
    )

    collection.insertOne(info).also {
        println("Inserted User - ${it.insertedId}")
    }
}

suspend fun readUser(userCollection: MongoCollection<UserInfo>, bookCollection: MongoCollection<BookInfo>) {
    userCollection.find<UserInfo>().collect { user ->
        println("${user.userName}'s Book List:")
        user.bookList.forEach { bookId ->
            val book = bookCollection.find(Filters.eq("_id", bookId)).firstOrNull()
            println("${book?.title} by ${book?.author}")
        }
    }
}

suspend fun updateUserBookList(collection: MongoCollection<UserInfo>, userName: String, newBookId: BsonValue) {
    val query = Filters.eq(UserInfo::userName.name, userName)
    val updateSet = Updates.addToSet(UserInfo::bookList.name, newBookId)

    collection.updateOne(filter = query, update = updateSet).also {
        println("Matched docs ${it.matchedCount} and updated docs ${it.modifiedCount}")
    }
}

suspend fun updateUserName(collection: MongoCollection<UserInfo>, curUserName: String, newUserName: String) {
    val query = Filters.eq(UserInfo::userName.name, curUserName)
    val updateSet = Updates.set(UserInfo::userName.name, newUserName)

    // can change to updateOne if only want to update first occurance
    collection.updateMany(filter = query, update = updateSet).also {
        println("Matched docs ${it.matchedCount} and updated docs ${it.modifiedCount}")
    }
}

suspend fun removeBookFromUser(collection: MongoCollection<UserInfo>, userName: String, bookIdToRemove: BsonValue) {
    val query = Filters.eq(UserInfo::userName.name, userName)
    val updateSet = Updates.pull(UserInfo::bookList.name, bookIdToRemove)

    collection.updateOne(filter = query, update = updateSet).also {
        println("Matched docs ${it.matchedCount} and updated docs ${it.modifiedCount}")
    }
}

suspend fun deleteUser(collection: MongoCollection<UserInfo>, userName: String) {
    val query = Filters.eq(UserInfo::userName.name, userName)
    collection.deleteMany(query).also {
        println("Deleted count ${it.deletedCount}")
    }
}


 */
