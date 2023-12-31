package com.example.composetraining

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.composetraining.stateholder.HolderState
import com.example.composetraining.stateholder.rememberHolderState
import com.example.composetraining.ui.theme.ComposeTrainingTheme
import com.example.composetraining.ui.theme.Elevations
import com.example.composetraining.ui.theme.LocalElevations
import com.example.composetraining.ui.theme.StandardElevations


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
            val elevations = if(isSystemInDarkTheme()){
                Elevations( card = 1.dp, default = 1.dp)
            } else {
                Elevations( card = 0.dp, default = 0.dp)
            }
/*
Locally scoped data with CompositionLocal
    CompositionLocal is a tool for passing data down through the Composition implicitly.
    - what a CompositionLocal is in more detail,
    - how to create your own CompositionLocal, and
    - know if a CompositionLocal is a good solution for your use case.
 */
            ComposeTrainingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    /*
                    Providing values to a CompositionLocal
                    The CompositionLocalProvider composable
                        - binds values to CompositionLocal instances for the given hierarchy.
                    - To provide a new value to a CompositionLocal,
                        - use the provides infix function that associates
                            - a CompositionLocal key to a value as follows


                     */
                    CompositionLocalProvider( LocalElevations provides elevations){
                        ParentScreen()
                    }
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
fun ParentScreen(
    screenState:StateHoistingViewModel = viewModel(),
) {
    var isNavGraphFlowVisible by rememberSaveable { mutableStateOf(true) }
    Column {

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.1f),
            onClick = {
                isNavGraphFlowVisible = !isNavGraphFlowVisible
            }
        ) {
            Text(text = "change UI")
        }
        if(isNavGraphFlowVisible){
            MyAppNavHost()
        }else{
            showUITreeScreen(screenState)
        }
    }
}

/*
Navigating with Compose

- The Navigation component provides support for Jetpack Compose applications.
- You can navigate between composables while taking advantage of
    - the Navigation component’s infrastructure and features.


NavController:
    -Tis the central API for the Navigation component.
    - It is stateful and
    - keeps track of the back stack of composables that make up the screens in your app
    - and the state of each screen.
    - You can create a NavController by using the rememberNavController() method in your composable:
    - You should create the NavController in
        - the place in your composable hierarchy where
        - all composables that need to reference it have access to it.
        - This follows the principles of state hoisting and
        - allows you to use the NavController and the state it provides via currentBackStackEntryAsState()
        - to be used as the source of truth for updating composables outside of your screens.
 */
