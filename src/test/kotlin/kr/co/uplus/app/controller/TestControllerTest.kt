package kr.co.uplus.app.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestControllerTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun `should return Hello World when helloWorld is called`() {
        // When
        val response = restTemplate.getForEntity(
            "http://localhost:$port/hello",
            String::class.java
        )

        // Then
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body == "Hello World")
        
        println("[DEBUG_LOG] Response status: ${response.statusCode}")
        println("[DEBUG_LOG] Response body: ${response.body}")
    }
}