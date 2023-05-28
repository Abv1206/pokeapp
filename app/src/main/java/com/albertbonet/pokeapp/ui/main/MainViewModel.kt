package com.albertbonet.pokeapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.albertbonet.pokeapp.model.Pokemon
import com.albertbonet.pokeapp.model.Pokemons
import com.albertbonet.pokeapp.model.PokemonsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel (
    private val pokemonsRepository: PokemonsRepository
): ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        refresh()
    }

    private fun refresh () {
        // Called on the first time, when the pokemons list is empty or null
        viewModelScope.launch {
            _state.value = UiState(loading = true)
            _state.value = UiState(pokemons = pokemonsRepository.findPokemons(0).results)
        }
    }

    fun onPokemonClicked(pokemonName: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, navigateTo = null)
            val pokemon = pokemonsRepository.getPokemon(pokemonName)
            _state.value = _state.value.copy(loading = false, navigateTo = pokemon)
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val pokemons: List<Pokemons>? = null,
        val navigateTo: Pokemon? = null
    )
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val pokemonsRepository: PokemonsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(pokemonsRepository) as T
    }
}
