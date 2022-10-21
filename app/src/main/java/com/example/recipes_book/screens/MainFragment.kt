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
import com.example.recipes_book.R
import com.example.recipes_book.databinding.FragmentMainBinding
import com.google.android.material.textfield.TextInputEditText

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_main, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchItem = view.findViewById<TextInputEditText>(R.id.main_input_field)

        searchItem.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                val searchText = textView.text

                if (searchText.isNotEmpty()) {

                    TODO()

                }
            }
            true
        }
    }
}