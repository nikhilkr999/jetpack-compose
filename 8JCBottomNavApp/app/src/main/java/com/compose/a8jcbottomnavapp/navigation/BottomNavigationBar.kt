package com.compose.a8jcbottomnavapp.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
@Composable
fun BottomNavigationBar(navController: NavController) {

    val navItems = listOf(NavItem.Home, NavItem.Profile, NavItem.Settings)

    // Observe the current back stack entry
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        navItems.forEach { item ->

            // Fix: Handle dynamic routes (e.g., "profile/77/true")
            val isSelected = currentRoute?.startsWith(item.path) == true

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    val route = when (item.path) {
                        NavRoute.Profile.path -> "${NavRoute.Profile.path}/77/true"
                        else -> item.path
                    }

                    navController.navigate(route) {
                        // Avoid building multiple instances of the same screen
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(item.title) }
            )
        }
    }
}
