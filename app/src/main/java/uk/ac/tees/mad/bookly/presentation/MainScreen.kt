package uk.ac.tees.mad.bookly.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.bookly.presentation.components.BottomNavItem
import uk.ac.tees.mad.bookly.presentation.components.StandardBottomBar
import uk.ac.tees.mad.bookly.presentation.home.HomeRoot

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        topBar = {
            // Only show the top bar on the search screen
            if (currentDestination?.route == BottomNavItem.Search.route) {
                CenterAlignedTopAppBar(
                    title = { Text("Search", fontWeight = FontWeight.Bold) },
                    actions = {
                        IconButton(onClick = { /* TODO */ }) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                        }
                    }
                )
            }
        },
        bottomBar = { StandardBottomBar(navController = navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Search.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(BottomNavItem.Search.route) {
                HomeRoot(
                    onBookClick = { /* Navigate to book details */ },

                    onNotificationClick = {  }
                )
            }
            composable(BottomNavItem.ReadingList.route) {
                // Placeholder for Reading List Screen
                Text("Reading List Screen")
            }
            composable(BottomNavItem.Profile.route) {
                // Placeholder for Profile Screen
                Text("Profile Screen")
            }
        }
    }
}