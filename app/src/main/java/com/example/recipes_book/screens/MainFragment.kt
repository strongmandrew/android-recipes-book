package com.example.recipes_book.screens

import BounceEdgeEffectFactory
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipes_book.R
import com.example.recipes_book.adapters.RecipeAdapter
import com.example.recipes_book.adapters.RecyclerOutlineProvider
import com.example.recipes_book.data.FavouritesRepositoryImpl
import com.example.recipes_book.data.MainRepositoryImpl
import com.example.recipes_book.databinding.CurrentStateBinding
import com.example.recipes_book.databinding.FragmentMainBinding
import com.example.recipes_book.models.room.Recipe
import com.example.recipes_book.viewModels.MainFragmentViewModel
import com.google.android.material.snackbar.Snackbar
import java.lang.RuntimeException
import java.text.FieldPosition

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

        mainFragmentViewModel = MainFragmentViewModel(
            MainRepositoryImpl(),
            FavouritesRepositoryImpl(requireContext())
        )

        val mainAdapter: RecipeAdapter = adapterInit(binding.root, arrayListOf())

        stateBinding.reloadButton.setOnClickListener {
            mainFragmentViewModel.getRecipes()
        }

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


                    mainAdapter.submitList(state.data as ArrayList<Recipe> /* = java.util.ArrayList<com.example.recipes_book.models.room.Recipe> */)
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

    private fun adapterInit(view: View, data: ArrayList<Recipe>): RecipeAdapter {
        val mainAdapter = RecipeAdapter(data, object : RecipeAdapter.FavouritesClickListener {
            override fun onAddClick(recipe: Recipe, position: Int, adapter: RecipeAdapter) {
                mainFragmentViewModel.addToFavourites(recipe)
                Snackbar
                    .make(
                        view,
                        "${recipe.title} added to favourites!",
                        Snackbar.LENGTH_SHORT
                    )
                    .setAnchorView(requireActivity().findViewById(R.id.bottom_navigation))
                    .show()
                adapter.itemHasBeenChanged(position)

            }

            override fun onDeleteClick(recipe: Recipe, position: Int, adapter: RecipeAdapter) {
                mainFragmentViewModel.deleteFromFavourites(recipe)
                Snackbar
                    .make(
                        view,
                        "${recipe.title} deleted from favourites!",
                        Snackbar.LENGTH_SHORT
                    )
                    .setAnchorView(requireActivity().findViewById(R.id.bottom_navigation))
                    .show()
                adapter.itemHasBeenChanged(position)
            }

            override fun onItemClick(recipe: Recipe) {
                val recipeFragment = RecipeFragment()
                val bundle = Bundle()
                bundle.putParcelable("RECIPE", recipe)
                recipeFragment.arguments = bundle
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.navigation_host, recipeFragment)
                    .addToBackStack(null)
                    .commit()
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


        /*val manager = binding.mainRecycler.layoutManager as LinearLayoutManager
        val position = manager.findFirstCompletelyVisibleItemPosition()

        val bundle = Bundle().apply {
            putParcelableArrayList(RECYCLER_DATA, recyclerData)
            putInt(RECYCLER_POSITION, position)
            putString(SEARCH_TEXT, binding.mainInputField.text.toString())
        }

        saveState(bundle)*/

        _binding = null
    }
}