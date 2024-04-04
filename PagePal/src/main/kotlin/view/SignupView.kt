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
import org.example.model.UserModel
import theme.*

@Composable
fun SignupView(setCurrentState: (LoginViewState) -> Unit) {
    var username by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }
    var password_confirm by remember { mutableStateOf(TextFieldValue()) }
    var showDialog by remember { mutableIntStateOf(0) }
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

    if (showDialog != 0) {
        AlertDialog(
            onDismissRequest = { showDialog = 0 },
            title = {
                Text(
                    text = "Error",
                    color = lightgrey
                )
            },
            text = {
                if (showDialog == 1) {
                    Text(
                        text = "PASSWORD MISMATCH, PLEASE ENTER THE SAME PASSWORD",
                        color = lightbrown
                    )
                } else if(showDialog == 2) {
                    Text(
                        text = "USERNAME ALREADY EXISTS",
                        color = lightbrown
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showDialog = 0 }
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
                        .size(220.dp)
                        .weight(1f)
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    textStyle = TextStyle(color = whitevariation),
                    label = { Text("Username", color = lightbrown) },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password", color = lightbrown) },
                    textStyle = TextStyle(color = whitevariation),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password_confirm,
                    onValueChange = { password_confirm = it },
                    label = { Text("Confirm Password", color = lightbrown) },
                    textStyle = TextStyle(color = whitevariation),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {setCurrentState(LoginViewState(null, "login"))},
                        colors = ButtonDefaults.buttonColors(
                            contentColor = lightbrown, // Text color of the button
                            backgroundColor = grey
                        )
                    ) {
                        Text(text = "Back to Login")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            val userExist = runBlocking {
                                dbManager?.getUserByUsername(username.text)
                            }
                            if (userExist != null) {
                                showDialog = 2
                            } else {
                                if (username.text.isNotEmpty() && password.text.isNotEmpty() && password_confirm.text.isNotEmpty()) {
                                    if (password.text.trim() != password_confirm.text.trim()) {
                                        showDialog = 1
                                    } else {
                                        runBlocking {
                                            dbManager?.addUser(
                                                UserModel(
                                                    username.text.trim(),
                                                    password.text.trim(),
                                                    mutableListOf()
                                                )
                                            )
                                        }
                                        setCurrentState(LoginViewState(null, "login"))
                                    }
                                }
                            }
                                  },
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

