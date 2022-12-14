package com.example.recipes_book.screens

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.recipes_book.R
import com.example.recipes_book.models.State
import com.example.recipes_book.models.Status
import com.google.android.material.snackbar.Snackbar

open class BaseFragment: Fragment() {

    fun <T> renderState(root: View,
                        state: State<T>,
                        onLoading: () -> Unit,
                        onSuccess: (T) -> Unit,
                        onError: (String) -> Unit,
                        onEmpty: () -> Unit) {

        (root as ViewGroup).children.forEach { it.visibility = View.GONE }
        when (state.status) {
            Status.SUCCESS -> onSuccess(state.data!!)
            Status.LOADING -> onLoading()
            Status.EMPTY -> onEmpty()
            Status.ERROR -> onError(state.message!!)
        }
    }

    fun showSnackBar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)
            .setAnchorView(requireActivity().findViewById(R.id.bottom_navigation))
            .show()
    }

}