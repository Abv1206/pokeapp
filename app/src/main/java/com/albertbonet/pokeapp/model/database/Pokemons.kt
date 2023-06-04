package com.albertbonet.pokeapp.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Pokemons(
    @PrimaryKey
    val id: Int,
    val name: String,
    val url: String,
    val officialUrlImage: String
    //@ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    //val image: ByteArray
)