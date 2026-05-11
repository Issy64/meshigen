package com.issy.meshigen.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.issy.meshigen.feature.collection.CollectionListScreen
import com.issy.meshigen.feature.detail.DetailScreen
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
            HomeScreen(
                onOpenDetailClick = { gourmetId ->
                    navController.navigate(
                        MeshigenDestination.createDetailRoute(gourmetId)
                    )
                },
            )
        }
        composable(route = MeshigenDestination.COLLECTION_ROUTE) {
            CollectionListScreen(
                onItemClick = { gourmetId ->
                    navController.navigate(
                        MeshigenDestination.createDetailRoute(gourmetId)
                    )
                },
            )
        }
        composable(
            route = MeshigenDestination.DETAIL_ROUTE,
            arguments = listOf(
                navArgument(MeshigenDestination.GOURMET_ID_ARG) {
                    type = NavType.StringType
                }
            ),
        ) { backStackEntry ->
            val gourmetId = backStackEntry.arguments
                ?.getString(MeshigenDestination.GOURMET_ID_ARG)
                .orEmpty()

            DetailScreen(
                gourmetId = gourmetId,
                onBackClick = { navController.navigateUp() },
            )
        }
    }
}
