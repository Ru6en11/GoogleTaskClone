package com.example.googletaskclonepro

import android.app.Application
import com.example.foundation.BaseApplication
import com.example.foundation.model.Repository

class App() : Application(), BaseApplication {

    override val repositories: List<Repository> = mutableListOf()

}