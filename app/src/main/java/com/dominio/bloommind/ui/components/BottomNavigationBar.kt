package com.dominio.bloommind.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dominio.bloommind.ui.navigation.BloomMindNavItems

@Composable
fun BottomNavigationBar(navController: NavController, items: List<BloomMindNavItems>) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.Black
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.6f),
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.White.copy(alpha = 0.6f),
                    indicatorColor = Color.Black
                ),
                icon = { Icon(item.icon, contentDescription = stringResource(id = item.displayNameRes)) },
                label = { Text(stringResource(id = item.displayNameRes)) },
                selected = isSelected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            // Corrected: Pop up to the Home route, which is the start destination of this graph.
                            // This avoids destroying the main navigation graph and correctly resets the stack.
                            popUpTo(BloomMindNavItems.Home.route) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when re-selecting the same item.
                            launchSingleTop = true
                            // Restore state when re-selecting a previously selected item.
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
