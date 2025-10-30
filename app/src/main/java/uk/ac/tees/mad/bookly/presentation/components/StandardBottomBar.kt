package uk.ac.tees.mad.bookly.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class BottomNavItem(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val label: String
) {
    object Search : BottomNavItem("search", Icons.Filled.Home, Icons.Outlined.Home, "Search")
    object ReadingList : BottomNavItem("reading_list", Icons.Filled.List, Icons.Outlined.List, "Reading List")
    object Profile : BottomNavItem("profile", Icons.Filled.Person, Icons.Outlined.Person, "Profile")
}

@Composable
fun StandardBottomBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Search,
        BottomNavItem.ReadingList,
        BottomNavItem.Profile
    )
    NavigationBar {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        if (currentRoute == item.route) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFD97D5D),
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color(0xFFD97D5D),
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}