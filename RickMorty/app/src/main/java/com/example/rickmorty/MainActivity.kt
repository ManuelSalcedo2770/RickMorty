package com.example.rickmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rickmorty.ui.screens.HomeScreen
import com.example.rickmorty.ui.screens.CharacterDetailScreen
import com.example.rickmorty.ui.theme.RickMortyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RickMortyTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(navController = navController, startDestination = "home") {
                        composable(route = "home") {
                            HomeScreen(innerPadding, navController)
                        }
                        composable(
                            route = "character_detail/{id}",
                            arguments = listOf(
                                navArgument("id") {
                                    type = NavType.IntType
                                    nullable = false
                                }
                            )
                        ) {
                            val id = it.arguments?.getInt("id") ?: 0
                            CharacterDetailScreen(id = id, innerPaddingValues = innerPadding)
                        }
                    }
                }
            }
        }
    }
}