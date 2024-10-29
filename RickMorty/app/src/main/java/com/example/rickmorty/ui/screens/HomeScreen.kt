package com.example.rickmorty.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.rickmorty.models.Result
import com.example.rickmorty.services.CharacterService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(innerPadding: PaddingValues, navController: NavController) {
    val scope = rememberCoroutineScope()
    var characters by remember { mutableStateOf(listOf<Result>()) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        scope.launch {
            try {
                isLoading = true
                val BASE_URL = "https://rickandmortyapi.com/api/"
                val characterService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(CharacterService::class.java)

                // Se obtiene la lista de personajes
                val response = characterService.getCharacters(1)  // 1 es el número de página
                characters = response.results
                Log.i("HomeScreenResponse", characters.toString())
                isLoading = false
            } catch (e: Exception) {
                characters = listOf()
                isLoading = false
            }
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Agrega un item para el header con la foto del título, ocupando 2 columnas
            item(span = { GridItemSpan(2) }) {
                Image(
                    painter = rememberAsyncImagePainter("https://cms.rhinoshield.app/public/images/ip_page_rick_and_morty_banner_mobile_de62ff184b.jpg"),
                    contentDescription = "Rick and Morty Banner",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp) // Ajusta la altura según sea necesario
                )
            }

            // Items para los personajes
            items(characters) { character ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(5.dp)
                        .clickable {
                            navController.navigate("character_detail/${character.id}")
                        },
                    // Agrega un fondo galáctico
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent // Haz el fondo del Card transparente
                    )
                ) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background( // Agrega un fondo degradado
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF800080),
                                    Color(0xFF000000)
                                ), // Colores del degradado
                                tileMode = TileMode.Clamp // Modo de repetición del degradado
                            )
                        )
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            AsyncImage(
                                model = character.image,
                                contentDescription = character.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp) // Ajusta la altura según sea necesario
                            )
                            Text(
                                text = character.name,
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.White // Cambia el color del texto a blanco
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
