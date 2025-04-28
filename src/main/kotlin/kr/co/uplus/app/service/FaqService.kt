package kr.co.uplus.app.service

import kr.co.uplus.app.dto.request.CreateFaqRequest
import kr.co.uplus.app.dto.request.UpdateFaqRequest
import kr.co.uplus.app.dto.response.FaqResponse
import org.springframework.http.ResponseEntity

/**
 * Service interface for managing FAQ entities.
 */
interface FaqService {
    /**
     * Create a new FAQ.
     *
     * @param request The FAQ data to create
     * @return The created FAQ response
     */
    fun createFaq(request: CreateFaqRequest): FaqResponse

    /**
     * Get all FAQs for a specific OS type.
     *
     * @param osType The OS type to filter by
     * @return List of all FAQ responses for the specified OS type
     */
    fun getAllFaqsByOsType(osType: String): ResponseEntity<Any>

    /**
     * Get a FAQ by ID and OS type.
     *
     * @param id The ID of the FAQ to retrieve
     * @param osType The OS type
     * @return ResponseEntity containing the FAQ if found, or a not found response
     */
    fun getFaqByIdAndOsType(id: Long?, osType: String): ResponseEntity<Any>

    /**
     * Update a FAQ.
     *
     * @param id The ID of the FAQ to update
     * @param request The updated FAQ data
     * @return ResponseEntity containing the updated FAQ if found, or a not found response
     */
    fun updateFaq(id: Long, request: UpdateFaqRequest): ResponseEntity<FaqResponse>

    /**
     * Delete a FAQ.
     *
     * @param id The ID of the FAQ to delete
     * @return ResponseEntity with no content if successful, or a not found response
     */
    fun deleteFaq(id: Long): ResponseEntity<Void>
}