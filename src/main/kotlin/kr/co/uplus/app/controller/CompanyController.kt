package kr.co.uplus.app.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.parameters.RequestBody as RequestBodyMapping
import jakarta.validation.Valid
import kr.co.uplus.app.dto.request.CreateCompanyRequest
import kr.co.uplus.app.dto.request.UpdateCompanyRequest
import kr.co.uplus.app.dto.response.CompanyResponse
import kr.co.uplus.app.service.CompanyService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/companies")
class CompanyController(
    private val companyService: CompanyService
) {

    // Create
    @PostMapping
    @Operation(summary = "Create new company")
    @RequestBodyMapping(
        required = true,
        content = [Content(
            mediaType = "application/json",
            examples = [ExampleObject(
                name = "CreateCompanyExample",
                summary = "A sample company creation request",
                value = """
                    {
                      "name": "ABC Corporation",
                      "address": "123 Business Street, Seoul",
                      "phoneNumber": "02-1234-5678"
                    }
                """
            )]
        )]
    )
    fun createCompany(@Valid @RequestBody request: CreateCompanyRequest): CompanyResponse {
        return companyService.createCompany(request)
    }

    // Read All
    @GetMapping
    fun getAllCompanies(): List<CompanyResponse> {
        return companyService.getAllCompanies()
    }

    // Read One
    @GetMapping("/{id}")
    fun getCompanyById(@PathVariable id: Long): ResponseEntity<CompanyResponse> {
        return companyService.getCompanyById(id)
    }

    // Update
    @PutMapping("/{id}")
    fun updateCompany(@PathVariable id: Long, @Valid @RequestBody request: UpdateCompanyRequest): ResponseEntity<CompanyResponse> {
        return companyService.updateCompany(id, request)
    }

    // Delete
    @DeleteMapping("/{id}")
    fun deleteCompany(@PathVariable id: Long): ResponseEntity<Void> {
        return companyService.deleteCompany(id)
    }

    // Pagination
    @GetMapping("/page")
    @Operation(summary = "Get companies with pagination")
    fun getCompaniesWithPagination(@PageableDefault(size = 10) pageable: Pageable): Page<CompanyResponse> {
        return companyService.getCompaniesWithPagination(pageable)
    }

    // Slicing
    @GetMapping("/slice")
    @Operation(summary = "Get companies with slicing")
    fun getCompaniesWithSlicing(@PageableDefault(size = 10) pageable: Pageable): Slice<CompanyResponse> {
        return companyService.getCompaniesWithSlicing(pageable)
    }
}