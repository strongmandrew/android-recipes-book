package com.example.recipes_book.screens

import BounceEdgeEffectFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.recipes_book.R
import com.example.recipes_book.adapters.RecipeAdapter
import com.example.recipes_book.adapters.RecyclerOutlineProvider
import com.example.recipes_book.data.FavouritesRepositoryImpl
import com.example.recipes_book.databinding.CurrentStateBinding
import com.example.recipes_book.databinding.FragmentFavoritesBinding
import com.example.recipes_book.models.room.Recipe
import com.example.recipes_book.viewModels.FavouritesFragmentViewModel
import com.google.android.material.snackbar.Snackbar
import java.lang.RuntimeException

private const val TAG = "FavoritesFragment"

class FavoritesFragment : BaseFragment() {

    private lateinit var favouritesFragmentViewModel: FavouritesFragmentViewModel
    private var _binding : FragmentFavoritesBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("FragmentFavouritesBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favouritesFragmentViewModel =
            FavouritesFragmentViewModel(FavouritesRepositoryImpl(requireContext()))

        val stateBinding = CurrentStateBinding.bind(binding.root)

        favouritesFragmentViewModel.favouritesLiveData.observe(viewLifecycleOwner) { it ->

            renderState(binding.root, it,
                onLoading = {
                    binding.titleContainer.visibility = View.VISIBLE
                    binding.recyclerContainer.visibility = View.VISIBLE
                    stateBinding.errorState.visibility = View.GONE
                    stateBinding.progressBar.visibility = View.VISIBLE
                    stateBinding.emptyState.visibility = View.GONE
                    stateBinding.reloadButton.visibility = View.GONE
                },
                onSuccess = {
                    stateBinding.emptyState.visibility = View.GONE
                    stateBinding.reloadButton.visibility = View.GONE
                    stateBinding.errorState.visibility = View.GONE
                    binding.recyclerContainer.visibility = View.VISIBLE
                    binding.favouritesRecycler.visibility = View.VISIBLE
                    binding.titleContainer.visibility = View.VISIBLE


                    val favouritesAdapter = RecipeAdapter(it as ArrayList<Recipe> /* = java.util.ArrayList<com.example.recipes_book.models.room.Recipe> */,object : RecipeAdapter.FavouritesClickListener {
                        override fun onAddClick(recipe: Recipe, position: Int) {

                            favouritesFragmentViewModel.addToFavourites(recipe)

                        }

                        override fun onDeleteClick(recipe: Recipe, position: Int, adapter: RecipeAdapter) {

                            favouritesFragmentViewModel.deleteFromFavourites(recipe)
                            adapter.removeRecipe(recipe, position)

                            Snackbar
                                .make(
                                    binding.recyclerContainer,
                                    "${recipe.title} deleted from favourites!",
                                    Snackbar.LENGTH_SHORT
                                )
                                .show()


                        }

                        override fun onItemClick(view: View, recipe: Recipe) {
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

                    with(binding.favouritesRecycler) {
                        adapter = favouritesAdapter
                        edgeEffectFactory = BounceEdgeEffectFactory()
                        outlineProvider = RecyclerOutlineProvider()
                        clipToOutline = true

                    }

                },
                onError = {
                    stateBinding.errorState.visibility = View.VISIBLE
                    stateBinding.reloadButton.visibility = View.VISIBLE
                    stateBinding.emptyState.visibility = View.GONE
                    stateBinding.progressBar.visibility = View.GONE
                },
                onEmpty = {
                    stateBinding.errorState.visibility = View.GONE
                    stateBinding.reloadButton.visibility = View.GONE
                    stateBinding.emptyState.visibility = View.VISIBLE
                    stateBinding.progressBar.visibility = View.GONE
                    binding.recyclerContainer.visibility = View.VISIBLE
                    binding.titleContainer.visibility = View.VISIBLE
                },)
        }

        favouritesFragmentViewModel.loadFavourites()


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }




}