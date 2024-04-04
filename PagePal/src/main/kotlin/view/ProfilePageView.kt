package view

import LoginViewState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.runBlocking
import org.example.model.PasswordEncryption
import org.example.viewmodel.MainPageViewModel
import theme.darkblue
import theme.grey
import theme.lightbrown
import theme.whitevariation
import viewmodel.ProfilePageViewModel

@Composable
fun ProfilePageView(mainPageViewModel: MainPageViewModel, profilePageViewModel: ProfilePageViewModel, setCurrentState: (LoginViewState) -> Unit) {
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
                            profilePageViewModel.toggleShowPasswordDialog(true)
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
                            profilePageViewModel.toggleShowUsernameChangeDialog(true)
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
                            profilePageViewModel.toggleShowConfirmationDialog(true)
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
        if (profilePageViewModel.isShowConfirmationDialog()) {
            AlertDialog(
                onDismissRequest = { profilePageViewModel.toggleShowConfirmationDialog(false)},
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
                                mainPageViewModel.dbManager?.deleteUser(mainPageViewModel.userModel.username)
                            }
                            profilePageViewModel.toggleShowConfirmationDialog(false)
                            setCurrentState(LoginViewState(null, "login"))
                        }
                    ) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { profilePageViewModel.toggleShowConfirmationDialog(false)}
                    ) {
                        Text("No")
                    }
                },
                backgroundColor = darkblue
            )
        }
        if (profilePageViewModel.isShowUsernameChangeDialog()) {
            AlertDialog(
                onDismissRequest = { profilePageViewModel.toggleShowUsernameChangeDialog(false) },
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
                            value = profilePageViewModel.newUsername,
                            onValueChange = { profilePageViewModel.toggleNewUsername(it) },
                            label = { Text("New Username", color = lightbrown) },
                            textStyle = TextStyle(color = whitevariation)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = profilePageViewModel.verifyUsername,
                            onValueChange = { profilePageViewModel.toggleVerifyUsername(it) },
                            label = { Text("Verify New Username", color = lightbrown) },
                            textStyle = TextStyle(color = whitevariation)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val userExist = runBlocking {
                                mainPageViewModel.dbManager?.getUserByUsername(profilePageViewModel.newUsername)
                            }
                            if (userExist == null) {
                                if (profilePageViewModel.newUsername == profilePageViewModel.verifyUsername) {
                                    runBlocking {
                                        mainPageViewModel.dbManager?.updateUsername(mainPageViewModel.userModel.username, profilePageViewModel.newUsername)
                                    }
                                    mainPageViewModel.userModel.username = profilePageViewModel.newUsername
                                    profilePageViewModel.toggleShowUsernameChangeDialog(false)
                                } else {
                                    profilePageViewModel.toggleErrorMessage("Usernames do not match")
                                }
                            } else {
                                profilePageViewModel.toggleErrorMessage("Username already taken")
                            }
                        }
                    ) {
                        Text(text = "Change")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { profilePageViewModel.toggleShowUsernameChangeDialog(false)}
                    ) {
                        Text(text = "Cancel")
                    }
                },
                backgroundColor = darkblue
            )
        }
        if (profilePageViewModel.isShowPasswordDialog()) {
            AlertDialog(
                onDismissRequest = { profilePageViewModel.toggleShowPasswordDialog(false) },
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
                            value = profilePageViewModel.currentPassword,
                            onValueChange = { profilePageViewModel.toggleCurrentPassword(it) },
                            label = { Text("Current Password", color = lightbrown) },
                            textStyle = TextStyle(color = whitevariation)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = profilePageViewModel.newPassword,
                            onValueChange = { profilePageViewModel.toggleNewPassword(it) },
                            label = { Text("New Password", color = lightbrown) },
                            textStyle = TextStyle(color = whitevariation)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = profilePageViewModel.verifyPassword,
                            onValueChange = { profilePageViewModel.toggleVerifyPassword(it)},
                            label = { Text("Verify New Password", color = lightbrown) },
                            textStyle = TextStyle(color = whitevariation)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (PasswordEncryption.verifyPassword(profilePageViewModel.currentPassword, mainPageViewModel.userModel.password)) {
                                if (profilePageViewModel.newPassword == profilePageViewModel.verifyPassword) {
                                    runBlocking {
                                        mainPageViewModel.dbManager?.changePassword(mainPageViewModel.userModel.username, profilePageViewModel.currentPassword, profilePageViewModel.newPassword)
                                    }
                                    mainPageViewModel.userModel.password = PasswordEncryption.hashPassword(profilePageViewModel.newPassword)
                                    profilePageViewModel.toggleShowPasswordDialog(false)
                                } else {
                                    profilePageViewModel.toggleErrorMessage("Passwords do not match")
                                }
                            } else {
                                profilePageViewModel.toggleErrorMessage("Incorrect current password")
                            }
                        }
                    ) {
                        Text(text = "Change")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { profilePageViewModel.toggleShowPasswordDialog(false) }
                    ) {
                        Text(text = "Cancel")
                    }
                },
                backgroundColor = darkblue
            )
        }
        if (profilePageViewModel.errorMessage.isNotEmpty()) {
            AlertDialog(
                onDismissRequest = { profilePageViewModel.toggleErrorMessage("") },
                title = {
                    Text(
                        text = "Error",
                        color = Color.White
                    )
                },
                text = {
                    Text(
                        text = profilePageViewModel.errorMessage,
                        color = Color.White
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { profilePageViewModel.toggleErrorMessage("")}
                    ) {
                        Text(text = "OK")
                    }
                },
                backgroundColor = darkblue
            )
        }
    }
}
