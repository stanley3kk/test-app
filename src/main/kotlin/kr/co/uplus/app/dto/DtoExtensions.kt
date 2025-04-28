package kr.co.uplus.app.dto

import kr.co.uplus.app.domain.PersonEntity
import kr.co.uplus.app.domain.CompanyEntity
import kr.co.uplus.app.domain.FaqEntity
import kr.co.uplus.app.dto.request.CreatePersonRequest
import kr.co.uplus.app.dto.request.UpdatePersonRequest
import kr.co.uplus.app.dto.request.CreateCompanyRequest
import kr.co.uplus.app.dto.request.UpdateCompanyRequest
import kr.co.uplus.app.dto.request.CreateFaqRequest
import kr.co.uplus.app.dto.request.UpdateFaqRequest
import kr.co.uplus.app.dto.response.PersonResponse
import kr.co.uplus.app.dto.response.CompanyResponse
import kr.co.uplus.app.dto.response.FaqResponse
import java.time.LocalDateTime

/**
 * Extension functions for converting between DTOs and entities.
 */

/**
 * Convert a CreatePersonRequest to a PersonEntity.
 */
fun CreatePersonRequest.toEntity(): PersonEntity {
    return PersonEntity(
        name = this.name,
        age = this.age
    )
}

/**
 * Convert a PersonEntity to a PersonResponse.
 */
fun PersonEntity.toResponse(): PersonResponse {
    return PersonResponse(
        id = this.id,
        name = this.name,
        age = this.age
    )
}

/**
 * Apply the updates from an UpdatePersonRequest to a PersonEntity.
 */
fun PersonEntity.applyUpdate(request: UpdatePersonRequest): PersonEntity {
    return this.copy(
        name = request.name,
        age = request.age
    )
}

/**
 * Convert a CreateCompanyRequest to a CompanyEntity.
 */
fun CreateCompanyRequest.toEntity(): CompanyEntity {
    return CompanyEntity(
        name = this.name,
        address = this.address,
        phoneNumber = this.phoneNumber
    )
}

/**
 * Convert a CompanyEntity to a CompanyResponse.
 */
fun CompanyEntity.toResponse(): CompanyResponse {
    return CompanyResponse(
        id = this.id,
        name = this.name,
        address = this.address,
        phoneNumber = this.phoneNumber
    )
}

/**
 * Apply the updates from an UpdateCompanyRequest to a CompanyEntity.
 */
fun CompanyEntity.applyUpdate(request: UpdateCompanyRequest): CompanyEntity {
    return this.copy(
        name = request.name,
        address = request.address,
        phoneNumber = request.phoneNumber
    )
}

/**
 * Convert a CreateFaqRequest to a FaqEntity.
 */
fun CreateFaqRequest.toEntity(): FaqEntity {
    return FaqEntity(
        question = this.question,
        answer = this.answer,
        faqType = this.faqType,
        faqCategory = this.faqCategory,
        isFavorite = this.isFavorite,
        imageUrl = this.imageUrl,
        appLink = this.appLink,
        startTime = LocalDateTime.now(),
        osType = this.osType
    )
}

/**
 * Convert a FaqEntity to a FaqResponse.
 */
fun FaqEntity.toResponse(): FaqResponse {
    return FaqResponse(
        id = this.id,
        question = this.question,
        answer = this.answer,
        faqType = this.faqType,
        faqCategory = this.faqCategory,
        isFavorite = this.isFavorite,
        imageUrl = this.imageUrl,
        appLink = this.appLink,
        startTime = this.startTime
    )
}

/**
 * Apply the updates from an UpdateFaqRequest to a FaqEntity.
 */
fun FaqEntity.applyUpdate(request: UpdateFaqRequest): FaqEntity {
    return this.copy(
        question = request.question,
        answer = request.answer,
        faqType = request.faqType,
        faqCategory = request.faqCategory,
        isFavorite = request.isFavorite,
        imageUrl = request.imageUrl,
        appLink = request.appLink,
        osType = request.osType
    )
}
