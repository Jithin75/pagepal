package org.example.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import org.example.model.BookModel
import org.example.viewmodel.MainPageViewModel

@Composable
fun MainPageView(mainPageViewModel: MainPageViewModel) {
    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "PagePal",
                            color = Color(0xFFFFFFFF),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 56.dp),
                            textAlign = TextAlign.Center
                        )
                            },
                    backgroundColor = Color(0xFF404040),
                    navigationIcon = {
                        IconButton(onClick = { /* Handle hamburger button click */ }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color(0xFF293040))
                        .padding(10.dp)
                ) {
                    // Search Bar and Filters
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(96.dp)
                            .background(color = Color(0xFF293040))
                            .padding(10.dp)
                    ) {
                        // Replace these with actual search and filter components

                        Text("Search Bar", Modifier.align(Alignment.CenterStart))
                        Text("Genre, Status, Sort", Modifier.align(Alignment.Center))
                        Button(
                            onClick = { /* Handle add button click */ },
                            Modifier.align(Alignment.CenterEnd),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF5FD855))
                        ) {
                            Text("Add")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Book Grid
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(5),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(mainPageViewModel.getUserLibrary().size) { index ->
                            BookItem(
                                mainPageViewModel.getUserLibrary()[index],
                                onClick = {/* Add click functionality */}
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun DropDownMenu(title: String, options: List<String>){
    var expanded by remember { mutableStateOf(false) }
}

@Composable
fun BookItem(book: BookModel, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        val painter = painterResource(book.cover)

        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // cover
            Image(
                painter = painter,
                contentDescription = book.title
            )

            // title
            Text(
                text = book.title,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFFFFFFFF)
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )

            // author
            Text(
                text = book.author,
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = Color(0xFFFFFFFF)
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

    }
}