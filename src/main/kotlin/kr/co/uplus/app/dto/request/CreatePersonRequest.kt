package kr.co.uplus.app.dto.request

import jakarta.validation.constraints.Size

/**
 * DTO for creating a new person.
 */
data class CreatePersonRequest(
    @field:Size(min = 1, max = 20, message = "Name must be between 10 and 20 characters")
    val name: String,
    val age: Int
)
