package kr.co.uplus.app.controller

import kr.co.uplus.app.dto.request.CreatePersonRequest
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
class PersonControllerTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun `should return 400 when name is too short`() {
        // Given
        val request = CreatePersonRequest(
            name = "Short", // Less than 10 characters
            age = 25
        )
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
        val entity = HttpEntity(request, headers)

        // When
        val response = restTemplate.postForEntity(
            "http://localhost:$port/api/persons",
            entity,
            Map::class.java
        )

        // Then
        assert(response.statusCode == HttpStatus.BAD_REQUEST)
        val body = response.body as Map<*, *>
        assert(body["message"] == "Validation failed")
        assert((body["errors"] as Map<*, *>)["name"] != null)

        println("[DEBUG_LOG] Response: $body")
    }

    @Test
    fun `should return 400 when name is too long`() {
        // Given
        val request = CreatePersonRequest(
            name = "ThisNameIsTooLongForTheValidation", // More than 20 characters
            age = 25
        )
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
        val entity = HttpEntity(request, headers)

        // When
        val response = restTemplate.postForEntity(
            "http://localhost:$port/api/persons",
            entity,
            Map::class.java
        )

        // Then
        assert(response.statusCode == HttpStatus.BAD_REQUEST)
        val body = response.body as Map<*, *>
        assert(body["message"] == "Validation failed")
        assert((body["errors"] as Map<*, *>)["name"] != null)

        println("[DEBUG_LOG] Response: $body")
    }

    @Test
    fun `should accept request when name is valid`() {
        // Given
        val request = CreatePersonRequest(
            name = "ValidNameHere", // Between 10 and 20 characters
            age = 25
        )
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
        val entity = HttpEntity(request, headers)

        // When
        val response = restTemplate.postForEntity(
            "http://localhost:$port/api/persons",
            entity,
            Any::class.java
        )

        // Then
        assert(response.statusCode == HttpStatus.OK)

        println("[DEBUG_LOG] Response status: ${response.statusCode}")
    }

    @Test
    fun `should handle invalid sort property gracefully`() {
        // When
        val response = restTemplate.exchange(
            "http://localhost:$port/api/persons/page?sort=string,asc",
            HttpMethod.GET,
            null,
            Map::class.java
        )

        // Then
        assert(response.statusCode == HttpStatus.BAD_REQUEST)
        val body = response.body as Map<*, *>
        assert(body["message"] == "Invalid sort property")
        assert(body["error"] != null)

        println("[DEBUG_LOG] Response: $body")
    }
}
