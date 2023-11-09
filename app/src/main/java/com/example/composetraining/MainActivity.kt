package com.example.composetraining

import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composetraining.ui.theme.ComposeTrainingTheme

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
    Column(modifier = modifier.fillMaxWidth()) {
        ArtistCardColumn(modifier = modifier)
        ArtistCardRow(modifier = modifier)
        ArtistCardBox()
        WithConstraintsComposable()
        ArtistCard2(artist = Artist())
        OffsetCard()
        WeightedCard()
    }
}

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
fun ArtistCardBox(artist: Artist = Artist(), modifier: Modifier = Modifier) {
    val padding = 16.dp
    Box(
         modifier = modifier
             .clickable {}
             .padding(padding)
    ){
        Image(
            painter = painterResource(id = artist.image),
            contentDescription = artist.contentDescription,
        )
    }
}

data class Artist(
    val image: Int = R.drawable.circle_photo,
    val contentDescription:String= "Artist Image"
)

@Composable
fun ArtistCardRow(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .padding(vertical = 32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Hello javier armenta")
        Spacer(modifier = Modifier.padding(horizontal = 8.dp))
        Text(text = "Hello luis vazquez")
    }
}

@Composable
fun WithConstraintsComposable(){
    BoxWithConstraints {
        Text("My minHeight is $minHeight while my maxWidth is $maxWidth")
    }
}

@Composable
fun ArtistCard2(artist: Artist) {
    Box(
        modifier = Modifier
            .size(width = 500.dp, height = 100.dp)

    ){
        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Gray)
        )00

        Row {

            Column {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.3f),
                    text = "im a text"
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .paddingFromBaseline(10.dp),
                    text = "with baseline"
                )
            }
            Image(
                modifier = Modifier.requiredSize(100.dp),
                painter = painterResource(id = artist.image),
                contentDescription = artist.contentDescription,
                contentScale = ContentScale.Fit
            )
        }

    }
}

@Composable
fun OffsetCard( modifier: Modifier = Modifier) {
    Icon(
        modifier = modifier
            .size(100.dp)
            .offset(-20.dp),
        imageVector = Icons.Filled.ArrowForward,
        contentDescription = "Check mark"
    )
}

@Composable
fun WeightedCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Icon(
            modifier = Modifier
                .background(Color.Gray)
                .weight(1f),
            imageVector = Icons.Filled.ArrowForward,
            contentDescription = "Check mark"
        )
        Icon(
            modifier = Modifier
                .background(Color.Gray)
                .weight(1f),
            imageVector = Icons.Filled.ArrowForward,
            contentDescription = "Check mark"
        )

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
