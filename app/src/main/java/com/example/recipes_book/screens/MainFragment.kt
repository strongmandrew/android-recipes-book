package com.example.recipes_book.screens

import BounceEdgeEffectFactory
import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.recipes_book.R
import com.example.recipes_book.adapters.RecipeAdapter
import com.example.recipes_book.databinding.FragmentMainBinding
import com.example.recipes_book.models.room.Recipe
import com.example.recipes_book.viewModels.MainFragmentViewModel
import com.google.android.material.snackbar.Snackbar
import java.lang.RuntimeException

class MainFragment : Fragment() {

    private lateinit var mainFragmentViewModel: MainFragmentViewModel
    private var _binding : FragmentMainBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("FragmentMainBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainFragmentViewModel = ViewModelProvider(this@MainFragment)[MainFragmentViewModel::class.java]

        val mainAdapter = RecipeAdapter(object: RecipeAdapter.FavouritesClickListener {
            override fun onAddClick(recipe: Recipe) {
                mainFragmentViewModel.addToFavourites(recipe)
                Snackbar
                    .make(view,
                        "${recipe.title} added to favourites!",
                        Snackbar.LENGTH_SHORT)
                    .show()
            }

            override fun onDeleteClick(recipe: Recipe) {
                mainFragmentViewModel.deleteFromFavourites(recipe)
                Snackbar
                    .make(view,
                        "${recipe.title} deleted from favourites!",
                        Snackbar.LENGTH_SHORT)
                    .show()
            }
        })

        mainFragmentViewModel.loadingLiveData.observe(viewLifecycleOwner) {

            if (it) binding.mainProgressBar.visibility = ProgressBar.VISIBLE
            else binding.mainProgressBar.visibility = ProgressBar.GONE

        }

        mainFragmentViewModel.getSeenRecipesLD().observe(viewLifecycleOwner) {
            mainAdapter.recipes = it
        }

        binding.mainRecycler.adapter = mainAdapter

        binding.mainRecycler.edgeEffectFactory = BounceEdgeEffectFactory()

        binding.mainRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING ||
                    newState == RecyclerView.SCROLL_STATE_IDLE) {

                    val manager = recyclerView.layoutManager as LinearLayoutManager

                    if (mainFragmentViewModel.getVisibleRecipes() -
                        manager.findLastVisibleItemPosition() < 4) {

                        mainFragmentViewModel.extendVisibleRecipes()
                    }

                }
            }
        })

        mainFragmentViewModel.getRecipes()

        binding.mainInputField.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                val searchText = textView.text

                hideKeyboard(requireContext(), binding.mainInputField)
                binding.mainInputField.clearFocus()


                if (searchText.isNotEmpty()) {

                    mainFragmentViewModel.searchRecipe(searchText.toString())

                }
            }
            true
        }

    }


    private fun hideKeyboard(context: Context, inputField: EditText ) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(inputField.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}