package kr.co.uplus.app.service

import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import io.mockk.verify
import kr.co.uplus.app.domain.PersonEntity
import kr.co.uplus.app.dto.request.CreatePersonRequest
import kr.co.uplus.app.dto.response.PersonResponse
import kr.co.uplus.app.dto.toEntity
import kr.co.uplus.app.dto.toResponse
import kr.co.uplus.app.repository.PersonRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import java.util.Optional
import java.util.concurrent.CompletableFuture

@ExtendWith(MockKExtension::class)
class PersonServiceImplTest {

    @MockK
    private lateinit var personRepository: PersonRepository

    @MockK
    private lateinit var kafkaTemplate: KafkaTemplate<String, String>

    @MockK
    private lateinit var objectMapper: ObjectMapper

    private lateinit var personService: PersonServiceImpl

    @BeforeEach
    fun setup() {
        personService = PersonServiceImpl(personRepository, kafkaTemplate, objectMapper)
    }

    @Test
    fun `createPerson should save person to repository and send to Kafka`() {
        // Given
        val request = CreatePersonRequest(
            name = "Test Person",
            age = 30
        )

        val savedEntity = PersonEntity(
            id = 1L,
            name = "Test Person",
            age = 30
        )

        val personResponse = PersonResponse(
            id = 1L,
            name = "Test Person",
            age = 30
        )

        val personJson = """{"id":1,"name":"Test Person","age":30}"""

        // Mock repository save
        every { personRepository.save(any()) } returns savedEntity

        // Mock toEntity and toResponse extension functions
        every { any<PersonEntity>().toResponse() } returns personResponse

        // Mock ObjectMapper
        every { objectMapper.writeValueAsString(any()) } returns personJson

        // Mock KafkaTemplate
        val future = CompletableFuture<SendResult<String, String>>()
        future.complete(null) // We don't need the actual result for this test
        every { kafkaTemplate.send(any(), any(), any()) } returns future

        // When
        val result = personService.createPerson(request)

        // Then
        assertEquals(personResponse, result)

        // Verify repository save was called
        verify(exactly = 1) { personRepository.save(any()) }

        // Verify Kafka send was called with correct topic and data
        verify(exactly = 1) { kafkaTemplate.send("person-topic", "1", personJson) }
    }

    @Test
    fun `getPersonById should return person when found`() {
        // Given
        val id = 1L
        val personEntity = PersonEntity(
            id = id,
            name = "Test Person",
            age = 30
        )

        val personResponse = PersonResponse(
            id = id,
            name = "Test Person",
            age = 30
        )

        every { personRepository.findById(id) } returns Optional.of(personEntity)
        every { personEntity.toResponse() } returns personResponse

        // When
        val result = personService.getPersonById(id)

        // Then
        assertEquals(200, result.statusCodeValue)
        assertEquals(personResponse, result.body)

        // Verify repository findById was called
        verify(exactly = 1) { personRepository.findById(id) }
    }
}
