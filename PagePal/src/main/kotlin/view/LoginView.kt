package view

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import theme.*
import viewmodel.LoginViewModel

@Composable
fun LoginView(loginViewModel: LoginViewModel) {

    if (loginViewModel.showDialog) {
        AlertDialog(
            onDismissRequest = { loginViewModel.toggleShowDialog() },
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
                    onClick = { loginViewModel.toggleShowDialog() }
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
                        .weight(1f)
                )

                OutlinedTextField(
                    value = loginViewModel.username,
                    onValueChange = { loginViewModel.usernameEntered(it) },
                    textStyle = TextStyle(color = whitevariation),
                    label = { Text("Username", color = lightbrown) },
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = lightbrown // Set the outline color when not focused
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = loginViewModel.password,
                    onValueChange = { loginViewModel.passwordEntered(it) },
                    label = { Text("Password", color = lightbrown) },
                    textStyle = TextStyle(color = whitevariation),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = lightbrown // Set the outline color when not focused
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            loginViewModel.loginUser()
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
                        onClick = {loginViewModel.switchSignUp()},
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