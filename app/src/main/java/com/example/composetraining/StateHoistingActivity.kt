package com.example.composetraining

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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

/*
Types of UI state and UI logic
- UI state:
    - Screen UI state
        - is what you need to display on the screen.
        - For example, a NewsUiState class contain the news articles and information needed to render the UI.
        - usually connected with other layers of the hierarchy because it contains app data.
    - UI element state
        - properties intrinsic to UI elements that influence how they are rendered.
        - A UI element
        - may be shown or hidden and
        - may have a certain font, font size, or font color.
        - In Android Views:
            - the View manages this state itself as it is inherently stateful,
            - exposing methods to modify or query its state.
            - An example of this are the get and set methods of the TextView class for its text.
        - In Jetpack Compose
            - the state is external to the composable,
            - you can even hoist it out:
                - out of the immediate vicinity of the composable into
                    - the calling composable function or
                    - a state holder.
                - An example of this is ScaffoldState for the Scaffold composable.


- Logic
    - Business logic:
        - It is the implementation of product requirements for app data.
            - For example,
                - bookmarking an article in a news reader app when the user taps the button.
                - This logic to save a bookmark to a file or database is usually placed in the domain or data layers.
                - The state holder usually delegates this logic to those layers by calling the methods they expose.
    - UI logic:
        - It is related to how to display UI state on the screen.
            - For example,
                - obtaining the right search bar hint when the user has selected a category,
                - scrolling to a particular item in a list, or
                - the navigation logic to a particular screen when the user clicks a button.
 */

/*
This ParentScreen is the UI screen - so it should host the Screen UI state
 */
@Composable
fun ParentScreen() {
    Column {
        ChildOneScreen()
        ChildTwoScreen()
        ChildThreeScreen()
        ChildFourScreen()
    }
}

/*
    CHILDREN 1:
        GRAND CHILDREN 1
 */
@Composable
fun ChildOneScreen() {
    GrandChildOneOneScreen(
        contentText = "click me to show text message"
    )
}


///////////GRAND CHILDREN - BEGINNING////////////
/*
UI logic - Composables as state owner

- Having UI logic and UI element state in composables is a good approach if the state and logic is simple.
- You can leave your state internal to a composable or hoist as required.

No state hoisting needed
    - Hoisting state isn't always required.
    - State can be kept internal in a composable when no other composable need to control it.
    In this snippet, there is a composable that expands and collapses on tap:
 */

@Composable
fun GrandChildOneOneScreen(contentText: String) {
    var showDetails by rememberSaveable{ mutableStateOf(false) }
    Text(text = "GrandChildOneOneScreen")
    Button(
        onClick = { showDetails = !showDetails }
    ){
        Text(text = contentText)
    }
    if(showDetails) {
        Text(text = "This a show/hidden message")
    }
}
///////////GRAND CHILDREN - END////////////

/*
    CHILDREN 2:
        GRAND CHILDREN 1
        GRAND CHILDREN 2
 */
@Composable
fun ChildTwoScreen() {
    var showDetails by rememberSaveable{ mutableStateOf(false) }
    val onShowDetails = { showDetails = !showDetails }
    Column {
        GrandChildTwoOneScreen(
            contentText = "click me to show text message",
            showDetails = showDetails,
            onShowDetails = onShowDetails
        )
        GrandChildTwoTwoScreen()
    }

}
///////////GRAND CHILDREN - BEGINNING////////////
/*
Hoisting within composables

    - If you need to share your UI element state with other composables and apply UI logic to it in different places,
    - you can hoist it higher in the UI hierarchy.
    - This makes your composables more reusable and easier to test.
 */
@Composable
fun GrandChildTwoOneScreen(
    contentText:String,
    showDetails: Boolean,
    onShowDetails: () -> Unit,
) {

    Text(text = "GrandChildTwoOneScreen")
    Button(
        onClick = onShowDetails
    ){
        Text(text = contentText)
    }
    if(showDetails) {
        Text(text = "This a show/hidden message")
    }


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
    Column {
        GrandChildThreeOneScreen()
        GrandChildThreeTwoScreen()
        GrandChildThreeThreeScreen()
    }
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
    Column {
        GrandChildFourOneScreen()
        GrandChildFourTwoScreen()
        GrandChildFourThreeScreen()
        GrandChildFourFourScreen()
    }
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
