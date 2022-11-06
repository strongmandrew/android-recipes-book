package com.example.recipes_book.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.example.recipes_book.R
import com.example.recipes_book.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var mActivityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mActivityMainBinding.root)


        mActivityMainBinding.bottomNavigation.setOnItemSelectedListener {

            lateinit var destinationFragment: Fragment


            when (it.itemId) {

                R.id.recipe_item -> {
                    destinationFragment = MainFragment()


                }

                R.id.favourites_item -> {
                    destinationFragment = FavoritesFragment()

                }

            }

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.navigation_host, destinationFragment)
                .addToBackStack("${destinationFragment.id}")
                .commit()

            true

        }




    }

}