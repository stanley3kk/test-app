package kr.co.uplus.app.dto.response

import kr.co.uplus.app.domain.PersonEntity

/**
 * DTO for returning person data.
 */
data class PersonResponse(
    val id: Long?,
    val name: String,
    val age: Int
) {
    companion object {
        /**
         * Convert a PersonEntity to a PersonResponse.
         */
        fun fromEntity(entity: PersonEntity): PersonResponse {
            return PersonResponse(
                id = entity.id,
                name = entity.name,
                age = entity.age
            )
        }
    }
}