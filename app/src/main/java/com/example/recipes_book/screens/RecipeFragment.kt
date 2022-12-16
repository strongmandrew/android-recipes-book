package com.example.recipes_book.screens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.children
import androidx.core.view.drawToBitmap
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.recipes_book.R
import com.example.recipes_book.adapters.*
import com.example.recipes_book.data.FavouritesRepositoryImpl
import com.example.recipes_book.data.MainRepositoryImpl
import com.example.recipes_book.databinding.CurrentStateBinding
import com.example.recipes_book.databinding.FragmentRecipeBinding
import com.example.recipes_book.models.Status
import com.example.recipes_book.models.retrofit.Recipe
import com.example.recipes_book.models.retrofit.SearchRecipe
import com.example.recipes_book.viewModels.RecipeFragmentViewModel
import com.example.recipes_book.viewModels.ShareRecipeHandler
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.lang.Error
import java.lang.Exception
import java.lang.RuntimeException
import java.util.regex.Pattern

private const val TAG = "RecipeFragment"

class RecipeFragment : BaseFragment() {

    private val receivedRecipe by lazy {
        arguments?.getParcelable<com.example.recipes_book.models.room.Recipe>("RECIPE")
    }

    private lateinit var recipeFragmentViewModel: RecipeFragmentViewModel
    private var _binding : FragmentRecipeBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("FragmentMainBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRecipeBinding.inflate(inflater, container, false)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigation = requireActivity()
            .findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigation.labelVisibilityMode = BottomNavigationView.LABEL_VISIBILITY_UNLABELED

        val stateBinding = CurrentStateBinding.bind(binding.root)

        recipeFragmentViewModel = RecipeFragmentViewModel(
            receivedRecipe?.id ?: 0,
            MainRepositoryImpl(),
            FavouritesRepositoryImpl(requireContext())
        )

        recipeFragmentViewModel.similarRecipesLiveData
            .observe(viewLifecycleOwner) {

            when (it.status) {

                Status.SUCCESS -> {
                    binding.similarProgress.visibility = View.GONE
                    binding.similarRecycler.visibility = View.VISIBLE

                    val similarAdapter = SimilarAdapter(it.data ?: listOf(),
                        object : SimilarAdapter.OnSimilarClickListener {
                        override fun onSimilarClick(similar: SearchRecipe) {
                            val recipeFragment = RecipeFragment()
                            val bundle = Bundle()

                            val isInFavourites = recipeFragmentViewModel.isInFavourites(similar.id)
                            bundle.putParcelable("RECIPE", similar.toRecipe(isInFavourites == 1))


                            recipeFragment.arguments = bundle
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.navigation_host, recipeFragment)
                                .addToBackStack(null)
                                .commit()
                        }
                    })
                    binding.similarRecycler.adapter = similarAdapter

                }
                Status.LOADING -> {
                    binding.similarProgress.visibility = View.VISIBLE
                    binding.similarRecycler.visibility = View.GONE
                }
                Status.EMPTY -> {
                    binding.similarProgress.visibility = View.GONE
                    binding.similarRecycler.visibility = View.GONE
                }
                Status.ERROR -> {
                    binding.similarProgress.visibility = View.GONE
                    binding.similarRecycler.visibility = View.GONE
                }
            }
        }

        recipeFragmentViewModel.recipeLiveData.observe(viewLifecycleOwner) {

            renderState(
                root = binding.root,
                state = it,
                onLoading = {
                    binding.scrollRecipe.visibility = View.VISIBLE
                    binding.titleContainer.visibility = View.VISIBLE

                    binding.scrollRecipe.children.forEach { childView ->
                        childView.visibility = View.GONE
                    }

                    stateBinding.progressBar.visibility = View.VISIBLE
                },
                onEmpty = {

                },
                onError = {
                    binding.scrollRecipe.visibility = View.VISIBLE
                    stateBinding.errorState.visibility = View.VISIBLE

                },
                onSuccess = { recipe ->
                    binding.titleContainer.visibility = View.VISIBLE
                    binding.scrollRecipe.visibility = View.VISIBLE

                    binding.scrollRecipe.children.forEach { childView ->
                        childView.visibility = View.VISIBLE
                    }

                    setupRecipeScreen(
                        recipe,
                        receivedRecipe?.isFavourite ?: false
                    )

                    val pattern = Pattern.compile(" ")
                    recipeFragmentViewModel.getSimilarRecipes(
                        recipe.title.split(pattern, 0).first(),
                    )

                })


        }

        recipeFragmentViewModel.getRecipeById()

        binding.scrollRecipe.outlineProvider = RecyclerOutlineProvider()
        binding.scrollRecipe.clipToOutline = true



    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupRecipeScreen(recipe: Recipe, isFavouriteRecipe: Boolean) {


        binding.favoritesButton.isChecked = isFavouriteRecipe

        binding.favoritesButton.setOnClickListener {
            if (!binding.favoritesButton.isChecked) {
                recipeFragmentViewModel
                    .deleteFromFavourites(recipe.toRecipe(false))
                showSnackBar("${recipe.title} deleted from favourites")
            }
            else {
                recipeFragmentViewModel
                    .addToFavourites(recipe.toRecipe(true))
                showSnackBar("${recipe.title} added to favourites")
            }
        }


        CoroutineScope(Dispatchers.IO).launch {

            val glide = async {
                Glide.with(binding.root)
                    .load(recipe.image)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.default_recipe)
                    .error(R.drawable.default_recipe)

            }

            withContext(Dispatchers.Main) {
                glide.await().into(binding.recipeImage)

            }
        }

        val palette = Palette.from(binding.recipeImage.drawable.toBitmap()).generate()
        val color = palette.lightMutedSwatch

        Log.d("COLOR", color.toString())

        binding.recipeTitle.text = recipe.title

        binding.shareButton.setOnClickListener {

            val shareText = "I've liked ${receivedRecipe?.title} recipe in Recipe book. Join us! " +
                    "https://play.google.com/store/apps/details?id=com.tudorspan.recipekeeper"

            val share = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }

            startActivity(Intent.createChooser(share, null))
        }

        val chipAdapter = ChipAdapter(recipe.diets, color?.rgb ?: "#FFFFFF".toInt())
        binding.chipRecycler.adapter = chipAdapter
        val productAdapter = ProductAdapter(recipe.extendedIngredients)
        binding.productsRecycler.adapter = productAdapter

        if (recipe.instructions.isNotBlank()) {
            binding.emptyStateText.visibility = View.GONE
            binding.recipesSteps.visibility = View.VISIBLE

            val stepAdapter = StepAdapter(recipe.analyzedInstructions.first().steps,
                color?.rgb ?: "#FFFFFF".toInt())

            binding.stepsRecycler.adapter = stepAdapter
        }
        else {
            binding.recipesSteps.visibility = View.GONE
            binding.emptyStateText.visibility = View.VISIBLE
        }

        binding.sourceButton.setOnClickListener {
            val source = Intent(Intent.ACTION_VIEW)

            source.data = Uri.parse(recipe.sourceUrl)

            startActivity(source)
        }
    }


}