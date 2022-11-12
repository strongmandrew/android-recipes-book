package com.example.recipes_book.viewModels

import android.app.Application
import androidx.lifecycle.*
import com.example.recipes_book.data.retrofit.RetrofitInstance
import com.example.recipes_book.data.room.FavouriteDatabase
import com.example.recipes_book.models.room.Recipe
import com.example.recipes_book.repository.FavouritesRepository
import com.example.recipes_book.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainFragmentViewModel(
    private val mainRepository: MainRepository,
    private val favouritesRepository: FavouritesRepository): ViewModel() {

    private val _recipesLiveData = MutableLiveData<List<Recipe>>()
    val recipesLiveData: LiveData<List<Recipe>> = _recipesLiveData
    private val _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    fun getRecipes() {
        _loadingLiveData.value = true

        val recipes = arrayListOf<Recipe>()

        viewModelScope.launch(Dispatchers.Main) {

            val apiResult = mainRepository.getRandomRecipes()
                .body()

            apiResult?.recipes?.forEach {

                if (favouritesRepository.isInFavourites(it.toRecipe()) > 0)  {
                    recipes.add(it.toRecipe(true))
                }
                else {
                    recipes.add(it.toRecipe())
                }


            }



            _recipesLiveData.value = recipes

            _loadingLiveData.value = false

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
        _loadingLiveData.value = true

        val recipes = arrayListOf<Recipe>()

        viewModelScope.launch(Dispatchers.Main) {

            val apiResult = mainRepository.searchRecipe(query)
                .body()

            apiResult?.results?.forEach {

                if (favouritesRepository.isInFavourites(it.toRecipe()) > 0)  {
                    recipes.add(it.toRecipe(true))
                }
                else {
                    recipes.add(it.toRecipe())
                }



            }

            _recipesLiveData.value = recipes

            _loadingLiveData.value = false
        }

    }


}