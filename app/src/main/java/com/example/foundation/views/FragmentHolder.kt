package com.example.foundation.views

import com.example.ActivityScopeViewModel

interface FragmentHolder {

    fun notifyScreenUpdates()

    fun getActivityScopeViewModel(): ActivityScopeViewModel

}