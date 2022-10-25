package com.example.recipes_book.screens

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.core.view.MenuItemCompat
import androidx.core.view.MenuProvider
import androidx.recyclerview.widget.DiffUtil
import com.example.recipes_book.R
import com.example.recipes_book.databinding.FragmentMainBinding
import com.google.android.material.textfield.TextInputEditText
import java.lang.RuntimeException

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
    get() = _binding ?: throw RuntimeException("FragmentMainBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)

        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainInputField.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                val searchText = textView.text

                if (searchText.isNotEmpty()) {

                    TODO()

                }
            }
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}