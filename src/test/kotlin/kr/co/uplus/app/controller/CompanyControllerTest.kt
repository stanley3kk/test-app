package kr.co.uplus.app.controller

import kr.co.uplus.app.dto.request.CreateCompanyRequest
import kr.co.uplus.app.dto.request.UpdateCompanyRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CompanyControllerTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun `should create company when createCompany is called with valid request`() {
        // Given
        val request = CreateCompanyRequest(
            name = "Test Company",
            address = "123 Test Street, Test City",
            phoneNumber = "02-1234-5678"
        )
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
        val entity = HttpEntity(request, headers)

        // When
        val response = restTemplate.postForEntity(
            "http://localhost:$port/api/companies",
            entity,
            Map::class.java
        )

        // Then
        assert(response.statusCode == HttpStatus.OK)
        val body = response.body as Map<*, *>
        assert(body["name"] == "Test Company")
        assert(body["address"] == "123 Test Street, Test City")
        assert(body["phoneNumber"] == "02-1234-5678")
        
        println("[DEBUG_LOG] Response status: ${response.statusCode}")
        println("[DEBUG_LOG] Response body: ${response.body}")
    }

    @Test
    fun `should return 400 when createCompany is called with invalid request`() {
        // Given
        val request = CreateCompanyRequest(
            name = "A", // Too short, should fail validation
            address = "123 Test Street, Test City",
            phoneNumber = "02-1234-5678"
        )
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
        val entity = HttpEntity(request, headers)

        // When
        val response = restTemplate.postForEntity(
            "http://localhost:$port/api/companies",
            entity,
            Map::class.java
        )

        // Then
        assert(response.statusCode == HttpStatus.BAD_REQUEST)
        println("[DEBUG_LOG] Response status: ${response.statusCode}")
        println("[DEBUG_LOG] Response body: ${response.body}")
    }

    @Test
    fun `should return all companies when getAllCompanies is called`() {
        // First create a company to ensure we have one to retrieve
        createTestCompany()
        
        // When
        val response = restTemplate.getForEntity(
            "http://localhost:$port/api/companies",
            List::class.java
        )

        // Then
        assert(response.statusCode == HttpStatus.OK)
        assert((response.body as List<*>).isNotEmpty())
        
        println("[DEBUG_LOG] Response status: ${response.statusCode}")
        println("[DEBUG_LOG] Response body: ${response.body}")
    }

    @Test
    fun `should return company by ID when getCompanyById is called with valid ID`() {
        // First create a company to retrieve
        val companyId = createTestCompany()
        
        // When
        val response = restTemplate.getForEntity(
            "http://localhost:$port/api/companies/$companyId",
            Map::class.java
        )

        // Then
        assert(response.statusCode == HttpStatus.OK)
        val body = response.body as Map<*, *>
        assert(body["id"] == companyId)
        assert(body["name"] == "Test Company")
        
        println("[DEBUG_LOG] Response status: ${response.statusCode}")
        println("[DEBUG_LOG] Response body: ${response.body}")
    }

    @Test
    fun `should return 404 when getCompanyById is called with invalid ID`() {
        // When
        val response = restTemplate.getForEntity(
            "http://localhost:$port/api/companies/999999", // Assuming this ID doesn't exist
            Map::class.java
        )

        // Then
        assert(response.statusCode == HttpStatus.NOT_FOUND)
        println("[DEBUG_LOG] Response status: ${response.statusCode}")
        println("[DEBUG_LOG] Response body: ${response.body}")
    }

    @Test
    fun `should update company when updateCompany is called with valid request`() {
        // First create a company to update
        val companyId = createTestCompany()
        
        // Given
        val request = UpdateCompanyRequest(
            name = "Updated Company",
            address = "456 Updated Street, Updated City",
            phoneNumber = "02-8765-4321"
        )
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
        val entity = HttpEntity(request, headers)

        // When
        val response = restTemplate.exchange(
            "http://localhost:$port/api/companies/$companyId",
            HttpMethod.PUT,
            entity,
            Map::class.java
        )

        // Then
        assert(response.statusCode == HttpStatus.OK)
        val body = response.body as Map<*, *>
        assert(body["id"] == companyId)
        assert(body["name"] == "Updated Company")
        assert(body["address"] == "456 Updated Street, Updated City")
        assert(body["phoneNumber"] == "02-8765-4321")
        
        println("[DEBUG_LOG] Response status: ${response.statusCode}")
        println("[DEBUG_LOG] Response body: ${response.body}")
    }

    @Test
    fun `should delete company when deleteCompany is called with valid ID`() {
        // First create a company to delete
        val companyId = createTestCompany()
        
        // When
        val response = restTemplate.exchange(
            "http://localhost:$port/api/companies/$companyId",
            HttpMethod.DELETE,
            null,
            Void::class.java
        )

        // Then
        assert(response.statusCode == HttpStatus.NO_CONTENT)
        println("[DEBUG_LOG] Response status: ${response.statusCode}")
        
        // Verify the company was deleted
        val getResponse = restTemplate.getForEntity(
            "http://localhost:$port/api/companies/$companyId",
            Map::class.java
        )
        assert(getResponse.statusCode == HttpStatus.NOT_FOUND)
    }

    @Test
    fun `should return paginated companies when getCompaniesWithPagination is called`() {
        // First create some companies to ensure we have data
        createTestCompany()
        createTestCompany()
        
        // When
        val response = restTemplate.getForEntity(
            "http://localhost:$port/api/companies/page?page=0&size=10",
            Map::class.java
        )

        // Then
        assert(response.statusCode == HttpStatus.OK)
        val body = response.body as Map<*, *>
        assert(body["content"] != null)
        assert((body["content"] as List<*>).isNotEmpty())
        
        println("[DEBUG_LOG] Response status: ${response.statusCode}")
        println("[DEBUG_LOG] Response body: ${response.body}")
    }

    @Test
    fun `should return sliced companies when getCompaniesWithSlicing is called`() {
        // First create some companies to ensure we have data
        createTestCompany()
        createTestCompany()
        
        // When
        val response = restTemplate.getForEntity(
            "http://localhost:$port/api/companies/slice?page=0&size=10",
            Map::class.java
        )

        // Then
        assert(response.statusCode == HttpStatus.OK)
        val body = response.body as Map<*, *>
        assert(body["content"] != null)
        assert((body["content"] as List<*>).isNotEmpty())
        
        println("[DEBUG_LOG] Response status: ${response.statusCode}")
        println("[DEBUG_LOG] Response body: ${response.body}")
    }

    // Helper method to create a test company and return its ID
    private fun createTestCompany(): Int {
        val request = CreateCompanyRequest(
            name = "Test Company",
            address = "123 Test Street, Test City",
            phoneNumber = "02-1234-5678"
        )
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
        val entity = HttpEntity(request, headers)
        val response = restTemplate.postForEntity(
            "http://localhost:$port/api/companies",
            entity,
            Map::class.java
        )
        return (response.body as Map<*, *>)["id"] as Int
    }
}