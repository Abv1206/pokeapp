package com.albertbonet.pokeapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.albertbonet.pokeapp.model.Error
import com.albertbonet.pokeapp.model.PokemonsRepository
import com.albertbonet.pokeapp.model.database.Pokemons
import com.albertbonet.pokeapp.model.toError
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val pokemonsRepository: PokemonsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private val _events = Channel<UiEvent>()
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            pokemonsRepository.pokemons
                .catch { cause -> _state.update { it.copy(error = cause.toError()) } }
                .collect { pokemons -> _state.update { UiState(pokemons = pokemons) } }
        }
    }

    fun onUiReady() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            val error = pokemonsRepository.requestPokemons()
            _state.update { it.copy(loading = false, error = error) }
        }
    }

    fun onPokemonClicked(pokemonName: String) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            val error = pokemonsRepository.requestPokemon(pokemonName)
            _state.update { it.copy(loading = false, error = error) }
            if (error == null) {
                _events.send(UiEvent.NavigateTo(pokemonName))
            }
            /*requestPokemonCallback(pokemonName) {
                _state.value = _state.value.copy(loading = false)
                viewModelScope.launch {
                    _events.send(UiEvent.NavigateTo(pokemonName))
                }
            }*/

        }
    }

    private suspend fun requestPokemonCallback(pokemonName: String, callback: () -> Unit) {
        val error = pokemonsRepository.requestPokemon(pokemonName)
        _state.value = _state.value.copy(error = error)
        callback()
    }

    data class UiState(
        val loading: Boolean = false,
        val pokemons: List<Pokemons>? = null,
        val error: Error? = null
    )

    sealed interface UiEvent {
        data class NavigateTo(val pokemonName: String) : UiEvent
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val pokemonsRepository: PokemonsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(pokemonsRepository) as T
    }
}
