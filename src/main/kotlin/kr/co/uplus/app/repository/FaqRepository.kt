package kr.co.uplus.app.repository

import kr.co.uplus.app.domain.FaqEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repository for FAQ entities.
 */
@Repository
interface FaqRepository : JpaRepository<FaqEntity, Long> {
    /**
     * Find all FAQs by OS type.
     *
     * @param osType The OS type to filter by
     * @return List of FAQs for the specified OS type
     */
    fun findByOsType(osType: String): List<FaqEntity>
    
    /**
     * Find a specific FAQ by ID and OS type.
     *
     * @param id The FAQ ID
     * @param osType The OS type
     * @return The FAQ entity if found, or null
     */
    fun findByIdAndOsType(id: Long, osType: String): FaqEntity?
}