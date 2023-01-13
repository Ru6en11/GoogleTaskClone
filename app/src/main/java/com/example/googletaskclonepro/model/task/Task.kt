package com.example.googletaskclonepro.model.task

import android.os.Parcelable
import java.util.UUID

data class Task(
    val id: UUID = UUID.randomUUID(),
    var isCompleted: Boolean,
    var text: String,
    var additionalInfo: String,
    var isFavourite: Boolean
)