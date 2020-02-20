package com.sanjog.lanatest.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.sanjog.lanatest.LanaApplication
import com.sanjog.lanatest.R
import com.sanjog.lanatest.databinding.ActivityMainBinding
import com.sanjog.lanatest.di.component.ApplicationComponent

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    // Reference to the Login graph
    lateinit var mApplicationComponent: ApplicationComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mApplicationComponent = (applicationContext as LanaApplication).mApplicationComponent
        mApplicationComponent.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.navHostFragment)
        return navController.navigateUp()
    }
}
