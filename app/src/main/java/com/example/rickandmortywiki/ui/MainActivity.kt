package com.example.rickandmortywiki.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.rickandmortywiki.R
import com.example.rickandmortywiki.databinding.ActivityMainBinding
import com.example.rickandmortywiki.utils.channel.ChannelImpl
import com.example.rickandmortywiki.utils.channel.Event
import com.example.rickandmortywiki.utils.extensions.collectLifecycleFlow
import com.example.rickandmortywiki.utils.extensions.gone
import com.example.rickandmortywiki.utils.extensions.visible
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_RickAndMortyWiki)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavView()
        setupEventCollector()
    }

    private fun setupBottomNavView() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        binding.bottomNavView.setupWithNavController(navHostFragment.navController)
        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.character_details_fragment -> binding.bottomNavView.gone()
                R.id.residents_fragment -> binding.bottomNavView.gone()
                else -> binding.bottomNavView.visible()
            }
        }
    }

    private fun setupEventCollector() {
        collectLifecycleFlow(ChannelImpl.event) { event ->
            when (event) {
                is Event.ShowSnackBar -> {
                    val snackBar = Snackbar.make(binding.root, event.message, Snackbar.LENGTH_SHORT)
                    if (binding.bottomNavView.isVisible) snackBar.setAnchorView(binding.bottomNavView)
                    snackBar.show()
                }
            }
        }
    }
}