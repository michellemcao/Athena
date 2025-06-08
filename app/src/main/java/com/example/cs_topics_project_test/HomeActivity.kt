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
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.drawable.GradientDrawable
import android.util.Base64
import android.widget.ImageView
import com.example.cs_topics_project_test.task.TaskDataStructure
import android.widget.TextView
import android.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.cs_topics_project_test.login.SignInActivity
import com.example.cs_topics_project_test.chat.ChatListActivity
import com.example.cs_topics_project_test.themes.ThemeManager
import com.example.cs_topics_project_test.ui.UserSettings
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {


    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TaskDataStructure.cleanUpTasks()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // setContentView(R.layout.activity_main)

        // Set the toolbar using the correct binding reference
        setSupportActionBar(binding.toolbar)

        // local copy of current theme
        val colors = ThemeManager.currentThemeColors!!

        binding.toolbar.setBackgroundColor(colors.toolbar) // the part where it says "Home"

        val titleView = findViewById<TextView>(R.id.titleTextView)
        titleView.setBackgroundColor(colors.toolbar)

        // Set up the FAB click listener and style
        findViewById<FloatingActionButton>(R.id.fab).backgroundTintList = ColorStateList.valueOf(colors.chatAccent)
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
            setOf(R.id.nav_home, R.id.nav_calendar, R.id.nav_tasks, R.id.nav_notes, R.id.user_settings),
            drawerLayout
        )

        // nav bar style and functionality
        val colorState = ColorStateList.valueOf(colors.icon)
        navView.itemIconTintList = colorState
        navView.itemTextColor = colorState
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        val currentUser = firebaseAuth.currentUser

        // show user's name and email and pfp in nav header
        val headerView = navView.getHeaderView(0)
        // nav gradient style set
        val gradient = GradientDrawable(
            GradientDrawable.Orientation.BL_TR, // 135 degrees
            intArrayOf(colors.startColor, colors.centerColor, colors.endColor)
        )
        gradient.gradientType = GradientDrawable.LINEAR_GRADIENT
        headerView.background = gradient

        val nameTextView = headerView.findViewById<TextView>(R.id.nameTextView)
        val emailTextView = headerView.findViewById<TextView>(R.id.textView)
        val pfpImageView = headerView.findViewById<ImageView>(R.id.imageView)
        if (currentUser != null) {
            val name = currentUser.displayName
            val email = currentUser.email
            nameTextView.text = name
            emailTextView.text = email

            firestore.collection("users").document(currentUser.uid).collection("profilePicture").document("pfpImg").get()
                .addOnSuccessListener { document ->
                    document.getString("pfp")?.let {
                        val decoded = Base64.decode(it, Base64.DEFAULT)
                        pfpImageView.setImageBitmap(BitmapFactory.decodeByteArray(decoded, 0, decoded.size))
                    }
                }

        }


        // signs user out of account when sign out button clicked
        navView.getMenu().findItem(R.id.signOutButton).setOnMenuItemClickListener ({ menuItem ->
            firebaseAuth.signOut()
            startActivity(Intent(this, SignInActivity::class.java))
            true
        })

        window.statusBarColor = colors.header
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
