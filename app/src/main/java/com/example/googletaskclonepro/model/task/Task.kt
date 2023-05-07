package com.example.googletaskclonepro.model.task

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity
data class Task(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var isCompleted: Boolean,
    var text: String,
    var additionalInfo: String,
    var isFavourite: Boolean,
    var time: Date = Date()
) : Parcelable