package com.example.composetraining

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class StateHoistingViewModel: ViewModel() {

    //Screen UI state
    /*
    - screen UI state is produced by applying business rules.
    - Given that the screen level state holder is responsible for it,
    - this means the screen UI state is typically hoisted in the screen level state holder, in this case a ViewModel.
     */
    private var _showDetails: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var showDetails: StateFlow<Boolean> = _showDetails.asStateFlow()
    //UI Logic
    fun  onShowDetails(){
        _showDetails.value = !_showDetails.value
    }


    //UI element state
    /*
    You can hoist UI element state to the screen level state holder if there is business logic that needs to read or write it.
     */

    //Caveat
    /*
    - For some Compose UI element state,
    - hoisting to the ViewModel might require special considerations.
    - For example,
    - some state holders of Compose UI elements expose methods to modify the state.
    - Some of them might be suspend functions that trigger animations.
    - These suspend functions can throw exceptions if you call them from a CoroutineScope that is not scoped to the Composition.
    - viewModelScope from Compose UI causes a runtime exception of type IllegalStateException with a message reading “a MonotonicFrameClock is not available in this CoroutineContext”.
    - To fix this, use a CoroutineScope scoped to the Composition.
    - It provides a MonotonicFrameClock in the CoroutineContext that is necessary for the suspend functions to work.
    - To fix this crash, switch the CoroutineContext of the coroutine in the ViewModel
    - to one that is scoped to the Composition. It could look like this:
    val drawerState = DrawerState(initialValue = DrawerValue.Closed)

    private val _drawerContent = MutableStateFlow(DrawerContent.Empty)
    val drawerContent: StateFlow<DrawerContent> = _drawerContent.asStateFlow()

    fun closeDrawer(uiScope: CoroutineScope) {
        viewModelScope.launch {
            withContext(uiScope.coroutineContext) { // Use instead of the default context
                drawerState.close()
            }
            // Fetch drawer content and update state
            _drawerContent.update { content }
        }
    }
     */
}
