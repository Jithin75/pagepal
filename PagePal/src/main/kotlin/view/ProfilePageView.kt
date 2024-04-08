package view

import LoginViewState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.runBlocking
import model.PasswordEncryption
import viewmodel.MainPageViewModel
import theme.darkblue
import theme.grey
import theme.lightbrown
import theme.whitevariation
import viewmodel.ProfilePageViewModel

@Composable
fun ProfilePageView(mainPageViewModel: MainPageViewModel, profilePageViewModel: ProfilePageViewModel, setCurrentState: (LoginViewState) -> Unit) {
    val qualityList : List<String> = listOf("Low", "Medium", "High")

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
                        //style = MaterialTheme.typography.h6,
                        style = TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 26.sp,
                            color = whitevariation,
                            fontFamily = FontFamily.Monospace
                        )
                    )

                    // Book Cover Resolution
                    Text(
                        text = "Resolution of covers for future books added:",
                        style = TextStyle(
                            fontWeight = FontWeight.W200,
                            fontSize = 14.sp,
                            color = lightbrown,
                        ),
                        modifier = Modifier.padding(top = 32.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        val cornerRadius = 16.dp
                        var selectedIndex by remember { mutableStateOf(mainPageViewModel.userModel.coverQuality.toInt() - 1) }

                        qualityList.forEachIndexed { index, quality ->

                            OutlinedButton(
                                onClick = {
                                    selectedIndex = index
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

                                    qualityList.size - 1 -> RoundedCornerShape(
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
                                Text(quality)
                            }
                        }

                        OutlinedButton(
                            onClick = {
                                mainPageViewModel.changeCoverQuality((selectedIndex+1).toString())
                            },
                            Modifier
                                .width(100.dp)
                                .padding(start = 20.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, lightbrown),
                            colors = ButtonDefaults.outlinedButtonColors(
                                backgroundColor = darkblue,
                                contentColor = lightbrown
                            )
                        ) {
                            Text("SAVE")
                        }
                    }

                    Text(
                        text = "(Note: Some covers are only available in Low Resolution)",
                        style = TextStyle(
                            fontWeight = FontWeight.W300,
                            fontSize = 11.sp,
                            color = Color.Red,
                        ),
                        fontStyle = FontStyle.Italic,
                    )

                    // Personal Information

                    Spacer(modifier = Modifier.height(20.dp))

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
        if (profilePageViewModel.showConfirmationDialog) {
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
                            profilePageViewModel.deleteAccount()
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
        if (profilePageViewModel.showUsernameChangeDialog) {
            AlertDialog(
                onDismissRequest = { profilePageViewModel.toggleShowUsernameChangeDialog(false) },
                text = {
                    Column {
                        Text(
                            text = "Change Username",
                            color = Color.White,
                            style = TextStyle(
                                fontSize = 20.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = profilePageViewModel.newUsername,
                            onValueChange = { profilePageViewModel.toggleNewUsername(it) },
                            label = { Text("New Username", color = lightbrown) },
                            textStyle = TextStyle(color = whitevariation),
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                unfocusedBorderColor = lightbrown // Set the outline color when not focused
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = profilePageViewModel.verifyUsername,
                            onValueChange = { profilePageViewModel.toggleVerifyUsername(it) },
                            label = { Text("Verify New Username", color = lightbrown) },
                            textStyle = TextStyle(color = whitevariation),
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                unfocusedBorderColor = lightbrown // Set the outline color when not focused
                            )
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val userExist = runBlocking {
                                mainPageViewModel.dbManager.getUserByUsername(profilePageViewModel.newUsername)
                            }
                            if (userExist == null) {
                                if (profilePageViewModel.newUsername == profilePageViewModel.verifyUsername) {
                                    profilePageViewModel.updateUsername()
                                    profilePageViewModel.toggleNewUsername("")
                                    profilePageViewModel.toggleVerifyUsername("")
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
        if (profilePageViewModel.showPasswordDialog) {
            AlertDialog(
                onDismissRequest = { profilePageViewModel.toggleShowPasswordDialog(false) },
                text = {
                    Column {
                        Text(
                            text = "Change Password",
                            color = Color.White,
                            style = TextStyle(
                                fontSize = 20.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = profilePageViewModel.currentPassword,
                            onValueChange = { profilePageViewModel.toggleCurrentPassword(it)},
                            label = { Text("Current Password", color = lightbrown) },
                            textStyle = TextStyle(color = whitevariation),
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                unfocusedBorderColor = lightbrown // Set the outline color when not focused
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = profilePageViewModel.newPassword,
                            onValueChange = { profilePageViewModel.toggleNewPassword(it) },
                            label = { Text("New Password", color = lightbrown) },
                            textStyle = TextStyle(color = whitevariation),
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                unfocusedBorderColor = lightbrown // Set the outline color when not focused
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = profilePageViewModel.verifyPassword,
                            onValueChange = { profilePageViewModel.toggleVerifyPassword(it) },
                            label = { Text("Verify New Password", color = lightbrown) },
                            textStyle = TextStyle(color = whitevariation),
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                unfocusedBorderColor = lightbrown // Set the outline color when not focused
                            )
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (PasswordEncryption.verifyPassword(profilePageViewModel.currentPassword, mainPageViewModel.userModel.password)) {
                                if (profilePageViewModel.newPassword == profilePageViewModel.verifyPassword) {
                                    profilePageViewModel.updatePassword()
                                    profilePageViewModel.toggleNewPassword("")
                                    profilePageViewModel.toggleVerifyPassword("")
                                    profilePageViewModel.toggleCurrentPassword("")
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
