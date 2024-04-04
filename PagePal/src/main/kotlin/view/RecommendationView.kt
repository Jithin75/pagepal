package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import org.example.view.BookItem
import org.example.viewmodel.MainPageViewModel
import org.json.JSONArray
import org.json.JSONObject
import theme.darkblue
import theme.grey
import theme.lightbrown
import viewmodel.BookViewModel
import viewmodel.RecommendationViewModel

@Composable
fun RecommendationView(mainPageViewModel: MainPageViewModel, recommendationViewModel: RecommendationViewModel) {
    var dbManager: DatabaseManager? by remember { mutableStateOf(null) }
    var isLoading by remember { mutableStateOf(true) } // State for loading
    var displayedBooks by remember { mutableStateOf<List<BookModel>>(emptyList()) }

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
        try {
            var userBooks = mainPageViewModel.getUserLibrary()

            val question = StringBuilder()
            question.append("The user has read the following books: \\n")

            userBooks.forEachIndexed { index, book ->
                // Append the title and authors to the question string
                val strippedTitle = book.title.replace("\"", "\\\"")
                val strippedAuthor = book.author.replace("\"", "\\\"")
                question.append("$strippedTitle: $strippedAuthor")

                // Append newline character except for the last book
                if (index < userBooks.size - 1) {
                    question.append("\\n")
                }
            }

            displayedBooks = mutableListOf<BookModel>()
            // Pass the constructed question string to the getResponse function
            val response = AIRecommender.getResponse(question.toString(), 10)
            if (response == "PARSE ERROR") {
                mainPageViewModel.onDismissRecommend()
            }
            val jsonObject = JSONObject(response)
            val jsonArray: JSONArray = jsonObject.getJSONArray("recommendations")
            val bookTitles = mutableListOf<String>()
            for (i in 0 until jsonArray.length()) {
                val bookObject = jsonArray.getJSONObject(i)
                val title = bookObject.getString("title")
                bookTitles.add(title)
            }

            val bookClient = BookApiClient()
            val books = mutableListOf<BookModel>()

            for (title in bookTitles) {
                val book : BookModel = bookClient.searchBook(title)
                val pattern = Regex("&zoom=\\d+")
                val replacement = "&zoom=3"
                val cover = book.cover
                book.cover = cover.replace(pattern, replacement)
                books.add(book)
            }

            displayedBooks = books // Update displayedBooks
        } catch (e: Exception) {
            println(e)
        } finally {
            isLoading = false // Hide loading screen when tasks complete
        }
    }

    MaterialTheme {
        // Show loading screen while data is being fetched
        if (isLoading) {
            Scaffold(
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
                            IconButton(onClick = { mainPageViewModel.onDismissRecommend() }) {
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
                            .background(color = darkblue),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "LOADING...",
                            color = lightbrown,
                            fontSize = 30.sp
                        )
                    }
                }
            )
        } else {
            // Actual content of the screen
            Scaffold(
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
                            IconButton(onClick = { mainPageViewModel.onDismissRecommend() }) {
                                Icon(
                                    Icons.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White
                                )
                            }
                        }
                    )
                },
                content = {
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

                            // Update displayedBooks within the composable context
                            val library = displayedBooks.toList()
                            items(items = library, key = { it.cover }) { book ->
                                BookItem(
                                    book,
                                    onClick = { recommendationViewModel.onBookClick(book) }
                                )
                            }
                        }
                    }
                }
            )
            if (recommendationViewModel.isBookOpen) {
                val bookModel = recommendationViewModel.bookOpened ?: BookModel(title = "error")
                BookView(
                    mainPageViewModel,
                    BookViewModel(bookModel, mainPageViewModel.dbManager),
                    isRecommended = true,
                    onDismiss = { recommendationViewModel.onDismissBook() })
            }
        }
    }
}