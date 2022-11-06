package com.example.recipes_book.viewModels

import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.*
import com.example.recipes_book.data.retrofit.RetrofitInstance
import com.example.recipes_book.data.room.FavouriteDatabase
import com.example.recipes_book.models.room.Recipe
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val API_KEY = "fc95cd6874c34b8882b3338ae705b76b"
const val RECIPES_AMOUNT = 100

class MainFragmentViewModel(application: Application): AndroidViewModel(application) {

    private val recipesLiveData = MutableLiveData<List<Recipe>>()
    private val visibleRecipesLiveData = MutableLiveData<List<Recipe>>()

    private val api = RetrofitInstance.api

    private val dao by lazy {
        FavouriteDatabase.getInstance(application).getFavouritesDao()
    }

    val loadingLiveData = MutableLiveData<Boolean>()

    private var visibleRecipes = 10

    fun getRecipes() {
        loadingLiveData.value = true

        val recipes = arrayListOf<Recipe>()

        viewModelScope.launch(Dispatchers.Main) {

            val apiResult = api.getRandomRecipes(API_KEY, RECIPES_AMOUNT)
                .body()

            apiResult?.recipes?.forEach {

                if (dao.entryInFavourites(it.id) > 0)  {
                    recipes.add(it.toRecipe(true))
                }
                else {
                    recipes.add(it.toRecipe())
                }


            }



            recipesLiveData.value = recipes

            loadingLiveData.value = false

            visibleRecipesLiveData.value = recipes.take(visibleRecipes)

        }

    }

    fun extendVisibleRecipes() {

        if (visibleRecipes != RECIPES_AMOUNT) {

            visibleRecipes += 10
            visibleRecipesLiveData.value = recipesLiveData.value?.take(visibleRecipes)

        }

    }



    fun deleteFromFavourites(recipe: Recipe) {

        viewModelScope.launch {
            dao.delete(recipe)

        }

    }

    fun addToFavourites(recipe: Recipe) {

        viewModelScope.launch {
            dao.insert(recipe)
        }

    }

    fun searchRecipe(query: String) {
        loadingLiveData.value = true

        val recipes = arrayListOf<Recipe>()

        viewModelScope.launch(Dispatchers.Main) {

            val apiResult = api.searchRecipes(API_KEY, RECIPES_AMOUNT, query)
                .body()

            apiResult?.results?.forEach {

                if (dao.entryInFavourites(it.id) > 0)  {
                    recipes.add(it.toRecipe(true))
                }
                else {
                    recipes.add(it.toRecipe())
                }



            }

            recipesLiveData.value = recipes

            loadingLiveData.value = false

            visibleRecipesLiveData.value = recipes.take(visibleRecipes)

        }

    }

    fun getSeenRecipesLD(): LiveData<List<Recipe>> {
        return visibleRecipesLiveData
    }

    fun getVisibleRecipes(): Int {
        return visibleRecipes
    }


}