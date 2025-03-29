package com.example.skillcinema.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.skillcinema.R
import com.example.skillcinema.databinding.ActivityMainBinding

class MainActivity : FragmentActivity() {

    private lateinit var binding: ActivityMainBinding
    private var startDestinationCheck = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadStartDestination()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController
        val inflater = navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_graph)
        navController.graph = graph.apply {
            setStartDestination(if (startDestinationCheck) R.id.homepageFragment else R.id.onBoardingFragment)
        }
        binding.bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->

            if (destination.id == R.id.onBoardingFragment
                || destination.id == R.id.searchFilterFragment
                || destination.id == R.id.searchFilterGenreFragment
                || destination.id == R.id.searchFilterCountryFragment
                || destination.id == R.id.searchFilterYearFragment) {
                binding.bottomNav.visibility = View.GONE
            } else {
                val sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.apply {
                    putBoolean("START", true)
                }.apply()
                binding.bottomNav.visibility = View.VISIBLE
            }
        }
    }

    private fun loadStartDestination() {
        val sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE)
        startDestinationCheck = sharedPreferences.getBoolean("START", false)
    }

}