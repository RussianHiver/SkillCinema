package com.example.skillcinema.presentation

import com.example.skillcinema.data.MovieDataDTO

sealed interface FilmUiState {

    data object Initial : FilmUiState

    data class Loading(val movieId: Int) : FilmUiState

    data class Success(val movie: MovieDataDTO) : FilmUiState



}