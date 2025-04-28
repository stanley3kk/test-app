package kr.co.uplus.app.dto.response

import kr.co.uplus.app.domain.FaqEntity
import java.io.Serializable
import java.time.LocalDateTime

/**
 * DTO for returning FAQ data.
 */
data class FaqResponse(
    val id: Long?,
    val question: String,
    val answer: String,
    val faqType: Int,
    val faqCategory: String,
    val isFavorite: Boolean,
    val imageUrl: String?,
    val appLink: String?,
    val startTime: LocalDateTime
) : Serializable {
    companion object {
        /**
         * Convert a FaqEntity to a FaqResponse.
         */
        fun fromEntity(entity: FaqEntity): FaqResponse {
            return FaqResponse(
                id = entity.id,
                question = entity.question,
                answer = entity.answer,
                faqType = entity.faqType,
                faqCategory = entity.faqCategory,
                isFavorite = entity.isFavorite,
                imageUrl = entity.imageUrl,
                appLink = entity.appLink,
                startTime = entity.startTime
            )
        }
    }
}