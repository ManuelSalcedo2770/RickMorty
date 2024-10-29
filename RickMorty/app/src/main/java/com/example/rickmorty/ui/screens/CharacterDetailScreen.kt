package com.example.rickmorty.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.rickmorty.models.Result
import com.example.rickmorty.services.CharacterService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun CharacterDetailScreen(id: Int, innerPaddingValues: PaddingValues) {
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var character by remember { mutableStateOf<Result?>(null) }

    LaunchedEffect(key1 = id) {
        scope.launch {
            val BASE_URL = "https://rickandmortyapi.com/api/"
            val characterService = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CharacterService::class.java)

            isLoading = true
            character = characterService.getCharacterById(id)
            isLoading = false
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .padding(innerPaddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        character?.let {
            Column(
                modifier = Modifier
                    .padding(innerPaddingValues)
                    .fillMaxSize()
            ) {
                // Card con diseño galáctico
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp), // Agrega bordes redondeados
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent // Haz el fondo del Card transparente
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF800080), Color(0xFF000000)),
                                    tileMode = TileMode.Clamp
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Column {
                            AsyncImage(
                                model = it.image,
                                contentDescription = it.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .padding(bottom = 16.dp)
                            )
                            Text(text = "Name: ${it.name}", color = Color.White, textAlign = TextAlign.Center)
                            Text(text = "Species: ${it.species}", color = Color.White, textAlign = TextAlign.Center)
                            Text(text = "Status: ${it.status}", color = Color.White, textAlign = TextAlign.Center)
                            Text(text = "Gender: ${it.gender}", color = Color.White, textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }
    }
}