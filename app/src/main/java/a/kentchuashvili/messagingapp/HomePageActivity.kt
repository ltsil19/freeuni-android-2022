package a.kentchuashvili.messagingapp

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.Exception


class HomePageActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

            val navHostFragment = supportFragmentManager.findFragmentById(R.id.activity_home_nav_host_fragment) as NavHostFragment
            navController = navHostFragment.navController

            val bottomNavigationView =
                findViewById<BottomNavigationView>(R.id.activity_home_bottom_navigation_view)
            bottomNavigationView.setupWithNavController(navController)
            NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }
}