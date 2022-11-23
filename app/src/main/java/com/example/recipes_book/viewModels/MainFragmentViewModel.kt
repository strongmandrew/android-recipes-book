package com.example.recipes_book.viewModels

import android.app.Application
import androidx.lifecycle.*
import com.example.recipes_book.data.retrofit.RetrofitInstance
import com.example.recipes_book.data.room.FavouriteDatabase
import com.example.recipes_book.models.State
import com.example.recipes_book.models.retrofit.SearchRecipe
import com.example.recipes_book.models.retrofit.SearchResult
import com.example.recipes_book.models.room.Recipe
import com.example.recipes_book.repository.FavouritesRepository
import com.example.recipes_book.repository.MainRepository
import kotlinx.coroutines.*
import java.lang.Exception
import java.net.SocketTimeoutException

class MainFragmentViewModel(
    private val mainRepository: MainRepository,
    private val favouritesRepository: FavouritesRepository): ViewModel() {

    private val _recipesLiveData = MutableLiveData<State<List<Recipe>>>()
    val recipesLiveData: LiveData<State<List<Recipe>>> = _recipesLiveData


    fun getRecipes() {

        var state: State<List<Recipe>>? = State.loading()

        _recipesLiveData.value = state

        viewModelScope.launch(Dispatchers.IO) {

            try {

                val request = mainRepository.getRandomRecipes()

                val recipes = arrayListOf<Recipe>()

                request.body()?.recipes?.forEach { recipe ->

                    if (favouritesRepository.isInFavourites(recipe.toRecipe()) > 0) {
                        recipes.add(recipe.toRecipe(true))
                    }
                    else {
                        recipes.add(recipe.toRecipe(false))
                    }
                }

                state = if (recipes.isEmpty()) State.empty() else State.success(recipes)

            }
            catch (e: Exception) {
                state = State.error(e.message)
            }

            _recipesLiveData.postValue(state)

        }

    }

    fun deleteFromFavourites(recipe: Recipe) {

        viewModelScope.launch {
            favouritesRepository.delete(recipe)

        }

    }

    fun addToFavourites(recipe: Recipe) {

        viewModelScope.launch {
            favouritesRepository.insert(recipe)
        }

    }

    fun searchRecipe(query: String) {


        var state: State<List<Recipe>>? = State.loading()

        _recipesLiveData.value = state

        viewModelScope.launch(Dispatchers.IO) {

            try {

                val request = mainRepository.searchRecipe(query)

                val recipes = arrayListOf<Recipe>()

                request.body()?.results?.forEach { searchRecipe ->

                    if (favouritesRepository.isInFavourites(searchRecipe.toRecipe()) > 0) {
                        recipes.add(searchRecipe.toRecipe(true))
                    }
                    else {
                        recipes.add(searchRecipe.toRecipe(false))
                    }
                }

                state = if (recipes.isEmpty()) State.empty() else State.success(recipes)

            }
            catch (e: Exception) {
                state = State.error(e.message)
            }

            _recipesLiveData.postValue(state)

    }

    }


}