package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.BookModel
import view.BookItem
import viewmodel.MainPageViewModel
import theme.darkblue
import theme.grey
import theme.lightbrown
import viewmodel.BookViewModel
import viewmodel.RecommendationViewModel

@Composable
fun RecommendationView(mainPageViewModel: MainPageViewModel, recommendationViewModel: RecommendationViewModel) {

    LaunchedEffect(Unit) {
        recommendationViewModel.initiateDisplayedBooks(mainPageViewModel)
    }

    MaterialTheme {
        // Show loading screen while data is being fetched
        if (recommendationViewModel.isLoading) {
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
                        // Title
                        Text(
                            text = AnnotatedString("AI Recommendations", spanStyle = SpanStyle(textDecoration = TextDecoration.Underline)),
                            color = lightbrown,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 30.sp // Set the font size to 30sp
                        )

                        Spacer(modifier = Modifier.height(8.dp)) // Add space below the text

                        // Book Grid
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(5),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {

                            // Update displayedBooks within the composable context
                            val library = recommendationViewModel.displayedBooks.toList()
                            items(items = library, key = { it.title + it.cover }) { book ->
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
                    mainPageViewModel = mainPageViewModel,
                    bookViewModel = BookViewModel(bookModel, mainPageViewModel.dbManager),
                    recommendationViewModel = recommendationViewModel,
                    isRecommended = true
                )
            }
        }
    }
}