package com.compose.a8jcbottomnavapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings

sealed class NavItem {

    object Home:
            Item(NavRoute.Home.path.toString(),
                title = "Home",
                icon = Icons.Default.Home
            )

    object Profile:
            Item(NavRoute.Profile.path,
                title = "Profile",
                icon = Icons.Default.Person
            )
    object Settings:
        Item(NavRoute.Settings.path,
            "Settings", Icons.Default.Settings)
}