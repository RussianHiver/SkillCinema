package com.example.skillcinema.domain

import com.example.skillcinema.data.GenresAndCountriesDTO
import com.example.skillcinema.data.MainRepository
import javax.inject.Inject

class FiltersUseCase (private val mainRepository: MainRepository) {

    suspend fun getFilters(): GenresAndCountriesDTO {
        return mainRepository.getFilters()
    }

}