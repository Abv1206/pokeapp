package com.albertbonet.pokeapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.albertbonet.pokeapp.usecases.GetPokemonUseCase
import com.albertbonet.pokeapp.data.Error
import com.albertbonet.pokeapp.data.toError
import com.albertbonet.pokeapp.domain.Pokemon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel (
    private val pokemonName: String,
    private val getPokemonUseCase: GetPokemonUseCase
): ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun onUiReady() {
        viewModelScope.launch {
            getPokemonUseCase(pokemonName)
                .catch { cause -> _state.update { it.copy(error = cause.toError()) } }
                .collect { pokemon -> _state.value = UiState(pokemon = pokemon) }
        }
    }

    data class UiState(
        val pokemon: Pokemon? = null,
        val error: Error? = null
    )
}

@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(
    private val pokemonName: String,
    private val getPokemonUseCase: GetPokemonUseCase
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewModel(pokemonName, getPokemonUseCase) as T
    }
}
