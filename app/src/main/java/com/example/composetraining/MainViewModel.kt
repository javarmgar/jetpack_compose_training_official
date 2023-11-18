package com.example.composetraining

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainViewModel: ViewModel() {

    val size = 10
    val waitingTime = 1000L
    val list: List<String> = List(size = size){ "This is the item: $it " }
    val listFlow: Flow<String> = flow<String> {
        list.forEach {
            delay(waitingTime)
            emit(it)
        }
    }

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
