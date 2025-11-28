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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.bookly.presentation.components.BottomNavItem
import uk.ac.tees.mad.bookly.presentation.components.StandardBottomBar
import uk.ac.tees.mad.bookly.presentation.home.HomeRoot
import uk.ac.tees.mad.bookly.presentation.navigation.GraphRoute
import uk.ac.tees.mad.bookly.presentation.reading_list.ReadingListRoot

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(mainNavController: NavHostController) { 
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        topBar = {
            val title = when (currentDestination?.route) {
                BottomNavItem.Search.route -> "Search"
                BottomNavItem.ReadingList.route -> "My Reading List"
                else -> ""
            }
            CenterAlignedTopAppBar(
                title = { Text(title, fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { mainNavController.navigate(GraphRoute.Notifications) }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                }
            )
        },
        bottomBar = { StandardBottomBar(navController = bottomNavController) }
    ) { paddingValues ->
        NavHost(
            navController = bottomNavController,
            startDestination = BottomNavItem.Search.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(BottomNavItem.Search.route) {
                HomeRoot(
                    onBookClick = { bookId ->
                        mainNavController.navigate(GraphRoute.BookDetails(bookId))
                    },
                    onNotificationsClick = { mainNavController.navigate(GraphRoute.Notifications) }
                )
            }
            composable(BottomNavItem.ReadingList.route) {
                ReadingListRoot(
                    onBookClick = { bookId ->
                        mainNavController.navigate(GraphRoute.BookDetails(bookId))
                    },
                    onNotificationsClick = { mainNavController.navigate(GraphRoute.Notifications) }
                )
            }
            composable(BottomNavItem.Profile.route) {
                Text("Profile Screen")
            }
        }
    }
}