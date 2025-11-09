package com.compose.a8jcbottomnavapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.compose.a8jcbottomnavapp.screens.HomeScreen
import com.compose.a8jcbottomnavapp.screens.ProfileScreen
import com.compose.a8jcbottomnavapp.screens.SettingsScreen

@Composable
fun NavGraph(navController: NavHostController,
             modifier: Modifier = Modifier){

    // Use navController to navigate to different
    // composables defined in the nav graph

    // Nav Host: acts as a container for hosting the Nav Graph
    NavHost(navController = navController,
        startDestination = NavRoute.Home.path) {

        addHomeScreen(
            navController,
            this
        )
        addProfileScreen(
            navController,
            this
        )

        addSettingsScreen(
            navController,
            this
        )

    }



}

fun addHomeScreen(navController: NavHostController,
                  navGraphBuilder: NavGraphBuilder
) {

    // navGraphBuilder: define the composables route
    // for the HomeScreen

    // Define a nav destination :Home
    navGraphBuilder.composable(
        route = NavRoute.Home.path
    ) {
        HomeScreen(
            navigateToProfile = {
                id,showDetails ->
                navController.navigate(
                    NavRoute.Profile.createRoute(88,false)
                )
            },

            navigateToSettings = {
                navController.navigate(
                    NavRoute.Settings.path
                )
            }
        )
    }
}

fun addProfileScreen(navController: NavHostController,
                     navGraphBuilder: NavGraphBuilder){

    navGraphBuilder.composable(
        route = NavRoute.Profile.path.plus("/{id}/{showDetails}"),


        arguments = listOf(

            navArgument("id"){
                type = NavType.IntType
            },

            navArgument("showDetails"){
                type = NavType.BoolType
            }

        )
    ){

        navBackStackEntry ->

        val args = navBackStackEntry.arguments

        ProfileScreen(
            id = args?.getInt("id")!!,
            showDetails = args.getBoolean("showDetails"),
            navigateToSettings = { navController.navigate(NavRoute.Settings.path) }
        )


    }
}


fun addSettingsScreen(navController: NavHostController,
                      navGraphBuilder: NavGraphBuilder){


    navGraphBuilder.composable(
        route = NavRoute.Settings.path
    ){

        SettingsScreen(
            navigateToHome = {navController
                .navigate(NavRoute.Home.path)
            }

        )


    }
}

