package kr.co.uplus.app.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

/**
 * DTO for creating a new FAQ.
 */
data class CreateFaqRequest(
    @field:NotBlank(message = "Question is required")
    val question: String,

    @field:NotBlank(message = "Answer is required")
    val answer: String,

    @field:NotNull(message = "FAQ type is required")
    val faqType: Int,

    @field:NotBlank(message = "FAQ category is required")
    val faqCategory: String,

    val isFavorite: Boolean = false,

    val imageUrl: String? = null,

    val appLink: String? = null,

    @field:NotBlank(message = "OS type is required")
    val osType: String
)