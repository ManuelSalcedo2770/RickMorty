package com.example.rickmorty.services

import com.example.rickmorty.models.RickAndMortyApiService
import com.example.rickmorty.models.Result
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterService {

    // Obtener una lista de personajes con paginación
    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): RickAndMortyApiService

    // Obtener un personaje específico por ID
    @GET("character/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): Result

}