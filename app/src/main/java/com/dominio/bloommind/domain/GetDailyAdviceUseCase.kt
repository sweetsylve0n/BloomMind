package com.dominio.bloommind.domain

import com.dominio.bloommind.data.repository.AdviceRepository
import com.dominio.bloommind.data.dto.AdviceDto

class GetDailyAdviceUseCase(private val repository: AdviceRepository) {
    suspend operator fun invoke(): Result<AdviceDto> {
        return repository.getDailyAdvice()
    }
}
