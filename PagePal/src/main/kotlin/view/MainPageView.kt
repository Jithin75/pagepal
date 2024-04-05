package org.example.view

import LoginViewState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import model.ImageLoader
import model.api.Book
import model.api.BookApiClient
import org.example.model.BookModel
import org.example.viewmodel.MainPageViewModel
import theme.*
import view.BookView
import view.HamburgerMenuView
import viewmodel.BookViewModel


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainPageView(mainPageViewModel: MainPageViewModel, setCurrentState: (LoginViewState) -> Unit) {
    val sortOptions =  listOf("Default","Title", "Author", "Recently Added")
    val statusOptions = listOf("All","New", "In Progress", "Completed")

    var sortExpanded by remember { mutableStateOf(false) }
    var sortSelectedOptionText by remember { mutableStateOf(sortOptions[0])}

    var statusExpanded by remember { mutableStateOf(false) }
    var statusSelectedOptionText by remember { mutableStateOf(statusOptions[0])}

    var searchContent by remember {mutableStateOf("")}
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val searchFocusRequester = remember { FocusRequester() }


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
                                .padding(end = 68.dp)
                                .focusable(true)
                                .onKeyEvent { event ->
                                    if (event.isCtrlPressed && event.key == Key.K) {
                                        println("Ctrl + K is pressed")
                                        searchFocusRequester.requestFocus()
                                        true
                                    } else {
                                        false
                                    }
                                },
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
                        .focusable(true)
                        .onKeyEvent { event ->
                            if (event.isCtrlPressed && event.key == Key.K) {
                                println("Ctrl + K is pressed")
                                searchFocusRequester.requestFocus()
                                true
                            } else {
                                false
                            }
                    }
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
                        Spacer(modifier = Modifier.weight(1f))
                        Row(
                            modifier = Modifier.weight(6f)
                        ) {
                            TextField(
                                value = searchContent,
                                onValueChange = { newValue : String ->
                                    searchContent = newValue
                                    mainPageViewModel.filter(sortSelectedOptionText, statusSelectedOptionText, searchContent)
                                },
                                leadingIcon = { Icon(
                                    Icons.Filled.Search,
                                    contentDescription = "searchIcon",
                                )},
                                placeholder = {Text("Search")},

                                shape = RoundedCornerShape(16.dp),
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = whitevariation,
                                    leadingIconColor = Color.Black,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    placeholderColor = lightgrey,
                                    cursorColor = Color.Black,
                                ),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        focusManager.clearFocus()
                                        keyboardController?.hide()
                                    }
                                ),
                                modifier = Modifier
                                    .width(320.dp)
                                    .height(50.dp)
                                    .padding(horizontal = 8.dp)
                                    .align(Alignment.CenterVertically)
                                    .weight(1.2f)
                                    .focusRequester(searchFocusRequester)
                            )
                            /*
                            SearchBar(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .align(Alignment.CenterVertically)
                                    .weight(1.2f),
                                mainPageViewModel
                            )*/
                            /*
                            DropDown(
                                listOf("Genre", "Horror", "Fantasy", "SciFi"),
                                Modifier
                                    .padding(horizontal = 8.dp)
                                    .align(Alignment.CenterVertically)
                            ) {}*/
                            Box(
                                contentAlignment = Alignment.CenterStart,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .align(Alignment.CenterVertically)
                                    .size(192.dp, 50.dp)
                                    .weight(0.9f)
                                    .clip(RoundedCornerShape(1.dp))
                                    .border(BorderStroke(1.dp, lightgrey), RoundedCornerShape(4.dp))
                                    .clickable { statusExpanded = !statusExpanded },
                            ) {
                                Text(
                                    text = if(statusSelectedOptionText == "All") { "Status" }
                                    else { statusSelectedOptionText },
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
                                                mainPageViewModel.filter(sortSelectedOptionText, statusSelectedOptionText, searchContent)
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
                                    .weight(0.9f)
                                    .clip(RoundedCornerShape(1.dp))
                                    .border(BorderStroke(1.dp, lightgrey), RoundedCornerShape(4.dp))
                                    .clickable { sortExpanded = !sortExpanded },
                            ) {
                                Text(
                                    text = if(sortSelectedOptionText == "Default") { "Sort" }
                                    else { sortSelectedOptionText },
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
                                                mainPageViewModel.filter(sortSelectedOptionText, statusSelectedOptionText, searchContent)
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
                                    .size(68.dp,36.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = green)
                            ) {
                                Text("ADD")
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Book Grid
                    val scrollState = rememberLazyGridState()
                    mainPageViewModel.filter(sortSelectedOptionText, statusSelectedOptionText, searchContent)
                    val displayedBooks = mainPageViewModel.getUserLibrary()
                    LazyVerticalGrid(
                        state = scrollState,
                        columns = GridCells.Fixed(5),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        val library = displayedBooks.toList()
                        items(items = library, key = { it.bookId }) { book : BookModel ->
                            BookItem(
                                book,
                                onClick = {mainPageViewModel.onBookClick(book)}
                            )
                        }
                    }

                    LaunchedEffect(displayedBooks) {
                        scrollState.scrollToItem(0)
                    }
                }
                if (mainPageViewModel.isHamburgerOpen) {
                    HamburgerMenuView(mainPageViewModel, setCurrentState)
                }

                if (mainPageViewModel.isAddBookOpen) {
                    addBookWindow(mainPageViewModel)
                }
            }
        )
        if(mainPageViewModel.isBookOpen) {
            val bookModel = mainPageViewModel.bookOpened ?: BookModel(title = "error")
            BookView(mainPageViewModel, BookViewModel(bookModel, mainPageViewModel.dbManager))
        }
    }
}

