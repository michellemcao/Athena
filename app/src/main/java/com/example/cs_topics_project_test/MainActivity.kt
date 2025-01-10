package com.example.cs_topics_project_test

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class MainActivity : AppCompatActivity() {
    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (user != null) {
            FirebaseAuth.getInstance().signOut(); //GET RID OF THIS LATER!!
            // go to main activity page (dont have it here)
        }
            startActivity(Intent(this, SignInActivity::class.java))

    }
/*
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the toolbar using the correct binding reference
        setSupportActionBar(binding.toolbar)

        // Set up the FAB click listener
        binding.fab.setImageResource(R.drawable.baseline_chat_24) // Change FAB icon here
        binding.fab.imageTintList = ContextCompat.getColorStateList(this, android.R.color.white)
        binding.fab.setOnClickListener { view: View ->
            Snackbar.make(view, "Chat Function Unavailable for now ;)", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()
        }
        // Setup drawer layout and navigation
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow),
            drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        */

    }
/*
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}
*/