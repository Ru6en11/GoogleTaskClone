package com.example.googletaskclonepro.database

import androidx.room.TypeConverter
import java.util.*

class TaskTypeConverters {

    @TypeConverter
    fun fromDate(date: Date?): Long? = date?.time

    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date? = millisSinceEpoch?.let { Date(it) }

    @TypeConverter
    fun toUUID(uuid: String?): UUID? = UUID.fromString(uuid)


    @TypeConverter
    fun fromUUID(uuid: UUID?): String? = uuid?.toString()

}