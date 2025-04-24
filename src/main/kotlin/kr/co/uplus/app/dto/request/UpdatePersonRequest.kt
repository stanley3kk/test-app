package kr.co.uplus.app.dto.request

import jakarta.validation.constraints.Size

/**
 * DTO for updating an existing person.
 */
data class UpdatePersonRequest(
    @field:Size(min = 10, max = 20, message = "Name must be between 10 and 20 characters")
    val name: String,
    val age: Int
)
