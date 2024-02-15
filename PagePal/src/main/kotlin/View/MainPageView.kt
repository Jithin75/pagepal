package org.example.View

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

@Composable
fun MainPageView() {
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
                                .padding(end = 48.dp),
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
                        items(20) { index ->
                            BookItem(title = "Book $index", modifier = Modifier.padding(8.dp))
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
fun BookItem(title: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(120.dp)
            .background(color = MaterialTheme.colors.secondary, shape = MaterialTheme.shapes.medium)
            .clickable { /* Handle book item click */ }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(color = MaterialTheme.colors.surface, shape = MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                Text(title)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(title)
        }
    }
}