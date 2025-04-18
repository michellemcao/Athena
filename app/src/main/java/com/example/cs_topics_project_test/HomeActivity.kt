package com.example.cs_topics_project_test

import android.os.Bundle
import android.view.Menu
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.cs_topics_project_test.databinding.ActivityMainBinding
import androidx.core.content.ContextCompat
import android.content.Intent
import com.example.cs_topics_project_test.task.TaskDataStructure
import android.widget.TextView
import com.example.cs_topics_project_test.login.SignInActivity
import com.example.cs_topics_project_test.ui.ChatListActivity
import com.example.cs_topics_project_test.ui.UserSettings
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {


    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TaskDataStructure.cleanUpTasks()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_main)

        // Set the toolbar using the correct binding reference
        setSupportActionBar(binding.toolbar)

        // Set up the FAB click listener
        binding.fab.setImageResource(R.drawable.baseline_chat_24) // Change FAB icon here
        binding.fab.imageTintList = ContextCompat.getColorStateList(this, android.R.color.white)
        binding.fab.setOnClickListener {
            // Navigate to ChatActivity
            // Navigate to the List of Chats (ChatListActivity)
            val intent = Intent(this, ChatListActivity::class.java)
            startActivity(intent)
        }


        // Setup drawer layout and navigation
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // leads to a fragment in content_main.xml that leads to the navigation set-up in mobile_navigation

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_calendar, R.id.nav_tasks, R.id.user_settings),
            drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        // show user's name and email in nav header
        val headerView = navView.getHeaderView(0)
        val nameTextView = headerView.findViewById<TextView>(R.id.nameTextView)
        val emailTextView = headerView.findViewById<TextView>(R.id.textView)
        if (currentUser != null) {
            val name = currentUser.displayName
            val email = currentUser.email
            nameTextView.text = name
            emailTextView.text = email
        }


        // signs user out of account when sign out button clicked
        navView.getMenu().findItem(R.id.signOutButton).setOnMenuItemClickListener ({ menuItem ->
            firebaseAuth.signOut()
            startActivity(Intent(this, SignInActivity::class.java))
            true
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