/*
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
        ),
        singleLine = true
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
}*/

@Composable
fun BookItem(book: BookModel, onClick: () -> Unit) {
    val imageLoader = ImageLoader()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {

        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // cover
            if (book.cover == "coverNotAvailable.png") {
                val painter = painterResource(book.cover)
                Image(
                    painter = painter,
                    contentDescription = book.title,
                    modifier = Modifier.size(300.dp, 300.dp)
                )
            } else {
                imageLoader.AsyncImage(
                    load = { imageLoader.loadImageBitmap(book.cover) },
                    painterFor = { remember { BitmapPainter(it) } },
                    contentDescription = book.title,
                    modifier = Modifier.size(300.dp, 300.dp)
                )
            }

            // title
            Text(
                text = book.title,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = whitevariation
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
                    color = whitevariation
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

    }
}

@Composable
fun addBookWindow(mainPageViewModel: MainPageViewModel) {

    var searchQuery by remember { mutableStateOf("") }
    var selectedBook by remember { mutableStateOf<Book?>(null) }
    var selectedStatus by remember { mutableStateOf("New") }
    var bookResults by remember { mutableStateOf<List<Book>>(emptyList()) }
    var chapter by remember { mutableStateOf("1") }
    var page by remember { mutableStateOf("1") }
    var bookModel by remember { mutableStateOf<BookModel?>(null)}
    val imageLoader = ImageLoader()
    //var statusExpanded by remember { mutableStateOf(false) }

    val statusList : List<String> = listOf("New", "In Progress", "Completed")

    val bookApiClient = BookApiClient()

    Popup(
        alignment = Alignment.Center,
        onDismissRequest = {
            searchQuery = ""
            selectedBook = null
            selectedStatus = ""
            bookResults = emptyList()
            bookModel = null
            mainPageViewModel.onDismissAddBook()
        },
        properties = PopupProperties(focusable = true),
        content = {
            Column (
                modifier = Modifier
                    .background(color = darkblue, shape = RoundedCornerShape(16.dp))
                    .border(
                        BorderStroke(width = 4.dp, color = grey),
                        shape = RoundedCornerShape(16.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 32.dp,
                        bottom = 20.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            bookModel = null
                        },
                        label = { Text("Title", color = lightbrown) },
                        textStyle = TextStyle(color = whitevariation),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Search
                        )
                    )

                    Spacer(Modifier.width(10.dp))

                    OutlinedButton(
                        onClick = {
                            // Call API with searchText and populate searchResults
                            // This is a placeholder, replace it with your actual API call
                            bookResults = bookApiClient.searchBooks(searchQuery)
                            bookModel = null
                        },
                        Modifier
                            .align(Alignment.CenterVertically)
                            .width(100.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, lightbrown),
                        colors = ButtonDefaults.outlinedButtonColors(
                            backgroundColor = darkblue,
                            contentColor = lightbrown
                        )
                    ) {
                        Text("SEARCH")
                    }
                    DropdownMenu(
                        expanded = bookResults.isNotEmpty(),
                        onDismissRequest = {
                            bookResults = emptyList()
                        },
                        modifier = Modifier.width(IntrinsicSize.Min)
                    ) {
                        // Populate the dropdown menu with search results
                        bookResults.forEach { result ->
                            Text(result.title, modifier = Modifier.clickable {
                                // Handle selection of search result here
                                // For example, close popup and show details in a new window
                                selectedBook = result
                                searchQuery = result.title
                                bookResults = emptyList()
                                val bookInfo = selectedBook
                                val pattern = Regex("&zoom=\\d+")
                                val replacement = "&zoom=${mainPageViewModel.coverQuality}"
                                var cover = bookInfo!!.img
                                cover = cover.replace(pattern, replacement)
                                bookModel = BookModel(
                                    title = bookInfo.title,
                                    author = bookInfo.authors,
                                    cover = cover,
                                    publisher = bookInfo.publisher,
                                    publishYear = bookInfo.publishYear,
                                    description = bookInfo.description,
                                    categories = bookInfo.categories,
                                    status = selectedStatus,
                                    chapter = chapter,
                                    page = page)
                            })
                        }
                    }
                }

                if(bookModel != null) {
                    imageLoader.AsyncImage(
                        load = { imageLoader.loadImageBitmap(bookModel!!.cover) },
                        painterFor = { remember { BitmapPainter(it) } },
                        contentDescription = bookModel!!.title,
                        modifier = Modifier
                            .size(160.dp, 160.dp)
                    )
                }

                Row(
                    modifier = Modifier.padding(
                        vertical = 20.dp,
                        horizontal = 32.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    val cornerRadius = 16.dp
                    var selectedIndex by remember { mutableStateOf(statusList.indexOf(selectedStatus)) }

                    statusList.forEachIndexed { index, status ->

                        OutlinedButton(
                            onClick = {
                                selectedIndex = index
                                selectedStatus = statusList[index]
                            },
                            modifier = when (index) {
                                0 -> Modifier
                                    .offset(0.dp, 0.dp)
                                    .zIndex(if (selectedIndex == index) 1f else 0f)

                                else -> Modifier
                                    .offset((-1 * index).dp, 0.dp)
                                    .zIndex(if (selectedIndex == index) 1f else 0f)
                            },
                            shape = when (index) {
                                0 -> RoundedCornerShape(
                                    topStart = cornerRadius,
                                    topEnd = 0.dp,
                                    bottomStart = cornerRadius,
                                    bottomEnd = 0.dp
                                )

                                statusList.size - 1 -> RoundedCornerShape(
                                    topStart = 0.dp,
                                    topEnd = cornerRadius,
                                    bottomStart = 0.dp,
                                    bottomEnd = cornerRadius
                                )

                                else -> RoundedCornerShape(
                                    topStart = 0.dp,
                                    topEnd = 0.dp,
                                    bottomStart = 0.dp,
                                    bottomEnd = 0.dp
                                )
                            },
                            border = BorderStroke(
                                1.dp, if (selectedIndex == index) {
                                    lightbrown
                                } else {
                                    lightbrown.copy(alpha = 0.75f)
                                }
                            ),
                            colors = if (selectedIndex == index) {
                                ButtonDefaults.outlinedButtonColors(
                                    backgroundColor = lightbrown.copy(alpha = 0.75f),
                                    contentColor = lightbrown
                                )
                            } else {
                                ButtonDefaults.outlinedButtonColors(
                                    backgroundColor = darkblue,
                                    contentColor = lightbrown
                                )
                            }
                        ) {
                            Text(status)
                        }
                    }
                }

                if(selectedStatus == "In Progress") {

                    TextField(
                        value = chapter,
                        onValueChange = {
                            if(it.isNotEmpty()) {
                                chapter = it.filter { symbol ->
                                    symbol.isDigit()
                                }
                            }
                            else {
                                chapter = ""
                            }
                        },
                        label = { Text("Chapter", color = lightbrown) },
                        textStyle = TextStyle(color = whitevariation),
                        singleLine = true,
                        modifier = Modifier
                            .widthIn(min = 200.dp, max = 240.dp)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    // Input field for Page
                    TextField(
                        value = page,
                        onValueChange = {
                            if(it.isNotEmpty()) {
                                page = it.filter { symbol ->
                                    symbol.isDigit()
                                }
                            }
                            else {
                                page = ""
                            }
                        },
                        label = { Text("Page", color = lightbrown) },
                        textStyle = TextStyle(color = whitevariation),
                        singleLine = true,
                        modifier = Modifier
                            .widthIn(min = 200.dp, max = 240.dp)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                Row(
                    modifier = Modifier.padding(top = 25.dp, bottom = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(
                        onClick = {
                            searchQuery = ""
                            selectedBook = null
                            selectedStatus = ""
                            bookResults = emptyList()
                            mainPageViewModel.onDismissAddBook()
                        },
                        modifier = Modifier.width(120.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, lightbrown),
                        colors = ButtonDefaults.outlinedButtonColors(
                            backgroundColor = darkblue,
                            contentColor = lightbrown
                        )
                    ) {
                        Text("CLOSE")
                    }

                    Spacer(modifier = Modifier.width(96.dp))

                    OutlinedButton(
                        onClick = {
                            if (bookModel != null) {
                                /*
                                val bookInfo = selectedBook
                                val pattern = Regex("&zoom=\\d+")
                                val replacement = "&zoom=3"
                                var cover = bookInfo!!.img
                                cover = cover.replace(pattern, replacement)
                                val book = BookModel(
                                    title = bookInfo.title,
                                    author = bookInfo.authors,
                                    cover = cover,
                                    publisher = bookInfo.publisher,
                                    publishYear = bookInfo.publishYear,
                                    description = bookInfo.description,
                                    categories = bookInfo.categories,
                                    status = selectedStatus,
                                    chapter = chapter,
                                    page = page)*/
                                mainPageViewModel.addBook(bookModel!!)
                                searchQuery = ""
                                selectedBook = null
                                selectedStatus = ""
                                bookResults = emptyList()
                                chapter = "1"
                                page = "1"
                                bookModel = null
                                mainPageViewModel.onDismissAddBook()
                            }
                        },
                        modifier = Modifier.width(120.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, lightbrown),
                        colors = ButtonDefaults.outlinedButtonColors(
                            backgroundColor = darkblue,
                            contentColor = lightbrown
                        )
                    ) {
                        Text("ADD")
                    }
                }
                /*
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
                }*/
            }
        }
    )
    /*
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
                            searchQuery.value = result.title
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
                if (selectedBook.value != null && selectedStatus.value != "Status") {
                    val bookInfo = selectedBook.value
                    val status = selectedStatus.value
                    val book = BookModel(bookInfo!!.title, bookInfo.authors, bookInfo.img,
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
    ) */
}

