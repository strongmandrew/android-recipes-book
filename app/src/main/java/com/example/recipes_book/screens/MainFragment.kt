package com.example.recipes_book.screens

import BounceEdgeEffectFactory
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes_book.adapters.RecipeAdapter
import com.example.recipes_book.adapters.RecyclerOutlineProvider
import com.example.recipes_book.data.FavouritesRepositoryImpl
import com.example.recipes_book.data.MainRepositoryImpl
import com.example.recipes_book.databinding.CurrentStateBinding
import com.example.recipes_book.databinding.FragmentMainBinding
import com.example.recipes_book.models.Status
import com.example.recipes_book.models.room.Recipe
import com.example.recipes_book.viewModels.MainFragmentViewModel
import com.google.android.material.snackbar.Snackbar
import java.lang.RuntimeException

private const val TAG = "MainFragment"

class MainFragment : BaseFragment() {

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

        val stateBinding = CurrentStateBinding.bind(binding.root)

        mainFragmentViewModel = MainFragmentViewModel(MainRepositoryImpl(),
            FavouritesRepositoryImpl(requireContext()))

        val mainAdapter = adapterInit(view)
        adapterSetup(mainAdapter)

        mainFragmentViewModel.recipesLiveData.observe(viewLifecycleOwner) { state ->

            renderState(
                root = binding.root,
                state = state,
                onLoading = {
                    binding.searchContainer.visibility = View.VISIBLE
                    binding.recyclerContainer.visibility = View.VISIBLE
                    binding.mainRecycler.visibility = View.GONE
                    stateBinding.progressBar.visibility = ProgressBar.VISIBLE
                },
                onSuccess = {
                    binding.recyclerContainer.visibility = View.VISIBLE
                    binding.searchContainer.visibility = View.VISIBLE
                    binding.mainRecycler.visibility = View.VISIBLE
                    mainAdapter.submitList(state.data)
                },
                onError = {
                    binding.searchContainer.visibility = View.VISIBLE
                    binding.recyclerContainer.visibility = View.VISIBLE
                    binding.mainRecycler.visibility = View.GONE
                    stateBinding.errorState.visibility = View.VISIBLE
                    Log.d(TAG, it)
                },
                onEmpty = {
                    binding.searchContainer.visibility = View.VISIBLE
                    binding.recyclerContainer.visibility = View.VISIBLE
                    binding.mainRecycler.visibility = View.GONE
                    stateBinding.emptyState.visibility = View.VISIBLE
                })

        }

        mainFragmentViewModel.getRecipes()

        searchSetup()

        stateBinding.reloadButton.setOnClickListener {
            mainFragmentViewModel.getRecipes()
        }

    }

    private fun searchSetup() {
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

    private fun adapterSetup(mainAdapter: RecipeAdapter) {

        with(binding.mainRecycler) {
            adapter = mainAdapter
            edgeEffectFactory = BounceEdgeEffectFactory()
            outlineProvider = RecyclerOutlineProvider()
            clipToOutline = true

        }

    }

    private fun adapterInit(view: View): RecipeAdapter {
        val mainAdapter = RecipeAdapter(object : RecipeAdapter.FavouritesClickListener {
            override fun onAddClick(recipe: Recipe) {
                mainFragmentViewModel.addToFavourites(recipe)
                Snackbar
                    .make(
                        view,
                        "${recipe.title} added to favourites!",
                        Snackbar.LENGTH_SHORT
                    )
                    .show()
            }

            override fun onDeleteClick(recipe: Recipe) {
                mainFragmentViewModel.deleteFromFavourites(recipe)
                Snackbar
                    .make(
                        view,
                        "${recipe.title} deleted from favourites!",
                        Snackbar.LENGTH_SHORT
                    )
                    .show()
            }
        })
        return mainAdapter
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