package org.example.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.ui.window.Dialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import org.example.model.BookModel
import org.example.viewmodel.MainPageViewModel
import theme.*
import view.BookView
import view.HamburgerMenuView
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import model.api.*

@Composable
fun MainPageView(mainPageViewModel: MainPageViewModel) {
    val sortOptions =  listOf("Sort", "Title", "Author", "Recently Added")
    val statusOptions = listOf("Status", "Complete", "Ongoing", "Dropped")

    var sortExpanded by remember { mutableStateOf(false) }
    var sortSelectedOptionText by remember { mutableStateOf(sortOptions[0])}

    var statusExpanded by remember { mutableStateOf(false) }
    var statusSelectedOptionText by remember { mutableStateOf(statusOptions[0])}

    MaterialTheme {
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
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        SearchBar(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .align(Alignment.CenterVertically),
                            mainPageViewModel
                        )
                        DropDown(
                            listOf("Genre", "Horror", "Fantasy", "SciFi"),
                            Modifier
                                .padding(horizontal = 8.dp)
                                .align(Alignment.CenterVertically)
                        ) {}
                        Box(
                            contentAlignment = Alignment.CenterStart,
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .align(Alignment.CenterVertically)
                                .size(192.dp, 50.dp)
                                .clip(RoundedCornerShape(1.dp))
                                .border(BorderStroke(1.dp, lightgrey), RoundedCornerShape(4.dp))
                                .clickable { statusExpanded = !statusExpanded },
                        ) {
                            Text(
                                text = statusSelectedOptionText,
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
                                expanded = statusExpanded,
                                onDismissRequest = { statusExpanded = false }
                            ) {
                                statusOptions.forEach { selectionOption ->
                                    DropdownMenuItem(
                                        onClick = {
                                            statusSelectedOptionText = selectionOption
                                            mainPageViewModel.statusFilter(selectionOption)
                                            statusExpanded = false
                                        }
                                    ) {
                                        Text(text = selectionOption)
                                    }
                                }
                            }
                        }
                        Box(
                            contentAlignment = Alignment.CenterStart,
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .align(Alignment.CenterVertically)
                                .size(192.dp, 50.dp)
                                .clip(RoundedCornerShape(1.dp))
                                .border(BorderStroke(1.dp, lightgrey), RoundedCornerShape(4.dp))
                                .clickable { sortExpanded = !sortExpanded },
                        ) {
                            Text(
                                text = sortSelectedOptionText,
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
                                expanded = sortExpanded,
                                onDismissRequest = { sortExpanded = false }
                            ) {
                                sortOptions.forEach { selectionOption ->
                                    DropdownMenuItem(
                                        onClick = {
                                            sortSelectedOptionText = selectionOption
                                            mainPageViewModel.sortFilter(selectionOption)
                                            sortExpanded = false
                                        }
                                    ) {
                                        Text(text = selectionOption)
                                    }
                                }
                            }
                        }
                        Button(
                            onClick = { mainPageViewModel.onAddBookClick() },
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
                        var displayedBooks = mainPageViewModel.getUserLibrary()
                        val library = displayedBooks.value
                        items(library.size) { index ->
                            BookItem(
                                library[index],
                                onClick = { mainPageViewModel.onBookClick(library[index]) }
                            )
                        }
                    }
                }
                if (mainPageViewModel.isHamburgerOpen) {
                    HamburgerMenuView(mainPageViewModel)
                }

                if (mainPageViewModel.isAddBookOpen) {
                    addBookWindow(mainPageViewModel)
                }

            }
        )
        if(mainPageViewModel.isBookOpen) {
            val bookModel = mainPageViewModel.bookOpened ?: BookModel(title = "error")
            BookView(mainPageViewModel, bookModel)
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier, mainPageViewModel: MainPageViewModel){
    var searchContent by remember {mutableStateOf("")}
    TextField(
        value = searchContent,
        onValueChange = {
            searchContent = it
            mainPageViewModel.searchResults(it)},
        leadingIcon = { Icon(
            Icons.Filled.Search,
            contentDescription = "searchIcon",
        )},
        placeholder = {Text("Search")},
        modifier = modifier
            .width(320.dp)
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
fun DropDown(options: List<String>, modifier: Modifier, onClick: (String) -> Unit){
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0])}

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .size(192.dp, 50.dp)
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

//@Composable
//fun refreshDisplay(mainPageViewModel: MainPageViewModel, modifier: Modifier) {
//    LazyVerticalGrid(
//        columns = GridCells.Fixed(5),
//        modifier = modifier
//    ) {
//        //coroutineScope.launch {
//        val library = mainPageViewModel.getUserLibrary()
//        items(library.size) { index ->
//            BookItem(
//                library[index],
//                onClick = { mainPageViewModel.onBookClick(library[index]) }
//            )
//        }
//        //}
//    }
//}

