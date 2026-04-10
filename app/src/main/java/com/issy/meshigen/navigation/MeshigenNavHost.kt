package com.issy.meshigen.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.issy.meshigen.feature.collection.CollectionListScreen
import com.issy.meshigen.feature.home.HomeScreen

@Composable
internal fun MeshigenNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = MeshigenDestination.HOME_ROUTE,
        modifier = modifier,
    ) {
        composable(route = MeshigenDestination.HOME_ROUTE) {
            HomeScreen()
        }
        composable(route = MeshigenDestination.COLLECTION_ROUTE) {
            CollectionListScreen()
        }
    }
}
