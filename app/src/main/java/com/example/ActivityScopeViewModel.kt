package com.example

import androidx.lifecycle.ViewModel
import com.example.foundation.navigator.IntermediateNavigator
import com.example.foundation.navigator.Navigator
import com.example.foundation.uiactions.UiActions

const val ARG_SCREEN = "com.example.args_screen"

class ActivityScopeViewModel(
    val uiAction: UiActions,
    val navigator: IntermediateNavigator
) : ViewModel(), UiActions by uiAction, Navigator by navigator {

    override fun onCleared() {
        super.onCleared()
        navigator.clear()
    }
}