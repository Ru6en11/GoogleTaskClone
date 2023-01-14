package com.example.googletaskclonepro

import android.app.Application
import com.example.foundation.BaseApplication
import com.example.foundation.model.Repository
import com.example.googletaskclonepro.views.tasks.InDatabaseTaskRepository

class App() : Application(), BaseApplication {

    override val repositories: MutableList<Repository> = mutableListOf()

    override fun onCreate() {
        super.onCreate()
        InDatabaseTaskRepository.initial(this)

        repositories.add(InDatabaseTaskRepository.get())
    }

}