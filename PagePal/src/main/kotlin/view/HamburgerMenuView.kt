package view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import org.example.viewmodel.MainPageViewModel

@Composable
fun HamburgerMenuView(mainPageViewModel: MainPageViewModel) {
    Popup(
        alignment = Alignment.TopStart,
        onDismissRequest = {mainPageViewModel.onDismissHamburger()},
        content = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(360.dp)
                    .background(color = Color(0xFF404040))
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable(onClick = {/* add Profile functionality */})
                        .fillMaxWidth()
                        .height(48.dp)

                ) {
                    Text(
                        text = "PROFILE",
                        modifier = Modifier.weight(1f).padding(start = 4.dp)
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable(onClick = {/* add Settings functionality */})
                        .fillMaxWidth()
                        .height(48.dp)

                ) {
                    Text(
                        text = "SETTINGS",
                        modifier = Modifier.weight(1f).padding(start = 4.dp)
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable(onClick = {/* add Friends functionality */})
                        .fillMaxWidth()
                        .height(48.dp)

                ) {
                    Text(
                        text = "FRIENDS",
                        modifier = Modifier.weight(1f).padding(start = 4.dp)
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable(onClick = {/* add Recommendations functionality */})
                        .fillMaxWidth()
                        .height(48.dp)

                ) {
                    Text(
                        text = "RECOMMENDATIONS",
                        modifier = Modifier.weight(1f).padding(start = 4.dp)
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
                Button(
                    onClick = {/* add Sign out functionality */},
                    modifier = Modifier.padding(64.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red, contentColor = Color.White)

                ) {
                    Text("SIGN OUT")
                }
            }
        }
    )
}