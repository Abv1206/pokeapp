package com.albertbonet.pokeapp.ui.detail

import androidx.lifecycle.SavedStateHandle
import com.albertbonet.pokeapp.di.PokemonName
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class DetailViewModelModule {

    @Provides
    @ViewModelScoped
    @PokemonName
    fun providePokemonName(savedStateHandle: SavedStateHandle) =
        savedStateHandle.get<String>(DetailActivity.POKEMON) ?: ""
}