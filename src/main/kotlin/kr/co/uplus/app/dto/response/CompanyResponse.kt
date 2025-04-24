package kr.co.uplus.app.dto.response

import kr.co.uplus.app.domain.CompanyEntity

/**
 * DTO for returning company data.
 */
data class CompanyResponse(
    val id: Long?,
    val name: String,
    val address: String,
    val phoneNumber: String
) {
    companion object {
        /**
         * Convert a CompanyEntity to a CompanyResponse.
         */
        fun fromEntity(entity: CompanyEntity): CompanyResponse {
            return CompanyResponse(
                id = entity.id,
                name = entity.name,
                address = entity.address,
                phoneNumber = entity.phoneNumber
            )
        }
    }
}