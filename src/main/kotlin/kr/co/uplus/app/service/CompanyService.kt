package kr.co.uplus.app.service

import kr.co.uplus.app.dto.request.CreateCompanyRequest
import kr.co.uplus.app.dto.request.UpdateCompanyRequest
import kr.co.uplus.app.dto.response.CompanyResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.http.ResponseEntity

/**
 * Service interface for managing Company entities.
 */
interface CompanyService {
    /**
     * Create a new company.
     *
     * @param request The company data to create
     * @return The created company response
     */
    fun createCompany(request: CreateCompanyRequest): CompanyResponse

    /**
     * Get all companies.
     *
     * @return List of all company responses
     */
    fun getAllCompanies(): List<CompanyResponse>

    /**
     * Get a company by ID.
     *
     * @param id The ID of the company to retrieve
     * @return ResponseEntity containing the company if found, or a not found response
     */
    fun getCompanyById(id: Long): ResponseEntity<CompanyResponse>

    /**
     * Update a company.
     *
     * @param id The ID of the company to update
     * @param request The updated company data
     * @return ResponseEntity containing the updated company if found, or a not found response
     */
    fun updateCompany(id: Long, request: UpdateCompanyRequest): ResponseEntity<CompanyResponse>

    /**
     * Delete a company.
     *
     * @param id The ID of the company to delete
     * @return ResponseEntity with no content if successful, or a not found response
     */
    fun deleteCompany(id: Long): ResponseEntity<Void>

    /**
     * Find a company by name.
     *
     * @param name The name to search for
     * @return The company response with the given name
     */
    fun findByName(name: String): CompanyResponse

    /**
     * Get companies with pagination.
     *
     * @param pageable The pagination information
     * @return Page of company responses
     */
    fun getCompaniesWithPagination(pageable: Pageable): Page<CompanyResponse>

    /**
     * Get companies with slicing.
     *
     * @param pageable The pagination information
     * @return Slice of company responses
     */
    fun getCompaniesWithSlicing(pageable: Pageable): Slice<CompanyResponse>
}