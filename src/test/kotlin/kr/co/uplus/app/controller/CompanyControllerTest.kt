package kr.co.uplus.app.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kr.co.uplus.app.dto.request.CreateCompanyRequest
import kr.co.uplus.app.dto.request.UpdateCompanyRequest
import kr.co.uplus.app.dto.response.CompanyResponse
import kr.co.uplus.app.service.CompanyService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.SliceImpl
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ExtendWith(MockKExtension::class)
class CompanyControllerTest {

    private lateinit var mockMvc: MockMvc

    @MockK
    private lateinit var companyService: CompanyService

    private lateinit var companyController: CompanyController

    private val objectMapper = ObjectMapper()

    @BeforeEach
    fun setup() {
        companyController = CompanyController(companyService)
        mockMvc = MockMvcBuilders.standaloneSetup(companyController)
            .setCustomArgumentResolvers(PageableHandlerMethodArgumentResolver())
            .build()
    }

    @Test
    fun `should create company when createCompany is called with valid request`() {
        // Given
        val request = CreateCompanyRequest(
            name = "Test Company",
            address = "123 Test Street, Test City",
            phoneNumber = "02-1234-5678"
        )

        val expectedResponse = CompanyResponse(
            id = 1L,
            name = "Test Company",
            address = "123 Test Street, Test City",
            phoneNumber = "02-1234-5678"
        )

        every { companyService.createCompany(any()) } returns expectedResponse

        // When & Then
        mockMvc.perform(
            post("/api/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Test Company"))
            .andExpect(jsonPath("$.address").value("123 Test Street, Test City"))
            .andExpect(jsonPath("$.phoneNumber").value("02-1234-5678"))

        // Verify
        verify(exactly = 1) { companyService.createCompany(request) }
    }

    @Test
    fun `should return all companies when getAllCompanies is called`() {
        // Given
        val companies = listOf(
            CompanyResponse(
                id = 1L,
                name = "Test Company 1",
                address = "123 Test Street, Test City",
                phoneNumber = "02-1234-5678"
            ),
            CompanyResponse(
                id = 2L,
                name = "Test Company 2",
                address = "456 Test Avenue, Test Town",
                phoneNumber = "02-8765-4321"
            )
        )

        every { companyService.getAllCompanies() } returns companies

        // When & Then
        mockMvc.perform(get("/api/companies"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("Test Company 1"))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].name").value("Test Company 2"))

        // Verify
        verify(exactly = 1) { companyService.getAllCompanies() }
    }

    @Test
    fun `should return company by ID when getCompanyById is called with valid ID`() {
        // Given
        val companyId = 1L
        val company = CompanyResponse(
            id = companyId,
            name = "Test Company",
            address = "123 Test Street, Test City",
            phoneNumber = "02-1234-5678"
        )

        every { companyService.getCompanyById(companyId) } returns ResponseEntity.ok(company)

        // When & Then
        mockMvc.perform(get("/api/companies/$companyId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(companyId))
            .andExpect(jsonPath("$.name").value("Test Company"))
            .andExpect(jsonPath("$.address").value("123 Test Street, Test City"))
            .andExpect(jsonPath("$.phoneNumber").value("02-1234-5678"))

        // Verify
        verify(exactly = 1) { companyService.getCompanyById(companyId) }
    }

    @Test
    fun `should return 404 when getCompanyById is called with invalid ID`() {
        // Given
        val companyId = 999L

        every { companyService.getCompanyById(companyId) } returns ResponseEntity.notFound().build()

        // When & Then
        mockMvc.perform(get("/api/companies/$companyId"))
            .andExpect(status().isNotFound)

        // Verify
        verify(exactly = 1) { companyService.getCompanyById(companyId) }
    }

    @Test
    fun `should update company when updateCompany is called with valid request`() {
        // Given
        val companyId = 1L
        val request = UpdateCompanyRequest(
            name = "Updated Company",
            address = "456 Updated Street, Updated City",
            phoneNumber = "02-8765-4321"
        )

        val updatedCompany = CompanyResponse(
            id = companyId,
            name = "Updated Company",
            address = "456 Updated Street, Updated City",
            phoneNumber = "02-8765-4321"
        )

        every { companyService.updateCompany(companyId, any()) } returns ResponseEntity.ok(updatedCompany)

        // When & Then
        mockMvc.perform(
            put("/api/companies/$companyId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(companyId))
            .andExpect(jsonPath("$.name").value("Updated Company"))
            .andExpect(jsonPath("$.address").value("456 Updated Street, Updated City"))
            .andExpect(jsonPath("$.phoneNumber").value("02-8765-4321"))

        // Verify
        verify(exactly = 1) { companyService.updateCompany(companyId, request) }
    }

    @Test
    fun `should delete company when deleteCompany is called with valid ID`() {
        // Given
        val companyId = 1L

        every { companyService.deleteCompany(companyId) } returns ResponseEntity.noContent().build()

        // When & Then
        mockMvc.perform(delete("/api/companies/$companyId"))
            .andExpect(status().isNoContent)

        // Verify
        verify(exactly = 1) { companyService.deleteCompany(companyId) }
    }

    @Test
    fun `should return paginated companies when getCompaniesWithPagination is called`() {
        // Given
        val pageable = PageRequest.of(0, 10)
        val companies = listOf(
            CompanyResponse(
                id = 1L,
                name = "Test Company 1",
                address = "123 Test Street, Test City",
                phoneNumber = "02-1234-5678"
            ),
            CompanyResponse(
                id = 2L,
                name = "Test Company 2",
                address = "456 Test Avenue, Test Town",
                phoneNumber = "02-8765-4321"
            )
        )

        val page = PageImpl(companies, pageable, companies.size.toLong())

        every { companyService.getCompaniesWithPagination(any()) } returns page

        // When & Then
        mockMvc.perform(get("/api/companies/page?page=0&size=10"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].id").value(1))
            .andExpect(jsonPath("$.content[0].name").value("Test Company 1"))
            .andExpect(jsonPath("$.content[1].id").value(2))
            .andExpect(jsonPath("$.content[1].name").value("Test Company 2"))
            .andExpect(jsonPath("$.totalElements").value(2))

        // Verify
        verify { companyService.getCompaniesWithPagination(any()) }
    }

    @Test
    fun `should return sliced companies when getCompaniesWithSlicing is called`() {
        // Given
        val pageable = PageRequest.of(0, 10)
        val companies = listOf(
            CompanyResponse(
                id = 1L,
                name = "Test Company 1",
                address = "123 Test Street, Test City",
                phoneNumber = "02-1234-5678"
            ),
            CompanyResponse(
                id = 2L,
                name = "Test Company 2",
                address = "456 Test Avenue, Test Town",
                phoneNumber = "02-8765-4321"
            )
        )

        val slice = SliceImpl(companies, pageable, false)

        every { companyService.getCompaniesWithSlicing(any()) } returns slice

        // When & Then
        mockMvc.perform(get("/api/companies/slice?page=0&size=10"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].id").value(1))
            .andExpect(jsonPath("$.content[0].name").value("Test Company 1"))
            .andExpect(jsonPath("$.content[1].id").value(2))
            .andExpect(jsonPath("$.content[1].name").value("Test Company 2"))

        // Verify
        verify { companyService.getCompaniesWithSlicing(any()) }
    }
}