@Composable
fun addBookWindow(mainPageViewModel: MainPageViewModel) {

    var searchQuery = remember { mutableStateOf("") }
    var selectedBook = remember { mutableStateOf<Book?>(null) }
    var selectedStatus = remember { mutableStateOf("") }
    var bookResults = remember { mutableStateOf<List<Book>>(emptyList()) }
    var statusExpanded by remember { mutableStateOf(false) }

    val statusOptions = listOf("Status", "Complete", "Ongoing", "Dropped")

    val bookApiClient = BookApiClient()

    AlertDialog(
        onDismissRequest = {
            searchQuery.value = ""
            selectedBook.value = null
            selectedStatus.value = ""
            bookResults.value = emptyList()
            mainPageViewModel.onDismissAddBook()
        },
        title = { Text("Search") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 68.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = searchQuery.value,
                    onValueChange = { searchQuery.value = it },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    )
                )
                Button(
                    onClick = {
                        // Call API with searchText and populate searchResults
                        // This is a placeholder, replace it with your actual API call
                        bookResults.value = bookApiClient.searchBooks(searchQuery.value)
                    }
                ) {
                    Text("Search")
                }
                DropdownMenu(
                    expanded = bookResults.value.isNotEmpty(),
                    onDismissRequest = {
                        bookResults.value = emptyList()
                    },
                    modifier = Modifier.width(IntrinsicSize.Min)
                ) {
                    // Populate the dropdown menu with search results
                    bookResults.value.forEach { result ->
                        Text(result.title, modifier = Modifier.clickable {
                            // Handle selection of search result here
                            // For example, close popup and show details in a new window
                            selectedBook.value = result
                            bookResults.value = emptyList()
                        })
                    }
                }
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(192.dp, 50.dp)
                        .clip(RoundedCornerShape(1.dp))
                        .border(BorderStroke(1.dp, lightgrey), RoundedCornerShape(4.dp))
                        .clickable { statusExpanded = !statusExpanded },
                ) {
                    Text(
                        text = selectedStatus.value,
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
                        expanded = statusExpanded,
                        onDismissRequest = { statusExpanded = false }
                    ) {
                        statusOptions.forEach { selectionOption ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedStatus.value = selectionOption
                                    statusExpanded = false
                                }
                            ) {
                                Text(text = selectionOption)
                            }
                        }
                    }
                }
            }
               },
        confirmButton = {
            Button(onClick = {
                if (selectedBook.value != null && selectedStatus.value.isNotEmpty()) {
                    val bookInfo = selectedBook.value
                    val status = selectedStatus.value
                    val book = BookModel(bookInfo!!.title, bookInfo.authors, "coverNotAvailable.png",
                        bookInfo.publisher, bookInfo.publishYear, bookInfo.description, bookInfo.categories, status)
                    mainPageViewModel.addBook(book)
                    searchQuery.value = ""
                    selectedBook.value = null
                    selectedStatus.value = ""
                    bookResults.value = emptyList()
                    mainPageViewModel.onDismissAddBook()
                }
            },
//                enabled = selectedBook.value != null /*&& selectedStatus.value.isNotEmpty()*/
            ) {
                Text("Add")
            }
        }
    )

//    Dialog(
//        onDismissRequest = {
//            searchQuery.value = ""
//            selectedBook.value = null
//            selectedStatus.value = ""
//            bookResults.value = emptyList()
//            mainPageViewModel.onDismissAddBook()},
//        content = {
//            Column(
//                modifier = Modifier.padding(16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//                // You can add more content here if needed
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    TextField(
//                        value = searchQuery.value,
//                        onValueChange = { searchQuery.value = it },
//                        label = { Text("Search") },
//                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
//                        keyboardActions = KeyboardActions(onSearch = { /* Handle search */ })
//                    )
//                    Button(onClick = {
//                        bookResults.value = bookApiClient.searchBooks(searchQuery.value)
//                    }) {
//                        Text("Search")
//                    }
//                }
//                Spacer(modifier = Modifier.height(16.dp))
//                DropdownMenu(
//                    expanded = selectedBook.value != null,
//                    onDismissRequest = { selectedBook.value = null }
//                ) {
//                    bookResults.value.forEach { book ->
//                        DropdownMenuItem(
//                            onClick = {
//                                selectedBook.value = book
//                            }
//                        ) {
//                            Text(book.title)
//                        }
//                    }
//                }
//                Column(
//                    modifier = Modifier.padding(16.dp)
//                ) {
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    DropdownMenu(
//                        expanded = selectedStatus.value.isNotEmpty(),
//                        onDismissRequest = { selectedStatus.value = "" }
//                    ) {
//                        listOf("Complete", "Ongoing", "Dropped").forEach { status ->
//                            DropdownMenuItem(
//                                onClick = {
//                                    selectedStatus.value = status
//                                }
//                            ) {
//                                Text(status)
//                            }
//                        }
//                    }
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.End
//                    ) {
//                        Button(onClick = {
//                            if (selectedBook.value != null && selectedStatus.value.isNotEmpty()) {
//                                val bookInfo = selectedBook.value
//                                val status = selectedStatus.value
//                                val book = BookModel(bookInfo!!.title, bookInfo.authors, "coverNotAvailable.png",
//                                    bookInfo.publisher, bookInfo.publishYear, bookInfo.description, bookInfo.categories)
//                                mainPageViewModel.addBook(book)
//                                searchQuery.value = ""
//                                selectedBook.value = null
//                                selectedStatus.value = ""
//                                bookResults.value = emptyList()
//                                mainPageViewModel.onDismissAddBook()
//                            }
//                        },
//                            enabled = selectedBook.value != null && selectedStatus.value.isNotEmpty()
//                        ) {
//                            Text("Add")
//                        }
//                        Button(onClick = {
//                            searchQuery.value = ""
//                            selectedBook.value = null
//                            selectedStatus.value = ""
//                            bookResults.value = emptyList()
//                            mainPageViewModel.onDismissAddBook()
//                        }) {
//                            Text("Cancel")
//                        }
//                    }
//
//                }
//            }
//        }
//    )
}

