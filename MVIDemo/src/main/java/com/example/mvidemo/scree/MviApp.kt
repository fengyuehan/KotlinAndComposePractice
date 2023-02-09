package com.example.mvidemo.scree

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mvidemo.MainScreen
import com.example.mvidemo.MainViewModel
import com.example.mvidemo.Route
import com.example.mvidemo.ui.theme.KotlinAndComposeTheme

@Composable
fun MviApp(){
    val navController = rememberNavController()
    KotlinAndComposeTheme {
        Scaffold (
            content = {
                NavHost(navController = navController, startDestination = Route.MainScreen){
                    mainScreenRoute(navController)
                }
            }
        )
    }


}

fun NavGraphBuilder.mainScreenRoute(navController: NavController){
    composable(Route.MainScreen){
        val viewModel = hiltViewModel<MainViewModel>()
        MainScreen(viewModel = viewModel)
    }
}