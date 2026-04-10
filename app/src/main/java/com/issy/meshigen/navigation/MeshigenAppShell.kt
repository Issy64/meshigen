package com.issy.meshigen.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.issy.meshigen.R
import com.issy.meshigen.ui.theme.MeshigenTheme

private data class TopLevelDestination(
    val route: String,
    val labelResId: Int,
    val icon: @Composable () -> Unit,
)

@Composable
internal fun MeshigenAppShell(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val isDetailRoute = currentRoute?.startsWith(MeshigenDestination.DETAIL_BASE_ROUTE) == true

    val topLevelDestinations = listOf(
        TopLevelDestination(
            route = MeshigenDestination.HOME_ROUTE,
            labelResId = R.string.navigation_home,
            icon = { Icon(imageVector = Icons.Filled.Home, contentDescription = null) },
        ),
        TopLevelDestination(
            route = MeshigenDestination.COLLECTION_ROUTE,
            labelResId = R.string.navigation_collection,
            icon = { Icon(imageVector = Icons.AutoMirrored.Filled.List, contentDescription = null) },
        ),
    )

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (!isDetailRoute) {
                NavigationBar {
                    topLevelDestinations.forEach { destination ->
                        val isSelected = currentRoute == destination.route

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(destination.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = destination.icon,
                            label = { Text(text = stringResource(destination.labelResId)) },
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        MeshigenNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MeshigenAppShellPreview() {
    MeshigenTheme {
        MeshigenAppShell()
    }
}
