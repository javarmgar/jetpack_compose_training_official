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

}
