package com.compose.a8jcbottomnavapp.navigation

sealed class NavRoute(val path:String) {
    // Sealed Class: represent restricted hierarchies
    // where a value can have one of the types from a
    // limited set, but no other types

    object Home: NavRoute("home")

    object Profile: NavRoute("profile"){

        fun createRoute(id: Int, showDetails : Boolean):String{
            return "profile/$id/$showDetails"
        }
    }



    object Settings: NavRoute("settings")


}