package com.example.ComposeTraining

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ComposeTraining.ui.theme.ComposeTrainingTheme
import com.example.sideeffects.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTrainingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
            }
        }
    }

}

@Composable
fun App(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        ArtistCardColumn(modifier = modifier)
        ArtistCardRow(modifier = modifier)
        ArtistCardBox()
    }
}

@Composable
fun ArtistCardBox(artist: Artist = Artist(), modifier: Modifier = Modifier) {
    Box(modifier = modifier){
        Image(
            painter = painterResource(id = artist.image),
            contentDescription = artist.contentDescription
        )
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = "Check mark"
        )
    }
}

data class Artist(
    val image: Int = R.drawable.circle_photo,
    val contentDescription:String= "Artist Image"
)

@Composable
fun ArtistCardColumn(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            modifier = modifier,
            text = "Hello javier armenta"
        )
        Text(
            text = "Hello luis vazquez"
        )
    }
}

@Composable
fun ArtistCardRow(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxHeight(0.5f)
            .padding(vertical = 32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Hello javier armenta")
        Spacer(modifier = Modifier.padding(horizontal = 8.dp))
        Text(text = "Hello luis vazquez")
    }
}

@Preview(
    showBackground = true,
    widthDp = 720,
    heightDp = 1280
    )
@Composable
fun ArtistCardPreview() {
    ComposeTrainingTheme {
        App()
    }
}