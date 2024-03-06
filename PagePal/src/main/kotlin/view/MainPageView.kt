package org.example.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import org.example.model.BookModel
import org.example.viewmodel.MainPageViewModel
import theme.*
import view.HamburgerMenuView

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
                                .padding(end = 68.dp),
                            textAlign = TextAlign.Center
                        )
                            },
                    backgroundColor = grey,
                    navigationIcon = {
                        IconButton(onClick = {mainPageViewModel.onHamburgerClick()}) {
                            Icon(Icons.Filled.Menu,
                                contentDescription = "Menu",
                                tint = lightbrown
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
                    // Search Bar, Filters and Add Button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .background(color = darkblue)
                            .align(Alignment.CenterHorizontally)
                            .padding(horizontal = 8.dp)
                    ) {
                        // Replace these with actual search and filter components
                        SearchBar(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .align(Alignment.CenterVertically)
                        )
                        DropDown(
                            listOf("Genre", "Horror", "Fantasy", "SciFi"),
                            Modifier
                                .padding(horizontal = 8.dp)
                                .align(Alignment.CenterVertically)
                        )
                        DropDown(
                            listOf("Status", "Read", "In Progress", "New"),
                            Modifier
                                .padding(horizontal = 8.dp)
                                .align(Alignment.CenterVertically)
                        )
                        DropDown(
                            listOf("Sort", "Alphabetical", "Most Recent"),
                            Modifier
                                .padding(horizontal = 8.dp)
                                .align(Alignment.CenterVertically)
                        )
                        Button(
                            onClick = { /* Handle add button click */ },
                            Modifier
                                .padding(horizontal = 8.dp)
                                .align(Alignment.CenterVertically)
                                .size(64.dp,32.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = green)
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
                if (mainPageViewModel.isHamburgerOpen) {
                    HamburgerMenuView(mainPageViewModel)
                }
            }
        )
    }
}

@Composable
fun SearchBar(modifier: Modifier){
    var searchContent by remember {mutableStateOf("")}
    TextField(
        value = searchContent,
        onValueChange = {searchContent = it},
        leadingIcon = { Icon(
            Icons.Filled.Search,
            contentDescription = "searchIcon",
        )},
        placeholder = {Text("Search")},
        modifier = modifier
            .width(256.dp)
            .height(50.dp),
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = whitevariation,
            leadingIconColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            placeholderColor = lightgrey,
            cursorColor = Color.Black,
        )
    )
}

@Composable
fun DropDown(options: List<String>, modifier: Modifier){
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0])}

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .size(187.dp, 50.dp)
            .clip(RoundedCornerShape(1.dp))
            .border(BorderStroke(1.dp, lightgrey), RoundedCornerShape(4.dp))
            .clickable { expanded = !expanded },
    ) {
        Text(
            text = selectedOptionText,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 10.dp),
            color = lightgrey
        )
        Icon(
            Icons.Filled.ArrowDropDown,
            "contentDescription",
            Modifier.align(Alignment.CenterEnd),
            tint = lightgrey
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                    }
                ) {
                    Text(text = selectionOption)
                }
            }
        }
    }
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
                    color = Color.White
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