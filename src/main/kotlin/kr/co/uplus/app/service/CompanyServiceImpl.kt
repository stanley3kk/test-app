package kr.co.uplus.app.service

import kr.co.uplus.app.domain.CompanyEntity
import kr.co.uplus.app.dto.request.CreateCompanyRequest
import kr.co.uplus.app.dto.request.UpdateCompanyRequest
import kr.co.uplus.app.dto.response.CompanyResponse
import kr.co.uplus.app.dto.toEntity
import kr.co.uplus.app.dto.toResponse
import kr.co.uplus.app.dto.applyUpdate
import kr.co.uplus.app.repository.CompanyRepository
import kr.co.uplus.app.util.logger
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

/**
 * Implementation of the CompanyService interface.
 */
@Service
class CompanyServiceImpl(
    private val companyRepository: CompanyRepository
) : CompanyService {

    override fun createCompany(request: CreateCompanyRequest): CompanyResponse {
        logger.info("Creating new company: {}", request)
        val companyEntity = request.toEntity()
        val savedEntity = companyRepository.save(companyEntity)
        return savedEntity.toResponse()
    }

    override fun getAllCompanies(): List<CompanyResponse> {
        logger.info("Retrieving all companies")
        return companyRepository.findAll().map { it.toResponse() }
    }

    override fun getCompanyById(id: Long): ResponseEntity<CompanyResponse> {
        logger.info("Retrieving company with id: {}", id)
        val company = companyRepository.findById(id)
        return if (company.isPresent) {
            ResponseEntity.ok(company.get().toResponse())
        } else {
            logger.warn("Company with id {} not found", id)
            ResponseEntity.notFound().build()
        }
    }

    override fun updateCompany(id: Long, request: UpdateCompanyRequest): ResponseEntity<CompanyResponse> {
        logger.info("Updating company with id: {}", id)
        return companyRepository.findById(id).map {
            val updatedEntity = it.applyUpdate(request)
            val savedEntity = companyRepository.save(updatedEntity)
            ResponseEntity.ok(savedEntity.toResponse())
        }.orElse(ResponseEntity.notFound().build())
    }

    override fun deleteCompany(id: Long): ResponseEntity<Void> {
        logger.info("Deleting company with id: {}", id)
        val company = companyRepository.findById(id)
        return if (company.isPresent) {
            companyRepository.delete(company.get())
            ResponseEntity.noContent().build()
        } else {
            logger.warn("Company with id {} not found", id)
            ResponseEntity.notFound().build()
        }
    }

    override fun findByName(name: String): CompanyResponse {
        logger.info("Finding company with name: {}", name)
        return companyRepository.findByName(name).toResponse()
    }

    override fun getCompaniesWithPagination(pageable: Pageable): Page<CompanyResponse> {
        logger.info("Retrieving companies with pagination: {}", pageable)
        return companyRepository.findAll(pageable).map { it.toResponse() }
    }

    override fun getCompaniesWithSlicing(pageable: Pageable): Slice<CompanyResponse> {
        logger.info("Retrieving companies with slicing: {}", pageable)
        return companyRepository.findAll(pageable).map { it.toResponse() }
    }
}