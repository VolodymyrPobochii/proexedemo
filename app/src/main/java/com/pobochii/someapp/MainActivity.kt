package com.pobochii.someapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.AbstractListDetailFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.pobochii.someapp.databinding.MainActivityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = MainActivityBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        setSupportActionBar(viewBinding.toolbar)

//        val fragment =
//            supportFragmentManager.findFragmentById(R.id.nav_host) as AbstractListDetailFragment
//        val navController = fragment.detailPaneNavHostFragment.navController
//        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val fragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as AbstractListDetailFragment
        val navController = fragment.detailPaneNavHostFragment.navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}