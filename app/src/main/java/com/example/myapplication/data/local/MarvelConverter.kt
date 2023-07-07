package com.example.myapplication.data.local


import androidx.room.TypeConverter

class MarvelConverter {
    @TypeConverter
    fun fromPreference(preference: Preference): String {
        return preference.name
    }

    @TypeConverter
    fun toPreference(name: String): Preference {
        return enumValueOf(name)
    }
}
