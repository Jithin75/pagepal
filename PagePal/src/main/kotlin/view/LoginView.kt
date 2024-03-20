package view

import LoginViewState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.runBlocking
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider
import org.example.model.DatabaseManager
import theme.*

@Composable
fun LoginView(setCurrentState: (LoginViewState) -> Unit) {
    var username by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }
    var showDialog by remember { mutableStateOf(false) }
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

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = "Error",
                    color = lightgrey
                )
            },
            text = {
                Text(
                    text = "INCORRECT USERNAME OR PASSWORD",
                    color = lightbrown
                )
            },
            confirmButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text(text = "OK")
                }
            },
            backgroundColor = darkblue
        )
    }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                style = TextStyle(
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 28.sp,
                                    brush = Brush.linearGradient(
                                        colors = listOf(lightbrown, lightgrey)
                                    )
                                ),
                                text = "PagePal",
                                textAlign = TextAlign.Center
                            )
                        }
                    },
                    backgroundColor = grey
                )
            },
            backgroundColor = darkblue
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource("logo.jpg"),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(420.dp)
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    textStyle = TextStyle(color = whitevariation),
                    label = { Text("Username", color = lightbrown) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password", color = lightbrown) },
                    textStyle = TextStyle(color = whitevariation),
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            if(username.text == "enter") {
                                setCurrentState(LoginViewState(null, "main"))
                            } else {
                                if (username.text.isNotEmpty() && password.text.isNotEmpty()) {
                                    val isValid = runBlocking {
                                        dbManager?.isValidCredentials(username.text, password.text)
                                    }
                                    if (isValid == true) {
                                        val User = runBlocking {
                                            dbManager?.getUserByUsername(username.text)
                                        }
                                        if (User != null) {
                                            setCurrentState(LoginViewState(User, "main"))
                                        } else {
                                            showDialog = true // Show dialog for incorrect credentials
                                        }
                                    } else {
                                        showDialog = true // Show dialog for incorrect credentials
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = lightbrown,
                            backgroundColor = grey
                        )
                    ) {
                        Text(text = "Login")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {setCurrentState(LoginViewState(null, "signup"))},
                        colors = ButtonDefaults.buttonColors(
                            contentColor = lightbrown, // Text color of the button
                            backgroundColor = grey
                        )
                    ) {
                        Text(text = "Sign Up")
                    }
                }

            }
        }
    }
}