package com.example.composetraining.stateholder

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.MutableStateFlow

@Stable
class MyFirstHolderState(
    val name: MutableStateFlow<String>,
    val onFirstUserChange: () -> Unit = { name.value = "Rodrigo"},
    val onSecondUserChange: () -> Unit = { name.value = "javier"}
)