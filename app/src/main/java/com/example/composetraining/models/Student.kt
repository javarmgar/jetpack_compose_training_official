package com.example.composetraining.models

import android.os.Parcelable

//@Parcelized it is not working probably we need a remote dependency to install using gradle
data class Student(
    val firstName:String,
    val lastName:String,
    val age: Int,
    val degree:String,
) //:Parcelable same reason above
