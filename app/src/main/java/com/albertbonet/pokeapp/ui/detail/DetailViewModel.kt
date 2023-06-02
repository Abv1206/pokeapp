package com.albertbonet.pokeapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.albertbonet.pokeapp.model.PokemonResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DetailViewModel (val pokemonResult: PokemonResult): ViewModel() {

    private val _state = MutableStateFlow(UiState(pokemonResult))
    val state: StateFlow<UiState> = _state.asStateFlow()

    class UiState(val pokemonResult: PokemonResult)
}

@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(private val pokemonResult: PokemonResult): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewModel(pokemonResult) as T
    }
}
