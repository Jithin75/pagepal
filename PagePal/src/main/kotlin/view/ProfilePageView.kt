package view

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.example.viewmodel.MainPageViewModel
import theme.darkblue
import theme.grey
import theme.lightbrown
import theme.whitevariation

@Composable
fun ProfilePageView(mainPageViewModel: MainPageViewModel) {
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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Name: ${mainPageViewModel.userModel.name}",
                            color = whitevariation,
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Email: ${mainPageViewModel.userModel.email}",
                            color = whitevariation,
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Date of Birth: ${mainPageViewModel.userModel.dateOfBirth}",
                            color = whitevariation,
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Settings Options
                    Button(
                        onClick = { /* Navigate to Change Password Screen */ },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = lightbrown,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Change Password")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { /* Navigate to Change Username Screen */ },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = lightbrown,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Change Username")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { /* Delete Account */ },
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
    }
}
