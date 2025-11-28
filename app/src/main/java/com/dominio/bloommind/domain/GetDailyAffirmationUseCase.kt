package com.dominio.bloommind.domain

import com.dominio.bloommind.data.repository.AffirmationRepository
import com.dominio.bloommind.data.dto.AffirmationDto

class GetDailyAffirmationUseCase(private val repository: AffirmationRepository) {
    suspend operator fun invoke(): Result<AffirmationDto> {
        return repository.getDailyAffirmation()
    }
}
