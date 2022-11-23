package com.example.recipes_book.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.recipes_book.R
import com.example.recipes_book.adapters.RecyclerOutlineProvider
import com.example.recipes_book.databinding.FragmentMainBinding
import com.example.recipes_book.databinding.FragmentRecipeBinding
import com.example.recipes_book.models.room.Recipe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.lang.RuntimeException

private const val TAG = "RecipeFragment"

class RecipeFragment : Fragment() {

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

        binding.scrollRecipe.outlineProvider = RecyclerOutlineProvider()
        binding.scrollRecipe.clipToOutline = true

        val recipe: Recipe? = arguments?.getParcelable("RECIPE")

        binding.recipeTitle.text = recipe?.title

        CoroutineScope(Dispatchers.Main).launch {

            Glide.with(view)
                .load(recipe?.imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(binding.recipeImage)

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}