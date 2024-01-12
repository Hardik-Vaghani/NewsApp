package com.hardik.bottomenavigation.db

import androidx.room.TypeConverter
import com.hardik.bottomenavigation.models.Source

class Converters {
    @TypeConverter
    fun fromSource(source: Source) : String{
        return source.name
    }

    @TypeConverter
    fun toSource(name:String) : Source {
        return Source(name,name)
    }
}