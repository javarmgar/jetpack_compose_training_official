package com.example.composetraining

import android.os.Bundle
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.composetraining.models.Student
import com.example.composetraining.stateholder.MyFirstHolderState
import com.example.composetraining.ui.theme.ComposeTrainingTheme
import kotlinx.coroutines.flow.MutableStateFlow

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
fun App(
    modifier: Modifier = Modifier,
    mainViewModel:MainViewModel = viewModel(),
) {
    val uiItemState by mainViewModel.listFlow.collectAsStateWithLifecycle(initialValue = "Nothing collected")
    LazyColumn(modifier = modifier.fillMaxWidth()) {

        item {
            CounterScreen(
                mainViewModel.counterLDState.observeAsState().value?:0,
                mainViewModel::increaseCounterByOne,
            )
        }
        item {
            CounterMultiplesScreen(
                mainViewModel.counterMultipleLD.observeAsState().value?:0,
            )
        }
        item { PlainStateHolderScreen() }
        item { ItemsScreen(uiItemState) }
        item { HelloComposableStateFull() }
        item { ArtistCardColumn(modifier = modifier) }
        item { ArtistCardRow(modifier = modifier) }
        item { ArtistCardBox() }
        item { WithConstraintsComposable() }
        item { ArtistCard2(artist = Artist()) }
        item { OffsetCard() }
        item { WeightedCard() }
        item { CityScreen() }
        item { StudentScreen() }

    }
}



/*
MapSaver
    - If for some reason @Parcelize is not suitable,
    - you can use mapSaver to define your own rule
        - for converting an object into a set of values that the system can save to the Bundle.
 */
val studentSaver = run{
    val firstNameKey = "firstName"
    val lastNameKey = "lastName"
    val ageKey = "age"
    val degreeKey = "degree"
    mapSaver(
        save = { mapOf(
            firstNameKey to it.firstName,
            lastNameKey to it.lastName,
            ageKey to it.age,
            degreeKey to it.degree,
        ) },
        restore = {
            Student(
                firstName = it[firstNameKey] as String,
                lastName = it[lastNameKey] as String,
                age = it[ageKey] as Int,
                degree = it[degreeKey] as String,
            )
        }
    )
}
@Composable
fun StudentScreen() {
    val student by rememberSaveable(stateSaver = studentSaver) {
        mutableStateOf(
            Student(
                firstName = "javier",
                lastName = "Armenta",
                age = 29,
                degree = "Computer Science"
            )
        )
    }

    var isFailingSubject by remember {
        mutableStateOf(true)
    }
    val onFailingSubjectChange = { isFailingSubject = !isFailingSubject}

    val studentStatusState by remember(key1= isFailingSubject ){
        mutableStateOf(if(isFailingSubject) "Should recompose" else "Should not recompose")
    }
    Column {
        StudentContent(
            student = student,
            onFailingSubjectChange = onFailingSubjectChange
        )
        StudentInfoContent(studentStatusState)
    }
}

@Composable
fun StudentContent(
    student: Student,
    onFailingSubjectChange: () -> Unit
) {
    Column {
        student.apply {
            Text(text = this.firstName)
            Text(text = this.lastName)
            Text(text = this.age.toString())
            Text(text = this.degree)
        }
        Button(onClick = onFailingSubjectChange) {
            Text(text = "Set Failing subject")
        }
    }
}

@Composable
fun StudentInfoContent(studentStatusState: String) {
    Column {
        Text(text = studentStatusState)
    }
}

@Composable
private fun rememberMyFirstHolderState(name:String): MyFirstHolderState {
    return remember (name){
        MyFirstHolderState(MutableStateFlow(name))
    }
}
@Composable
fun PlainStateHolderScreen() {
    val uiState: MyFirstHolderState = rememberMyFirstHolderState(name = "default")
    PlainStateHolderContent(
        uiState = uiState.name.collectAsStateWithLifecycle(),
        onFirstUserChange = uiState.onFirstUserChange,
        onSecondUserChange = uiState.onSecondUserChange,
    )

}
@Composable
fun PlainStateHolderContent(
    uiState: State<String>,
    onFirstUserChange: () -> Unit,
    onSecondUserChange: () -> Unit
) {
    Column {
        Text(text = "The is state is ${uiState.value}")
        Row {
            OutlinedButton(onClick = onFirstUserChange) {
                Text(text = "First change")
            }
            OutlinedButton(onClick = onSecondUserChange) {
                Text(text = "Second change")
            }
        }
    }
}

@Composable
fun CounterMultiplesScreen(counterMultipleState: Int) {
    CounterMultiplesContent(counterMultipleState)
}

@Composable
fun CounterMultiplesContent(counterMultipleState: Int) {
    Text(text = "The multiple by 10 counter is:$counterMultipleState")
}

