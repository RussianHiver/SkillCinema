package com.example.skillcinema.domain

import com.example.skillcinema.data.MainRepository
import com.example.skillcinema.data.StaffListDTO
import com.example.skillcinema.data.StaffMemberDTO
import javax.inject.Inject

class MovieStaffUseCase (private val mainRepository: MainRepository) {

    suspend fun getMovieStaffList(id: Int): List<StaffListDTO> {
        return mainRepository.getMovieStaff(id = id)
    }

    suspend fun getIndividualStaffMember(id: Int): StaffMemberDTO {
        return mainRepository.getIndividualStaffMember(id = id)
    }

}