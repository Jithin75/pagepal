package view

import LoginViewState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import org.example.viewmodel.MainPageViewModel
import theme.grey
import theme.lightbrown
import theme.whitevariation
import viewmodel.ProfilePageViewModel

@Composable
fun HamburgerMenuView(mainPageViewModel: MainPageViewModel, setCurrentState: (LoginViewState) -> Unit) {
    Popup(
        alignment = Alignment.TopStart,
        onDismissRequest = {mainPageViewModel.onDismissHamburger()},
        properties = PopupProperties(focusable = true),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(360.dp)
                    .background(color = grey),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    IconButton(
                        onClick = {mainPageViewModel.onDismissHamburger()},
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(horizontal = 4.dp)
                    ) {
                        Icon(
                            Icons.Filled.Menu,
                            contentDescription = "menu",
                            tint = lightbrown
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable(onClick = {mainPageViewModel.toggleProfilePage()})
                        .fillMaxWidth()
                        .height(48.dp)

                ) {
                    Text(
                        text = "PROFILE",
                        modifier = Modifier.weight(1f).padding(start = 16.dp),
                        color = whitevariation
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 16.dp),
                        tint = whitevariation
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable(onClick = {mainPageViewModel.toggleRecommendPage()})
                        .fillMaxWidth()
                        .height(48.dp)

                ) {
                    Text(
                        text = "RECOMMENDATIONS",
                        modifier = Modifier
                            .weight(1f).padding(start = 16.dp),
                        color = whitevariation
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 16.dp),
                        tint = whitevariation
                    )
                }
                Button(
                    onClick = {setCurrentState(LoginViewState(null, "login"))},
                    modifier = Modifier.padding(64.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red, contentColor = Color.White)

                ) {
                    Text("SIGN OUT")
                }
            }
            if (mainPageViewModel.isProfileOpen) {
                ProfilePageView(mainPageViewModel, ProfilePageViewModel(mainPageViewModel.userModel), setCurrentState)
            }
            if (mainPageViewModel.isRecommendOpen) {
                RecommendationView(mainPageViewModel)
            }
        }
    )
}