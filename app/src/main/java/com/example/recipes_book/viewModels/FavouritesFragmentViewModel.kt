package com.example.recipes_book.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipes_book.models.State
import com.example.recipes_book.models.room.Recipe
import com.example.recipes_book.repository.FavouritesRepository
import kotlinx.coroutines.launch

class FavouritesFragmentViewModel(
    private val favouritesRepository: FavouritesRepository): ViewModel() {

    private val _favouritesLiveData = MutableLiveData<State<List<Recipe>>>()
    val favouritesLiveData: LiveData<State<List<Recipe>>> = _favouritesLiveData

    fun loadFavourites() {

        var state: State<List<Recipe>> = State.loading()

        _favouritesLiveData.value = state

        viewModelScope.launch {

            state = try {
                val favourites = favouritesRepository.getAllFavourites()

                if (favourites.isNotEmpty()) State.success(favourites) else State.empty()
            } catch (e: Exception) {
                State.error(e.message)
            }

            _favouritesLiveData.postValue(state)

        }
    }

    fun addToFavourites(recipe: Recipe) {
        viewModelScope.launch {

            favouritesRepository.insert(recipe)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            favouritesRepository.deleteAll()
        }
    }

    fun deleteFromFavourites(recipe: Recipe) {
        viewModelScope.launch {
            favouritesRepository.delete(recipe)
        }
    }
}