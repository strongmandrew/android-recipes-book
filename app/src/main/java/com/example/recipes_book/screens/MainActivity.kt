package com.example.recipes_book.screens

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.renderscript.RenderScript
import com.example.recipes_book.R
import com.example.recipes_book.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import eightbitlab.com.blurview.RenderScriptBlur
import jp.wasabeef.blurry.Blurry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var mActivityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mActivityMainBinding.root)

        setBottomNavigationListener()

        setBottomNavigationBlur()

    }

    private fun setBottomNavigationListener() {
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

    private fun setBottomNavigationBlur() {
        val blurRadius = 10f
        val decorView = window.decorView
        val rootView = decorView
            .findViewById<ViewGroup>(android.R.id.content)
        val windowBackground = decorView.background

        mActivityMainBinding.blurView.setupWith(rootView, RenderScriptBlur(this))
            .setFrameClearDrawable(windowBackground)
            .setBlurRadius(blurRadius)
    }


}