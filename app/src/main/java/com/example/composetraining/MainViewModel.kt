package com.example.composetraining

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainViewModel: ViewModel() {

    /*
    Other supported types of state
    - Compose doesn't require that you use MutableState<T> to hold state;
      it supports other observable types.
    - Before reading another observable type in Compose, you must convert it to a State<T>
      so that composables can automatically recompose when the state changes

     */

    /*
    Flow: collectAsStateWithLifecycle()
    - Collects values from a Flow in a lifecycle-aware manner, allowing your app to save unneeded app resources.
    - It represents the latest emitted value via Compose State.
    - Use this API as the recommended way to collect flows on Android apps.

    *** collectAsState()
        -Use collectAsState for platform-agnostic code instead of collectAsStateWithLifecycle, which is Android-only.
     */
    val size = 10
    val waitingTime = 1000L
    val list: List<String> = List(size = size){ "This is the item: $it " }
    val listFlow: Flow<String> = flow {
        list.forEach {
            delay(waitingTime)
            emit(it)
        }
    }

    /*
    LiveData: observeAsState()
    - observeAsState() starts observing this LiveData and represents its values via State.
     */
    private val _counterLD = MutableLiveData(0)
    val counterLDState:LiveData<Int> = _counterLD

    private val _counterMultipleLD = MutableLiveData(0)
    val counterMultipleLD:LiveData<Int> = _counterMultipleLD

    fun increaseCounterByOne(){
        _counterLD.value?.run {
            _counterLD.value = this + 1
            if(this % 10 == 0){
                increaseMultipleCounterByOne()
            }
        }
    }

    fun increaseMultipleCounterByOne(){
        _counterMultipleLD.value?.run {
            _counterMultipleLD.value = this + 1
        }
    }

}