@Composable
fun CounterScreen(
    counterLDState: Int,
    increaseCounterByOne: () -> Unit,
) {
    CounterContent(
        counter = counterLDState,
        onUpdateCounter = increaseCounterByOne,
    )
}

@Composable
fun CounterContent(
    counter: Int,
    onUpdateCounter: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Text(text = "The counter is:$counter")
        OutlinedButton(onClick = onUpdateCounter) {
            Text("change the count")
        }
    }

}

@Composable
fun ItemsScreen(uiState: String) {
    //1 state
    var itemSelectedState by rememberSaveable {
        mutableStateOf("")
    }
    val onTappedItem: (String) -> Unit  = { stateSelected -> itemSelectedState = stateSelected.uppercase()}
    //2 events
    //3 call site
    ItemsContent(uiState, onTappedItem)
    SelectedComponent(itemSelectedState)
}

@Composable
fun SelectedComponent(itemSelectedState: String) {
    Text(text = "The item selected is $itemSelectedState")
}

@Composable
fun ItemsContent(item: String, onTappedItem: (String) -> Unit) {
    Column {
        Text(text = "Item: $item")
        Button(onClick = {
            onTappedItem(item)
        }) {
            Text(text = "Select item")
        }

    }
}

//@Parcelized
//data class City(val country: String, val city: String):Parcelable
data class City(val country: String, val city: String)
val CitySaver = run {
    val nameKey = "Name"
    val countryKey = "Country"
    mapSaver(
        save = { mapOf(nameKey to it.city, countryKey to it.country) },
        restore = { City(it[nameKey] as String, it[countryKey] as String) }
    )
}
@Composable
fun CityScreen() {
    val city by rememberSaveable(stateSaver = CitySaver ) {
        mutableStateOf(City( country = "mexico", city = "Mexico city"))
    }
    CityContent(city)
}

@Composable
fun CityContent(city: City) {
    Column {
        Text(text = "Country: ${city.country} ")
        Text(text = "City: ${city.city}")
    }
}


/*
Stateful versus stateless
    - stateful:A composable that uses remember to store an object creates internal state
        - it holds and modifies its state internally.
        - This can be useful in situations where a caller doesn't need to:
            - control the state and
            - can use it without having to manage the state themselves.
            However,
        - composables with internal state tend to be less reusable and harder to test.
    - stateless: It's a composable that doesn't hold any state.
        - An easy way to achieve stateless is by using state hoisting.


    reusable composables
    - you often want to expose both a stateful and a stateless version of the same composable.
    - The stateful version is convenient for callers that don't care about the state,
    - The stateless version is necessary for callers that need to control or hoist the state.

 */
@Composable
fun HelloComposableStateFull() {
    var name by rememberSaveable{mutableStateOf("")}
    val onNameChanged: (String) -> Unit = { name = it }
    HelloComposableStateLess(name, onNameChanged)
}

/*
State hoisting

    - Pattern of moving state to a composable's caller to make a composable stateless.
    - The general pattern for state hoisting in Jetpack Compose is to replace the state variable with two parameters:
        - value: T: the current value to display
        - onValueChange: (T) -> Unit: an event that requests the value to change, where T is the proposed new value

 */

/*
Hoisted State properties:
    - Single source of truth:
        - By moving state instead of duplicating it,
        - we're ensuring there's only one source of truth.
        - This helps avoid bugs.
    - Encapsulated:
        - Only stateful composables can modify their state.
        - It's completely internal.
    - Shareable:
        - Hoisted state can be shared with multiple composables.
        - If you wanted to read state in a different composable, hoisting would allow you to do that.
    - Interceptable:
        - callers to the stateless composables can decide to ignore or modify events before changing the state.
    - Decoupled:
        - the state for the stateless Composable may be stored anywhere.
        - For example,
            - it's now possible to move state into a ViewModel.

 */

/*
UDF = unidirectional data flow
    - The pattern where
        - the state goes down, and
        - events go up is called
    - By following unidirectional data flow,
        - you can decouple
            - composables that display state in the UI
            from
            - the parts of your app that store and change state.


 */

/*
Rules for hoisting state

    1.- State should be hoisted to at least the lowest common parent of all composables that use the state (read).
    2.- State should be hoisted to at least the highest level it may be changed (write).
    3.- If two states change in response to the same events they should be hoisted together.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelloComposableStateLess(name: String, onNameChanged: (String) -> Unit) {

    Column(modifier = Modifier.padding(16.dp)) {
        if(name.isNotEmpty()){
            Text(
                text = "hello $name",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        OutlinedTextField(
            value = name,
            onValueChange = onNameChanged,
            label = { Text(text = "name") }
        )
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
        )

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
