package com.example.foundation.views

import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    open fun onResult(result: Any?) {}

}