@Composable
fun MyAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination:String = ROUTE_SCREEN_ONE
) {
/*
    1-2
    1-3-4
    1-3-5
 */

    /*
    Navigate to a destination:
        - The Navigation component provides a simple way of navigating to a destination.
        - This interface supports a range of contexts and UI frameworks.
            - For example, you can use the Navigation component with
                - Compose,
                - Views,
                - Fragments,
                - Activities, and even
                - custom UI frameworks.

        - Each NavHost you create has its own corresponding NavController. T
        - The NavController provides access to the NavHost's graph.
    Navigate
        - Regardless of which UI framework you use,
        - there is a single function you can use to navigate to a destination:
            - NavController.navigate().

        - There are many overloads available for navigate().
        - The overload you should choose corresponds to your exact context.
            - For example,
                - you should use one overload when navigating to a composable and
                - another when navigating to a view.

    Navigate to a composable
        - To navigate to a composable in the navigation graph,
            - use NavController.navigate(route).
            - With this overload, navigate() takes a single String argument.
            - This is the route. It serves as the key to a destination.
                - example: navController.navigate("friendslist")

            - To navigate using a route string,
                1 - you first need to create your NavGraph
                    - such that each destination is associated with a route.
                    - For composables, you do so with the composable() function.
                2 - Expose events from your composables
                    - When a composable function needs to navigate to a new screen,
                    - you shouldn't pass it a reference to the NavController so that it can call navigate() directly.
                    - According to Unidirectional Data Flow (UDF) principles,
                        - it should instead expose an event that the NavController handles.
                    - More directly put, your composable should have a parameter of type () -> Unit.
                    - When you add destinations to your NavHost with the composable() function,
                    - pass your composable a call to NavController.navigate().

     */
    val userId = "1234"
    val onNavigateToScreenTwo   = { navController.navigate("$ROUTE_SCREEN_TWO/$userId?age=17") }
    val onNavigateToScreenThree = { navController.navigate(ROUTE_SCREEN_THREE) }
    val onNavigateToScreenFour  = { navController.navigate(ROUTE_SCREEN_FOUR) }
    val onNavigateToScreenFive  = { navController.navigate(ROUTE_SCREEN_FIVE) }
    val onNavigateToNestedGraph  = { navController.navigate(ROUTE_NESTED_GRAPH) }

    val navGraphBuilder: NavGraphBuilder.() -> Unit = {
        composable(
            ROUTE_SCREEN_ONE,
        ){

            ScreenOne(
                onNavigateToScreenTwo,
                onNavigateToScreenThree,
            )
        }
        composable(
            "$ROUTE_SCREEN_TWO/{userId}?age={age}",
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("age") {
                    type = NavType.StringType
                    defaultValue = "18"
                }
            )
        ){
                backStackEntry ->
            ScreenTwo(
                backStackEntry.arguments?.getString("userId"),
                backStackEntry.arguments?.getString("age")
            )
        }
        composable(ROUTE_SCREEN_THREE){
            ScreenThree(
                onNavigateToScreenFour,
                onNavigateToScreenFive,
            )
        }
        composable(ROUTE_SCREEN_FOUR){ ScreenFour() }
        composable(ROUTE_SCREEN_FIVE){ ScreenFive(onNavigateToNestedGraph) }
        nestedGraph(
            navController = navController
        )
    }

    /*
    Creating a NavHost
        - Each NavController must be associated with a single NavHost composable.
        - The NavHost links
            - the NavController with
            - a navigation graph that specifies the composable destinations that you should be able to navigate between
        - As you navigate between composables,
            - the content of the NavHost is automatically recomposed.
        - Each composable destination in your navigation graph is associated with a route.
    - Route
        - is a String that defines the path to your composable.
        - You can think of it as an implicit deep link that leads to a specific destination.
        - Each destination should have a unique route.
    - Creating the NavHost
        - requires the NavController previously created via rememberNavController()
         - and the route of the starting destination of your graph.
         - NavHost creation uses the lambda syntax from the Navigation Kotlin DSL to
            - construct your navigation graph.
            - You can add to your navigation structure by using the composable() method.
            - This method requires that you provide a route and the composable that should be linked to the destination:
     */
    NavHost(
        navController = navController,
        startDestination = startDestination,
        builder = navGraphBuilder
    )

}

@Composable
fun showUITreeScreen(screenState: StateHoistingViewModel) {
    Column {
        /*
        Consuming the CompositionLocal
        CompositionLocal.current
            - returns the value provided by the nearest CompositionLocalProvider
            - that provides a value to that CompositionLocal
         */
        Text(text = "${LocalElevations.current.card}")
        ChildOneScreen()
        ChildTwoScreen()
        ChildThreeScreen()
        /*
        Property drilling
            -  refers to passing data through several nested children components to the location where they’re read.
            - A typical example is when
                - you inject the screen level state holder at the top level
                    - and pass down state and events to children composables.
                    - This might additionally generate an overload of composable functions signatures.

            - Even though exposing events as individual lambda parameters could overload the function signature,
            - it maximizes the visibility of what the composable function responsibilities are.
            - Property drilling is preferable over creating wrapper classes
                - to encapsulate state and events in one place because
                - this reduces the visibility of the composable responsibilities.
            - By not having wrapper classes you’re also
                - more likely to pass composables only the parameters they need, which is a best practice.
                - The same best practice applies if these events are navigation events
         */
        ChildFourScreen(
            showDetails = screenState.showDetails.collectAsStateWithLifecycle(),
            onShowDetails = screenState::onShowDetails,
        )
    }

}

/*
    CHILDREN 1:
        GRAND CHILDREN 1
 */
