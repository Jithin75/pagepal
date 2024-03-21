package view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import org.example.model.BookModel
import org.example.viewmodel.MainPageViewModel
import theme.*
import viewmodel.BookViewModel


@Composable
fun BookView(mainPageViewModel: MainPageViewModel, bookViewModel: BookViewModel){
    MaterialTheme{
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
                        IconButton(onClick = {mainPageViewModel.onDismissBook()}) {
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
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = darkblue)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(420.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        val painter = painterResource(bookViewModel.getCover())
                        //cover
                        Image(
                            painter = painter,
                            contentDescription = bookViewModel.getTitle(),
                            modifier = Modifier.size(500.dp)
                        )

                        //title
                        Text(
                            text = bookViewModel.getTitle(),
                            style = TextStyle(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp,
                                color = whitevariation
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 10.dp)
                        )

                        //author
                        Text(
                            text = bookViewModel.getAuthor(),
                            style = TextStyle(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp,
                                color = whitevariation
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(vertical = 6.dp)
                        )
                    }
                    Column (
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Row (
                            modifier = Modifier
                                .height(80.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = {bookViewModel.onEditClick()},
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(horizontal = 20.dp),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = lightbrown,
                                    contentColor = whitevariation
                                )
                            ) {
                                Text("EDIT")
                            }
                        }
                        Column (
                            Modifier.padding(horizontal = 20.dp)
                        ) {
                            Text(
                                text = "SUMMARY:",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = whitevariation
                                ),
                                textAlign = TextAlign.Left,
                                modifier = Modifier.padding(vertical = 20.dp)
                            )

                            Text(
                                text = bookViewModel.getSummary(),
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    color = whitevariation,
                                    lineHeight = 20.sp,
                                    lineHeightStyle = LineHeightStyle(
                                        alignment = LineHeightStyle.Alignment.Center,
                                        trim = LineHeightStyle.Trim.None
                                    ),
                                ),
                                textAlign = TextAlign.Justify,
                            )

                            Row(
                                modifier = Modifier
                                    .padding(vertical = 120.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "STATUS:",
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = whitevariation
                                    ),
                                    textAlign = TextAlign.Left,
                                )

                                Text(
                                    text = bookViewModel.getStatus(),
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = green
                                    ),
                                    textAlign = TextAlign.Left,
                                    modifier = Modifier.padding(start = 10.dp)
                                )
                            }
                        }
                    }
                }
                if(bookViewModel.isEditOpen) {
                    editWindow(bookViewModel)
                }
            }
        )
    }
}

@Composable
fun editWindow(bookViewModel: BookViewModel) {
    var selectedStatus by remember {mutableStateOf(bookViewModel.getStatus())}
    var statusList : List<String> = listOf("New", "In Progress", "Completed")

    Popup(
        alignment = Alignment.Center,
        onDismissRequest = {bookViewModel.onDismissEdit()},
        content = {
            Column(
                modifier = Modifier
                    .background(color = darkblue)
                    .border(
                        BorderStroke(width = 4.dp, color = grey),
                        shape = RoundedCornerShape(16.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.padding(
                        vertical = 24.dp,
                        horizontal = 32.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "STATUS:",
                        style = TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = whitevariation
                        ),
                        textAlign = TextAlign.Left,
                        modifier = Modifier.padding(end = 20.dp)
                    )

                    val cornerRadius = 16.dp
                    var selectedIndex by remember {mutableStateOf(statusList.indexOf(selectedStatus))}

                    statusList.forEachIndexed { index, status ->

                        OutlinedButton(
                            onClick = {
                                selectedIndex = index
                                selectedStatus = statusList[index]
                            },
                            modifier = when(index) {
                                0 -> Modifier
                                    .offset(0.dp, 0.dp)
                                    .zIndex(if (selectedIndex == index) 1f else 0f)
                                else -> Modifier
                                    .offset((-1 * index).dp, 0.dp)
                                    .zIndex(if (selectedIndex == index) 1f else 0f)
                            },
                            shape = when(index) {
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

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {/* Delete book functionality */},
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Red,
                            contentColor = Color.White
                        )
                    ) {
                        Text("DELETE")
                    }
                }

                Row(
                    modifier = Modifier.padding(top = 40.dp, bottom = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(
                        onClick = {bookViewModel.onDismissEdit()},
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
                        onClick = {/* Confirm change functionality */},
                        modifier = Modifier.width(120.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, lightbrown),
                        colors = ButtonDefaults.outlinedButtonColors(
                            backgroundColor = darkblue,
                            contentColor = lightbrown
                        )
                    ) {
                        Text("CONFIRM")
                    }
                }
            }
        }
    )
}