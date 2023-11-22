package com.example.composetraining

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.composetraining.ui.theme.ComposeTrainingTheme


/*
In a Compose application,
Location of hoist UI state:
    - depends on whether UI logic or business logic requires it.
    - Best practice:
        - hoist UI state to the lowest common ancestor between all the composables that read and write it
        - From the state owner,
            - expose to consumers immutable state
            - expose to consumers events to modify the state.
 */
class StateHoistingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTrainingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ParentScreen()
                }
            }
        }
    }
}

/*
UI TREE COMPOSITION STRUCTURE
    PARENT 1:
        CHILDREN 1
            GRAND CHILDREN 1
        CHILDREN 2
            GRAND CHILDREN 1
            GRAND CHILDREN 2
        CHILDREN 3
            GRAND CHILDREN 1
            GRAND CHILDREN 2
            GRAND CHILDREN 3
        CHILDREN 4
            GRAND CHILDREN 1
            GRAND CHILDREN 2
            GRAND CHILDREN 3
            GRAND CHILDREN 4
 */
@Composable
fun ParentScreen() {
    ChildOneScreen()
    ChildTwoScreen()
    ChildThreeScreen()
    ChildFourScreen()
}

/*
    CHILDREN 1:
        GRAND CHILDREN 1
 */

///////////GRAND CHILDREN - BEGINNING////////////
@Composable
fun ChildOneScreen() {
    GrandChildOneOneScreen()
}
///////////GRAND CHILDREN - END////////////
@Composable
fun GrandChildOneOneScreen() {
    Text(text = "GrandChildOneOneScreen")
}

/*
    CHILDREN 2:
        GRAND CHILDREN 1
        GRAND CHILDREN 2
 */
@Composable
fun ChildTwoScreen() {
    GrandChildTwoOneScreen()
    GrandChildTwoTwoScreen()

}
///////////GRAND CHILDREN - BEGINNING////////////

@Composable
fun GrandChildTwoOneScreen() {
    Text(text = "GrandChildTwoOneScreen")
}

@Composable
fun GrandChildTwoTwoScreen() {
    Text(text = "GrandChildTwoTwoScreen")
}
///////////GRAND CHILDREN - END////////////

/*
    CHILDREN 3:
        GRAND CHILDREN 1
        GRAND CHILDREN 2
        GRAND CHILDREN 3
 */
@Composable
fun ChildThreeScreen() {
    GrandChildThreeOneScreen()
    GrandChildThreeTwoScreen()
    GrandChildThreeThreeScreen()
}
///////////GRAND CHILDREN - BEGINNING////////////

@Composable
fun GrandChildThreeOneScreen() {
    Text(text = "GrandChildThreeOneScreen")
}

@Composable
fun GrandChildThreeTwoScreen() {
    Text(text = "GrandChildThreeTwoScreen")
}

@Composable
fun GrandChildThreeThreeScreen() {
    Text(text = "GrandChildThreeThreeScreen")
}
///////////GRAND CHILDREN - END////////////

/*
    CHILDREN 4:
        GRAND CHILDREN 1
        GRAND CHILDREN 2
        GRAND CHILDREN 3
        GRAND CHILDREN 4
 */
@Composable
fun ChildFourScreen() {
    GrandChildFourOneScreen()
    GrandChildFourTwoScreen()
    GrandChildFourThreeScreen()
    GrandChildFourFourScreen()
}
///////////GRAND CHILDREN - BEGINNING////////////

@Composable
fun GrandChildFourOneScreen() {
    Text(text = "GrandChildFourOneScreen")
}

@Composable
fun GrandChildFourTwoScreen() {
    Text(text = "GrandChildFourTwoScreen")
}

@Composable
fun GrandChildFourThreeScreen() {
    Text(text = "GrandChildFourThreeScreen")
}

@Composable
fun GrandChildFourFourScreen() {
    Text(text = "GrandChildFourFourScreen")
}
///////////GRAND CHILDREN - END////////////
