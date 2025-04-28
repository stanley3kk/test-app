package kr.co.uplus.app.controller

import kr.co.uplus.app.dto.request.CreateFaqRequest
import kr.co.uplus.app.dto.request.UpdateFaqRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FaqControllerTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun `should return FAQs when getFaqs is called with valid OS type`() {
        // When
        val response = restTemplate.getForEntity(
            "http://localhost:$port/faq?os=iOS",
            Map::class.java
        )

        // Then
        assert(response.statusCode == HttpStatus.OK || response.statusCode == HttpStatus.NO_CONTENT)
        println("[DEBUG_LOG] Response status: ${response.statusCode}")
        println("[DEBUG_LOG] Response body: ${response.body}")
    }

    @Test
    fun `should return specific FAQ when getFaqs is called with valid ID and OS type`() {
        // First create a FAQ to ensure we have one to retrieve
        val createRequest = CreateFaqRequest(
            question = "Test Question",
            answer = "Test Answer",
            faqType = 1,
            faqCategory = "Test Category",
            isFavorite = false,
            osType = "iOS"
        )
        val createHeaders = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
        val createEntity = HttpEntity(createRequest, createHeaders)
        val createResponse = restTemplate.postForEntity(
            "http://localhost:$port/faq",
            createEntity,
            Map::class.java
        )
        
        // Get the ID of the created FAQ
        val createdFaqId = (createResponse.body as Map<*, *>)["id"] as Int
        
        // When
        val response = restTemplate.getForEntity(
            "http://localhost:$port/faq?id=$createdFaqId&os=iOS",
            Map::class.java
        )

        // Then
        assert(response.statusCode == HttpStatus.OK)
        println("[DEBUG_LOG] Response status: ${response.statusCode}")
        println("[DEBUG_LOG] Response body: ${response.body}")
    }

    @Test
    fun `should return 400 when getFaqs is called without OS type`() {
        // When
        val response = restTemplate.getForEntity(
            "http://localhost:$port/faq",
            Map::class.java
        )

        // Then
        assert(response.statusCode == HttpStatus.BAD_REQUEST)
        println("[DEBUG_LOG] Response status: ${response.statusCode}")
        println("[DEBUG_LOG] Response body: ${response.body}")
    }

    @Test
    fun `should create FAQ when createFaq is called with valid request`() {
        // Given
        val request = CreateFaqRequest(
            question = "Test Question",
            answer = "Test Answer",
            faqType = 1,
            faqCategory = "Test Category",
            isFavorite = false,
            osType = "iOS"
        )
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
        val entity = HttpEntity(request, headers)

        // When
        val response = restTemplate.postForEntity(
            "http://localhost:$port/faq",
            entity,
            Map::class.java
        )

        // Then
        assert(response.statusCode == HttpStatus.OK)
        val body = response.body as Map<*, *>
        assert(body["question"] == "Test Question")
        assert(body["answer"] == "Test Answer")
        
        println("[DEBUG_LOG] Response status: ${response.statusCode}")
        println("[DEBUG_LOG] Response body: ${response.body}")
    }

    @Test
    fun `should return 400 when createFaq is called with invalid request`() {
        // Given
        val request = CreateFaqRequest(
            question = "",  // Empty question should fail validation
            answer = "Test Answer",
            faqType = 1,
            faqCategory = "Test Category",
            isFavorite = false,
            osType = "iOS"
        )
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
        val entity = HttpEntity(request, headers)

        // When
        val response = restTemplate.postForEntity(
            "http://localhost:$port/faq",
            entity,
            Map::class.java
        )

        // Then
        assert(response.statusCode == HttpStatus.BAD_REQUEST)
        println("[DEBUG_LOG] Response status: ${response.statusCode}")
        println("[DEBUG_LOG] Response body: ${response.body}")
    }

    @Test
    fun `should update FAQ when updateFaq is called with valid request`() {
        // First create a FAQ to update
        val createRequest = CreateFaqRequest(
            question = "Original Question",
            answer = "Original Answer",
            faqType = 1,
            faqCategory = "Original Category",
            isFavorite = false,
            osType = "iOS"
        )
        val createHeaders = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
        val createEntity = HttpEntity(createRequest, createHeaders)
        val createResponse = restTemplate.postForEntity(
            "http://localhost:$port/faq",
            createEntity,
            Map::class.java
        )
        
        // Get the ID of the created FAQ
        val createdFaqId = (createResponse.body as Map<*, *>)["id"] as Int
        
        // Given
        val updateRequest = UpdateFaqRequest(
            question = "Updated Question",
            answer = "Updated Answer",
            faqType = 2,
            faqCategory = "Updated Category",
            isFavorite = true,
            osType = "iOS"
        )
        val updateHeaders = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
        val updateEntity = HttpEntity(updateRequest, updateHeaders)

        // When
        val response = restTemplate.exchange(
            "http://localhost:$port/faq/$createdFaqId",
            HttpMethod.PUT,
            updateEntity,
            Map::class.java
        )

        // Then
        assert(response.statusCode == HttpStatus.OK)
        val body = response.body as Map<*, *>
        assert(body["question"] == "Updated Question")
        assert(body["answer"] == "Updated Answer")
        
        println("[DEBUG_LOG] Response status: ${response.statusCode}")
        println("[DEBUG_LOG] Response body: ${response.body}")
    }

    @Test
    fun `should delete FAQ when deleteFaq is called with valid ID`() {
        // First create a FAQ to delete
        val createRequest = CreateFaqRequest(
            question = "Question to Delete",
            answer = "Answer to Delete",
            faqType = 1,
            faqCategory = "Category to Delete",
            isFavorite = false,
            osType = "iOS"
        )
        val createHeaders = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
        val createEntity = HttpEntity(createRequest, createHeaders)
        val createResponse = restTemplate.postForEntity(
            "http://localhost:$port/faq",
            createEntity,
            Map::class.java
        )
        
        // Get the ID of the created FAQ
        val createdFaqId = (createResponse.body as Map<*, *>)["id"] as Int
        
        // When
        val response = restTemplate.exchange(
            "http://localhost:$port/faq/$createdFaqId",
            HttpMethod.DELETE,
            null,
            Void::class.java
        )

        // Then
        assert(response.statusCode == HttpStatus.NO_CONTENT)
        println("[DEBUG_LOG] Response status: ${response.statusCode}")
    }
}