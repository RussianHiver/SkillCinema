package com.example.skillcinema.domain

import com.example.skillcinema.data.MainRepository
import com.example.skillcinema.data.SeasonsAndEpisodesDTO
import javax.inject.Inject

class SeasonsAndEpisodesUseCase (private val mainRepository: MainRepository) {

    suspend fun getSeasonsAndEpisodes(id: Int): SeasonsAndEpisodesDTO {
        return mainRepository.getSeasonsAndEpisodes(id)
    }

}