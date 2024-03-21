package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.runBlocking
import model.AIRecommender
import model.api.BookApiClient
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider
import org.example.model.BookModel
import org.example.model.DatabaseManager
import org.example.viewmodel.MainPageViewModel
import org.json.JSONArray
import org.json.JSONObject
import theme.darkblue
import theme.grey

@Composable
fun RecommendationView(mainPageViewModel: MainPageViewModel) {
    var dbManager: DatabaseManager? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        dbManager = runBlocking {
            val pojoCodecRegistry: CodecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
            )
            val client =
                MongoClient.create(connectionString = "mongodb+srv://praviin10:Prav2003@cluster0.fqt7qpj.mongodb.net/?retryWrites=true&w=majority")
            val database = client.getDatabase("PagePalDB").withCodecRegistry(pojoCodecRegistry)
            DatabaseManager(database)
        }
    }

    MaterialTheme {
        Scaffold (
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "PagePal",
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 68.dp),
                            textAlign = TextAlign.Center
                        )
                    },
                    backgroundColor = grey,
                    navigationIcon = {
                        IconButton(onClick = {mainPageViewModel.toggleRecommendPage()}) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    }
                )
            }, content = {
                Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = darkblue)
                    .padding(10.dp)
            ) {
                // Book Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    var userBooks = mainPageViewModel.getUserLibrary()

                    val question = StringBuilder()
                    question.append("The user has read the following books: \\n")

                    userBooks.value.forEachIndexed { index, book ->
                        // Append the title and authors to the question string
                        question.append("${book.title}: ${book.author}")

                        // Append newline character except for the last book
                        if (index < userBooks.value.size - 1) {
                            question.append("\\n")
                        }
                    }

//                    var bookResponseString = ""
//                    var displayedBooks by remember { mutableStateOf(mutableListOf<BookModel>()) }
                    var displayedBooks = mutableListOf<BookModel>()
                    // Pass the constructed question string to the getResponse function
                    val response = AIRecommender.getResponse(question.toString(), 10)
                    val jsonObject = JSONObject(response)
                    val jsonArray: JSONArray = jsonObject.getJSONArray("recommendations")
                    val bookTitles = mutableListOf<String>()
                    for (i in 0 until jsonArray.length()) {
                        val bookObject = jsonArray.getJSONObject(i)
                        val title = bookObject.getString("title")
                        bookTitles.add(title)
                    }

                    val bookClient = BookApiClient()

                    for (title in bookTitles) {
                        displayedBooks.add(bookClient.searchBook(title))
                    }

                    // Update displayedBooks within the composable context
                    val library = displayedBooks
//                    items(library.size) { index ->
//                        BookItem(
//                            library[index],
//                            onClick = { mainPageViewModel.onBookClick(library[index]) }
//                        )
//                    }
                }
            }
            }
        )
    }
}

