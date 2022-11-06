package com.example.recipes_book.viewModels

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipes_book.data.retrofit.RetrofitInstance
import com.example.recipes_book.data.room.FavouriteDatabase
import com.example.recipes_book.models.retrofit.SearchResult
import com.example.recipes_book.models.room.Recipe
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val API_KEY = "70254bc4318e40dcb6344aad63e456ec"
const val RECIPES_AMOUNT = 100

class MainFragmentViewModel: ViewModel() {

    private val recipesLiveData = MutableLiveData<List<Recipe>>()
    private val visibleRecipesLiveData = MutableLiveData<List<Recipe>>()

    private var visibleRecipes = 5

    fun getRecipes() {

        val recipes = arrayListOf<Recipe>()

        viewModelScope.launch(Dispatchers.Main) {

            val apiResult = RetrofitInstance.api.getRandomRecipes(API_KEY, RECIPES_AMOUNT)
                .body()

            apiResult!!.recipes.forEach {
                recipes.add(it.toRecipe())
            }

            recipesLiveData.value = recipes

            visibleRecipesLiveData.value = recipes.take(visibleRecipes)

        }

    }

    fun extendVisibleRecipes() {

        if (visibleRecipes != RECIPES_AMOUNT) {

            visibleRecipes += 5
            visibleRecipesLiveData.value = recipesLiveData.value?.take(visibleRecipes)

        }

    }



    private fun deleteFromFavourites(rootView: View, context: Context, recipe: Recipe) {

        viewModelScope.launch {
            FavouriteDatabase.getInstance(context).getFavouritesDao().delete(recipe)
            Snackbar
                .make(rootView,
                    "${recipe.title} deleted from favourites!",
                    Snackbar.LENGTH_SHORT)
                .setAction("Undo") {
                    addToFavourites(rootView, context, recipe)
                }
                .show()

        }

    }

    private fun addToFavourites(rootView: View, context: Context, recipe: Recipe) {
        viewModelScope.launch {
            FavouriteDatabase.getInstance(context).getFavouritesDao().insert(recipe)
            Snackbar
                .make(rootView,
                    "${recipe.title} added to favourites!",
                    Snackbar.LENGTH_SHORT)
                .setAction("Undo") {
                    deleteFromFavourites(rootView, context, recipe)
                }
                .show()


        }

    }

    fun changeFavouritesClick(rootView: View, context: Context, recipe: Recipe) {

        if (recipe.isFavourite) {
             addToFavourites(rootView,context, recipe)
        } else {
            deleteFromFavourites(rootView,context, recipe)
        }

    }

    fun searchRecipe(query: String) {

        val recipes = arrayListOf<Recipe>()

        viewModelScope.launch(Dispatchers.Main) {

            val apiResult = RetrofitInstance.api.searchRecipes(API_KEY, RECIPES_AMOUNT, query)
                .body()

            apiResult!!.results.forEach {
                recipes.add(it.toRecipe())

            }

            recipesLiveData.value = recipes
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