package view

import LoginViewState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.runBlocking
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider
import org.example.model.DatabaseManager
import org.example.model.PasswordEncryption
import org.example.viewmodel.MainPageViewModel
import theme.darkblue
import theme.grey
import theme.lightbrown
import theme.whitevariation

@Composable
fun ProfilePageView(mainPageViewModel: MainPageViewModel, setCurrentState: (LoginViewState) -> Unit) {

    var dbManager: DatabaseManager? by remember { mutableStateOf(null) }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var verifyPassword by remember { mutableStateOf("") }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var newUsername by remember { mutableStateOf("") }
    var verifyUsername by remember { mutableStateOf("") }
    var showUsernameChangeDialog by remember { mutableStateOf(false) }


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

    MaterialTheme {
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
                        IconButton(onClick = {mainPageViewModel.toggleProfilePage()}) {
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
                        .background(color = darkblue),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Picture
                    Image(
                        painter = painterResource("logo.jpg"),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Username
                    Text(
                        text = mainPageViewModel.userModel.username,
                        color = whitevariation,
                        style = MaterialTheme.typography.h6
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Personal Information

                    Spacer(modifier = Modifier.height(32.dp))

                    // Settings Options
                    Button(
                        onClick = {
                            showPasswordDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = lightbrown,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Change Password")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            showUsernameChangeDialog = true;
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = lightbrown,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Change Username")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            showConfirmationDialog = true;
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = lightbrown,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Delete Account")
                    }
                }
            }
        )
        if (showConfirmationDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmationDialog = false },
                title = {
                    Text(
                        text = "Delete Account",
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to delete your account?",
                        color = Color.White
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            runBlocking {
                                dbManager?.deleteUser(mainPageViewModel.userModel.username)
                            }
                            showConfirmationDialog = false
                            setCurrentState(LoginViewState(null, "login"))
                        }
                    ) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showConfirmationDialog = false }
                    ) {
                        Text("No")
                    }
                },
                backgroundColor = darkblue
            )
        }
        if (showUsernameChangeDialog) {
            AlertDialog(
                onDismissRequest = { showUsernameChangeDialog = false },
                title = {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 20.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Change Username",
                            color = Color.White,
                        )
                    }
                },
                text = {
                    Column {
                        OutlinedTextField(
                            value = newUsername,
                            onValueChange = { newUsername = it },
                            label = { Text("New Username", color = lightbrown) },
                            textStyle = TextStyle(color = whitevariation)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = verifyUsername,
                            onValueChange = { verifyUsername = it },
                            label = { Text("Verify New Username", color = lightbrown) },
                            textStyle = TextStyle(color = whitevariation)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val userExist = runBlocking {
                                dbManager?.getUserByUsername(newUsername)
                            }
                            if (userExist == null) {
                                if (newUsername == verifyUsername) {
                                    runBlocking {
                                        dbManager?.updateUsername(mainPageViewModel.userModel.username, newUsername)
                                    }
                                    mainPageViewModel.userModel.username = newUsername
                                    showUsernameChangeDialog = false
                                } else {
                                    errorMessage = "Usernames do not match"
                                }
                            } else {
                                errorMessage = "Username already taken"
                            }
                        }
                    ) {
                        Text(text = "Change")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showUsernameChangeDialog = false }
                    ) {
                        Text(text = "Cancel")
                    }
                },
                backgroundColor = darkblue
            )
        }
        if (showPasswordDialog) {
            AlertDialog(
                onDismissRequest = { showPasswordDialog = false },
                title = {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 20.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Change Password",
                            color = Color.White,
                        )
                    }
                },
                text = {
                    Column {
                        OutlinedTextField(
                            value = currentPassword,
                            onValueChange = { currentPassword = it },
                            label = { Text("Current Password", color = lightbrown) },
                            textStyle = TextStyle(color = whitevariation)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            label = { Text("New Password", color = lightbrown) },
                            textStyle = TextStyle(color = whitevariation)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = verifyPassword,
                            onValueChange = { verifyPassword = it },
                            label = { Text("Verify New Password", color = lightbrown) },
                            textStyle = TextStyle(color = whitevariation)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (PasswordEncryption.verifyPassword(currentPassword, mainPageViewModel.userModel.password)) {
                                if (newPassword == verifyPassword) {
                                    runBlocking {
                                        dbManager?.changePassword(mainPageViewModel.userModel.username, currentPassword, newPassword)
                                    }
                                    mainPageViewModel.userModel.password = PasswordEncryption.hashPassword(newPassword)
                                    showPasswordDialog = false
                                } else {
                                    errorMessage = "Passwords do not match"
                                }
                            } else {
                                errorMessage = "Incorrect current password"
                            }
                        }
                    ) {
                        Text(text = "Change")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showPasswordDialog = false }
                    ) {
                        Text(text = "Cancel")
                    }
                },
                backgroundColor = darkblue
            )
        }
        if (errorMessage.isNotEmpty()) {
            AlertDialog(
                onDismissRequest = { errorMessage = "" },
                title = {
                    Text(
                        text = "Error",
                        color = Color.White
                    )
                },
                text = {
                    Text(
                        text = errorMessage,
                        color = Color.White
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { errorMessage = "" }
                    ) {
                        Text(text = "OK")
                    }
                },
                backgroundColor = darkblue
            )
        }
    }
}
