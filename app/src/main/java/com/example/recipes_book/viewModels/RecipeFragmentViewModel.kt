package com.example.recipes_book.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipes_book.models.State
import com.example.recipes_book.models.retrofit.Recipe
import com.example.recipes_book.models.retrofit.SearchRecipe
import com.example.recipes_book.repository.FavouritesRepository
import com.example.recipes_book.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

private const val SIMILAR_AMOUNT = 10

class RecipeFragmentViewModel(
    private val receiveId: Int,
    private val mainRepo: MainRepository,
    private val favouritesRepo: FavouritesRepository
): ViewModel() {

    private val _recipeLiveData = MutableLiveData<State<Recipe>>()
    val recipeLiveData: LiveData<State<Recipe>> = _recipeLiveData

    private val _similarRecipesLiveData = MutableLiveData<State<List<SearchRecipe>>>()
    val similarRecipesLiveData = _similarRecipesLiveData

    fun getSimilarRecipes(keyword: String) {
        var state: State<List<SearchRecipe>> = State.loading()

        _similarRecipesLiveData.value = state

        viewModelScope.launch(Dispatchers.IO) {

            try {

                val request = mainRepo.searchRecipe(keyword, SIMILAR_AMOUNT)

                if (request.isSuccessful) {

                    val response = request.body()?.results?.filter { it.id != receiveId }

                    state = if (response?.isNotEmpty() == true)
                        State.success(response)
                    else
                        State.empty()
                }

            }
            catch (e: Exception) {
                state = State.error(e.message)
            }

            _similarRecipesLiveData.postValue(state)

        }
    }

    fun isInFavourites(id: Int): Int {

        val ex = Executors.newFixedThreadPool(1)

        val future: Future<Int> = ex.submit(object : Callable<Int> {
            override fun call(): Int {
                return favouritesRepo.isInFavourites(id)
            }
        })

        ex.shutdown()

        return future.get()

    }

    fun getRecipeById() {

        var state: State<Recipe> = State.loading()

        _recipeLiveData.value = state

        viewModelScope.launch(Dispatchers.IO) {

            try {

                val request = mainRepo.getRecipeById(receiveId)

                if (request.isSuccessful) state = State.success(request.body())

            }

            catch (e: Exception) {
                state = State.error(e.message)
            }

            _recipeLiveData.postValue(state)

        }


    }

    fun addToFavourites(recipe: com.example.recipes_book.models.room.Recipe) {
        viewModelScope.launch {
            favouritesRepo.insert(recipe)
        }
    }

    fun deleteFromFavourites(recipe: com.example.recipes_book.models.room.Recipe) {
        viewModelScope.launch {
            favouritesRepo.delete(recipe)
        }
    }
}