@Composable
fun ChildOneScreen() {
    CompositionLocalProvider( LocalElevations provides StandardElevations.XS){
        GrandChildOneOneScreen(
            contentText = "click me to show text message"
        )
    }
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
    Text(text = "${LocalElevations.current.card}")
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
    CompositionLocalProvider( LocalElevations provides StandardElevations.S){
        Column {
            GrandChildTwoOneScreen(
                contentText = "click me to show text message",
                showDetails = showDetails,
                onShowDetails = onShowDetails
            )
            GrandChildTwoTwoScreen(showDetails)
        }
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
    Text(text = "${LocalElevations.current.card}")
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
fun GrandChildTwoTwoScreen(showDetails: Boolean) {
    Text(text = "GrandChildTwoTwoScreen")
    if(showDetails){
        Text(
            text = "show details is a state also being shared here in GrandChildTwoTwoScreen"
        )
    }
}
///////////GRAND CHILDREN - END////////////

/*
    CHILDREN 3:
        GRAND CHILDREN 1
        GRAND CHILDREN 2
        GRAND CHILDREN 3
 */


/*
ViewModels as state owner
- The benefits of  ViewModels  make them suitable for:
    - providing access to the business logic
    - preparing the application data for presentation on the screen.

- When you hoist UI state in the ViewModel, you move it outside of the Composition.
    - ViewModels aren't stored as part of the Composition.
    - They're provided by the framework and
    - they're scoped to a ViewModelStoreOwner which can be
        - an Activity,
        - Fragment,
        - navigation graph, or d
        - destination of a navigation graph.
 */
@Composable
fun ChildThreeScreen(
    holderState:HolderState = rememberHolderState(),
    screenState:StateHoistingViewModel = viewModel(),
) {
    CompositionLocalProvider( LocalElevations provides StandardElevations.M){

        Column {
            GrandChildThreeOneScreen(
                contentText = "click me to show text message",
                showDetails = holderState.showDetails.collectAsStateWithLifecycle(),
                onShowDetails = holderState.onShowDetails,
            )
            GrandChildThreeTwoScreen(
                contentText = "click me to show text message",
                showDetails = screenState.showDetails.collectAsStateWithLifecycle(),
                onShowDetails = screenState::onShowDetails,
            )
            GrandChildThreeThreeScreen()
        }
    }
}
///////////GRAND CHILDREN - BEGINNING////////////
/*
Plain state holder class as state owner

- When a composable contains complex UI logic that involves one or multiple state fields of a UI element,
- then it should delegate that responsibility to state holders, like a plain state holder class.
- This makes the composable logic
    - more testable in isolation, and
    - reduces its complexity.
- This approach favors the separation of concerns principle:
    - the composable is in charge of emitting UI elements, and
    - the state holder contains
        - the UI logic and
        - UI element state.


 */
@Composable
fun GrandChildThreeOneScreen(
    contentText:String,
    showDetails: State<Boolean>,
    onShowDetails: () -> Unit,
) {
    Text(text = "GrandChildThreeOneScreen")
    Text(text = "${LocalElevations.current.card}")
    Button(
        onClick = onShowDetails
    ){
        Text(text = contentText)
    }
    if(showDetails.value) {
        Text(text = "This a show/hidden message using state holder class")
    }
}

/*
Business logic

- composables and plain state holders classes <===>  in charge of the [UI logic] and [UI element state],
- a screen level state holder(plain or VM)   <===> is in charge of the following tasks:
                                                    - access to the business logic
                                                    - [Screen UI state]

 */
@Composable
fun GrandChildThreeTwoScreen(
    contentText:String,
    showDetails: State<Boolean>,
    onShowDetails: () -> Unit,
) {
    Text(text = "GrandChildThreeTwoScreen")
    Button(
        onClick = onShowDetails
    ){
        Text(text = contentText)
    }
    if(showDetails.value) {
        Text(text = "This a show/hidden message using ViewModel class")
    }
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
fun ChildFourScreen(
    showDetails: State<Boolean>,
    onShowDetails: () -> Unit,
) {
    CompositionLocalProvider( LocalElevations provides StandardElevations.L){
        Card(elevation = CardDefaults.cardElevation(LocalElevations.current.card)) {
            Column {
                Text(text = "${LocalElevations.current.card}")
                GrandChildFourOneScreen(
                    showDetails = showDetails,
                    onShowDetails = onShowDetails,
                )
                GrandChildFourTwoScreen()
                GrandChildFourThreeScreen()
                GrandChildFourFourScreen()
            }
        }
    }
}
///////////GRAND CHILDREN - BEGINNING////////////

@Composable
fun GrandChildFourOneScreen(
    showDetails: State<Boolean>,
    onShowDetails: () -> Unit,
) {
    Text(text = "GrandChildFourOneScreen")
    Button(
        onClick = onShowDetails
    ){
        Text(text = "Click me to show more")
    }
    if(showDetails.value) {
        Text(text = "This a show/hidden message using ViewModel class")
    }
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
/*
    1-2
    1-3-4
    1-3-5
 */
@Composable
fun ScreenOne(
    onNavigateToScreenTwo: () -> Unit,
    onNavigateToScreenThree: () -> Unit,

) {
    Column {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Screen one"
        )
        Button(
            onClick = onNavigateToScreenTwo) {
            Text(text = "Navigate to screen two")
        }
        Button(
            onClick = onNavigateToScreenThree) {
            Text(text = "Navigate to screen three")
        }
    }

}
@Composable
fun ScreenTwo(userId: String?, age: String?) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Screen two with userId:$userId and age$age"
    )
}
@Composable
fun ScreenThree(
    onNavigateToScreenFour: () -> Unit,
    onNavigateToScreenFive: () -> Unit
) {
    Column {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Screen three"
        )
        Button(
            onClick = onNavigateToScreenFour) {
            Text(text = "Navigate to screen four")
        }
        Button(
            onClick = onNavigateToScreenFive) {
            Text(text = "Navigate to screen five")
        }
    }
}
@Composable
fun ScreenFour() {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Screen four"
    )
}
@Composable
fun ScreenFive(
    onNavigateToNestedGraph: () -> Unit
) {
    Column {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Screen five"
        )
        Button(onClick = onNavigateToNestedGraph) {
            Text(text = "Navigate to nested Graph")
        }
    }
}

