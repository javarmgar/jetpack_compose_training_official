package com.example.composetraining.stateholder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/*
This class is annotated as Stable.
- incrementing a composable's responsibilities increases the need for a state holder.
- The responsibilities could be in UI logic, or just in the amount of state to keep track of.

- Another common pattern is using a plain state holder class to handle the complexity of root composable functions in the app.
- You can use such a class to
    - encapsulate app-level state like navigation state and screen sizing.
    - A complete description of this can be found in the UI logic and its state holder page.
        - https://developer.android.com/topic/architecture/ui-layer/stateholders#ui-logic


 */
@Stable
class HolderState() {
    //UI State
    var _showDetails: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var showDetails: StateFlow<Boolean> = _showDetails.asStateFlow()
    //UI Logic
    val onShowDetails: () -> Unit = { _showDetails.value = !_showDetails.value }
}

@Composable
fun rememberHolderState():HolderState = remember {
    HolderState()
}
