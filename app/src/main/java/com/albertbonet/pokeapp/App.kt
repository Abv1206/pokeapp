package com.albertbonet.pokeapp

import android.app.Application
import androidx.room.Room
import com.albertbonet.pokeapp.data.database.PokemonDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application()