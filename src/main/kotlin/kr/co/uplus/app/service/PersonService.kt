package kr.co.uplus.app.service

import kr.co.uplus.app.domain.PersonEntity
import kr.co.uplus.app.dto.request.CreatePersonRequest
import kr.co.uplus.app.dto.request.UpdatePersonRequest
import kr.co.uplus.app.dto.response.PersonResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.http.ResponseEntity

/**
 * Service interface for managing Person entities.
 */
interface PersonService {
    /**
     * Create a new person.
     *
     * @param request The person data to create
     * @return The created person response
     */
    fun createPerson(request: CreatePersonRequest): PersonResponse

    /**
     * Get all persons.
     *
     * @return List of all person responses
     */
    fun getAllPersons(): List<PersonResponse>

    /**
     * Get a person by ID.
     *
     * @param id The ID of the person to retrieve
     * @return ResponseEntity containing the person if found, or a not found response
     */
    fun getPersonById(id: Long): ResponseEntity<PersonResponse>

    /**
     * Update a person.
     *
     * @param id The ID of the person to update
     * @param request The updated person data
     * @return ResponseEntity containing the updated person if found, or a not found response
     */
    fun updatePerson(id: Long, request: UpdatePersonRequest): ResponseEntity<PersonResponse>

    /**
     * Delete a person.
     *
     * @param id The ID of the person to delete
     * @return ResponseEntity with no content if successful, or a not found response
     */
    fun deletePerson(id: Long): ResponseEntity<Void>

    /**
     * Find a person by name.
     *
     * @param name The name to search for
     * @return The person response with the given name
     */
    fun findByName(name: String): PersonResponse

    /**
     * Get persons with pagination.
     *
     * @param pageable The pagination information
     * @return Page of person responses
     */
    fun getPersonsWithPagination(pageable: Pageable): Page<PersonResponse>

    /**
     * Get persons with slicing.
     *
     * @param pageable The pagination information
     * @return Slice of person responses
     */
    fun getPersonsWithSlicing(pageable: Pageable): Slice<PersonResponse>

    /**
     * Find all persons with age greater than or equal to the specified value.
     *
     * @param age The minimum age to filter by
     * @return List of person responses with age >= the specified value
     */
    fun findByAgeGreaterThanEqual(age: Int): List<PersonResponse>
}