/*
Nested Navigation

    - Destinations can be grouped into a nested graph to modularize a particular flow in your app’s UI.
        - An example of this could be a self-contained login flow.
    - The nested graph encapsulates its destinations.
        - As with the root graph,
            - a nested graph must have a destination identified as the start destination by its route.
            - This is the destination that is navigated to when you navigate to the route associated with the nested graph.
    - To add a nested graph to your NavHost, you can use the navigation extension function:
    - It is strongly recommended that
        - you split your navigation graph into multiple methods as the graph grows in size.
        - This also allows multiple modules to contribute their own navigation graphs.
        - By making the method an extension method on NavGraphBuilder,
        - you can use it alongside the prebuilt navigation, composable, and dialog extension methods:
 */

/*
nested graph
    n1 n2
    n1 n3
 */

fun NavGraphBuilder.nestedGraph(
    navController: NavController
){
    navigation(
        startDestination = ROUTE_NESTED_SCREEN_ONE,
        route = ROUTE_NESTED_GRAPH
    ){
        composable(ROUTE_NESTED_SCREEN_ONE){
            ScreenNestedOne(
                onNavigateNestedScreenTwo = { navController.navigate(ROUTE_NESTED_SCREEN_TWO) },
                onNavigateNestedScreenThree = { navController.navigate(ROUTE_NESTED_SCREEN_THREE) }
            )
        }
        composable(ROUTE_NESTED_SCREEN_TWO){ ScreenNestedTwo() }
        composable(ROUTE_NESTED_SCREEN_THREE){ ScreenNestedThree() }
    }
}

@Composable
fun ScreenNestedOne(
    onNavigateNestedScreenTwo: () -> Unit,
    onNavigateNestedScreenThree: () -> Unit,
) {
    Column {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Screen nested one"
        )
        Button(onClick = onNavigateNestedScreenTwo) {
            Text(text = "Navigate to nested screen two")
        }
        Button(onClick = onNavigateNestedScreenThree) {
            Text(text = "Navigate to nested screen two")
        }
    }
}

@Composable
fun ScreenNestedTwo() {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Screen nested two"
    )
}
@Composable
fun ScreenNestedThree() {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Screen  nested three"
    )
}


const val ROUTE_SCREEN_ONE = "ROUTE_SCREEN_ONE"
const val ROUTE_SCREEN_TWO = "ROUTE_SCREEN_TWO"
const val ROUTE_SCREEN_THREE = "ROUTE_SCREEN_THREE"
const val ROUTE_SCREEN_FOUR = "ROUTE_SCREEN_FOUR"
const val ROUTE_SCREEN_FIVE = "ROUTE_SCREEN_FIVE"

const val ROUTE_NESTED_GRAPH = "ROUTE_NESTED_GRAPH"

const val ROUTE_NESTED_SCREEN_ONE = "ROUTE_NESTED_SCREEN_ONE"
const val ROUTE_NESTED_SCREEN_TWO = "ROUTE_NESTED_SCREEN_TWO"
const val ROUTE_NESTED_SCREEN_THREE = "ROUTE_NESTED_SCREEN_THREE"