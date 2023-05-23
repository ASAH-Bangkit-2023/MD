package com.asahteam.md.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.asahteam.md.R
import com.asahteam.md.databinding.ActivityMainBinding
import com.asahteam.md.ui.utils.AuthViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        AuthViewModelFactory.getInstance(this@MainActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getUser().observe(this@MainActivity) { user ->
            if (user.accessToken.isEmpty() || user.refreshToken.isEmpty()) {
                Navigation.findNavController(this@MainActivity, R.id.fragmentContainerView)
                    .navigate(R.id.loginFragment)
            } else {
                Navigation.findNavController(this@MainActivity, R.id.fragmentContainerView)
                    .navigate(R.id.navigation_home)
            }
        }

        val navView: BottomNavigationView = binding.bottomNavigation
        val navController = findNavController(R.id.fragmentContainerView)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.navigation_home || destination.id == R.id.navigation_about || destination.id == R.id.navigation_profile) {
                binding.bottomNavigation.visibility = View.VISIBLE
            } else {
                binding.bottomNavigation.visibility = View.GONE
            }
        }
    }
}