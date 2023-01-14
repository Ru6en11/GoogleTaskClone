package com.example.googletaskclonepro.model.task

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Task(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var isCompleted: Boolean,
    var text: String,
    var additionalInfo: String,
    var isFavourite: Boolean
)