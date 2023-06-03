package com.albertbonet.pokeapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.albertbonet.pokeapp.model.PokemonResult
import com.albertbonet.pokeapp.model.PokemonsRepository
import com.albertbonet.pokeapp.model.database.Pokemon
import com.albertbonet.pokeapp.ui.main.MainViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailViewModel (private val pokemonName: String, private val pokemonsRepository: PokemonsRepository): ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            pokemonsRepository.getPokemon(pokemonName).collect() { pokemon ->
                _state.value = UiState(pokemon = pokemon)
            }
        }
    }

    class UiState(val pokemon: Pokemon? = null)
}

@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(private val pokemonName: String, private val pokemonsRepository: PokemonsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewModel(pokemonName, pokemonsRepository) as T
    }
}
