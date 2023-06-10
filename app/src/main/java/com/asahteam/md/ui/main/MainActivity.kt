package com.asahteam.md.ui.main

import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.asahteam.md.R
import com.asahteam.md.databinding.ActivityMainBinding
import com.asahteam.md.ui.utils.AuthViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.time.LocalDate
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        AuthViewModelFactory.getInstance(this@MainActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 15)
        calendar.set(Calendar.MINUTE, 0)

        val currentTime = System.currentTimeMillis()
        val targetTime = calendar.timeInMillis
        val initialDelay = targetTime - currentTime

        val workRequest = PeriodicWorkRequestBuilder<MainWorkRequest>(1, TimeUnit.DAYS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(this).enqueue(workRequest)

        viewModel.getUser().observe(this@MainActivity) { user ->
            if (LocalDate.now().dayOfMonth != user.date) {
                viewModel.logOut()
            }
            if (user.accessToken.isEmpty() || user.refreshToken.isEmpty()) {
                Navigation.findNavController(this@MainActivity, R.id.fragmentContainerView)
                    .navigate(R.id.loginFragment)
            } else {
                if (savedInstanceState == null) {
                    Navigation.findNavController(this@MainActivity, R.id.fragmentContainerView)
                        .navigate(R.id.navigation_home)
                }
            }
        }

        val navView: BottomNavigationView = binding.bottomNavigation
        val navController = findNavController(R.id.fragmentContainerView)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.navigation_home || destination.id == R.id.navigation_about || destination.id == R.id.navigation_profile ||
                destination.id == R.id.navigation_maps
            ) {
                binding.bottomNavigation.visibility = View.VISIBLE
            } else {
                binding.bottomNavigation.visibility = View.GONE
            }
        }
    }
}