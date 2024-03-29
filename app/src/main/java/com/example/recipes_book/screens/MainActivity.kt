package com.example.recipes_book.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.example.recipes_book.R
import com.example.recipes_book.data.FavouritesRepositoryImpl
import com.example.recipes_book.databinding.ActivityMainBinding
import com.example.recipes_book.repository.FavouritesRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var mActivityMainBinding: ActivityMainBinding

    private lateinit var favouritesRepository: FavouritesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        mActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mActivityMainBinding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        favouritesRepository = FavouritesRepositoryImpl(this)

        setBottomNavigationListener()

        setBottomNavigationBlur()

    }



    override fun onBackPressed() {
        super.onBackPressed()
        mActivityMainBinding.bottomNavigation.labelVisibilityMode =
            BottomNavigationView.LABEL_VISIBILITY_SELECTED
    }

    private fun setBottomNavigationListener() {

        mActivityMainBinding.bottomNavigation.setOnItemSelectedListener {

            supportFragmentManager.popBackStack()

            mActivityMainBinding.bottomNavigation.labelVisibilityMode =
                BottomNavigationView.LABEL_VISIBILITY_SELECTED

            lateinit var destinationFragment: Fragment


            when (it.itemId) {

                R.id.recipe_item -> {

                    destinationFragment = MainFragment()




                }

                R.id.favourites_item -> {

                    destinationFragment = FavoritesFragment()

                    CoroutineScope(Dispatchers.Main).launch {
                        Log.d("MAIN", favouritesRepository.getAllFavourites().toString())
                    }


                }



            }


            supportFragmentManager.beginTransaction()
                .replace(R.id.navigation_host, destinationFragment)
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

    override fun onSaveInstanceState(outState: Bundle) {

        Log.d("MAIN", "onSaveInstance")

        super.onSaveInstanceState(outState)

    }


}