package com.albertbonet.pokeapp.model.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    /**** TYPE CLASS ****/
    @TypeConverter
    fun fromTypeObjects(value: List<Pokemon.Type>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun fromSingularTypeObject(value: Pokemon.SingularType): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toTypeObjects(value: String): List<Pokemon.Type> {
        val type = object : TypeToken<List<Pokemon.Type>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun toSingularTypeObject(value: String): Pokemon.SingularType {
        return Gson().fromJson(value, Pokemon.SingularType::class.java)
    }

    /**** STAT CLASS ****/
    @TypeConverter
    fun fromStatObjects(value: List<Pokemon.Stat>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun fromSingularStatObject(value: Pokemon.SingularStat): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStatObjects(value: String): List<Pokemon.Stat> {
        val stat = object : TypeToken<List<Pokemon.Stat>>() {}.type
        return Gson().fromJson(value, stat)
    }

    @TypeConverter
    fun toSingularStatObject(value: String): Pokemon.SingularStat {
        return Gson().fromJson(value, Pokemon.SingularStat::class.java)
    }

    /**** SPRITE CLASS ****/
    @TypeConverter
    fun fromSpriteObject(value: Pokemon.Sprite): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toSpriteObject(value: String): Pokemon.Sprite {
        return Gson().fromJson(value, Pokemon.Sprite::class.java)
    }
}