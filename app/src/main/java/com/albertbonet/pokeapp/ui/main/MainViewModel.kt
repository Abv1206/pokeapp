package com.albertbonet.pokeapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.albertbonet.pokeapp.model.Pokemon
import com.albertbonet.pokeapp.model.Pokemons
import com.albertbonet.pokeapp.model.PokemonsRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val pokemonsRepository: PokemonsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private val _events = Channel<UiEvent>()
    val events = _events.receiveAsFlow()

    init {
        refresh()
    }

    private fun refresh() {
        // Called on the first time, when the pokemons list is empty or null
        viewModelScope.launch {
            _state.value = UiState(loading = true)
            _state.value = UiState(pokemons = pokemonsRepository.findPokemons(0).results)
        }
    }

    fun onPokemonClicked(pokemonName: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            val pokemon = pokemonsRepository.getPokemon(pokemonName)
            _state.value = _state.value.copy(loading = false)
            _events.send(UiEvent.NavigateTo(pokemon))
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val pokemons: List<Pokemons>? = null,
    )

    sealed interface UiEvent {
        data class NavigateTo(val pokemon: Pokemon) : UiEvent
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val pokemonsRepository: PokemonsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(pokemonsRepository) as T
    }
}
