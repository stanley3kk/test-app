package kr.co.uplus.app.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.io.Serializable
import java.time.LocalDateTime

/**
 * Entity representing a FAQ item.
 */
@Entity
@Table(name = "faq")
data class FaqEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var question: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    var answer: String,

    @Column(nullable = false)
    var faqType: Int,

    @Column(nullable = false)
    var faqCategory: String,

    @Column(nullable = false)
    var isFavorite: Boolean = false,

    @Column
    var imageUrl: String? = null,

    @Column
    var appLink: String? = null,

    @Column(nullable = false)
    var startTime: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var osType: String
) : Serializable