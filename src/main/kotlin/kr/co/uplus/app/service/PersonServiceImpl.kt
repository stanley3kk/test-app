package kr.co.uplus.app.service

import kr.co.uplus.app.domain.PersonEntity
import kr.co.uplus.app.dto.request.CreatePersonRequest
import kr.co.uplus.app.dto.request.UpdatePersonRequest
import kr.co.uplus.app.dto.response.PersonResponse
import kr.co.uplus.app.dto.toEntity
import kr.co.uplus.app.dto.toResponse
import kr.co.uplus.app.dto.applyUpdate
import kr.co.uplus.app.repository.PersonRepository
import kr.co.uplus.app.util.logger
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.cache.annotation.Cacheable
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.transaction.annotation.Transactional
import com.fasterxml.jackson.databind.ObjectMapper

/**
 * Implementation of the PersonService interface.
 */
@Service
class PersonServiceImpl(
    private val personRepository: PersonRepository,
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper
) : PersonService {

    @Transactional
    override fun createPerson(request: CreatePersonRequest): PersonResponse {
        logger.info("Creating new person: {}", request)
        val personEntity = request.toEntity()
        val savedEntity = personRepository.save(personEntity)
        val personResponse = savedEntity.toResponse()

        throw RuntimeException("test")
        val personJson = objectMapper.writeValueAsString(personResponse)
        kafkaTemplate.send("person-topic", personResponse.id.toString(), personJson)

        return personResponse
    }

    override fun getAllPersons(): List<PersonResponse> {
        logger.info("Retrieving all persons")
        return personRepository.findAll().map { it.toResponse() }
    }

    override fun getPersonById(id: Long): ResponseEntity<PersonResponse> {
        logger.info("Retrieving person with id: {}", id)
        val person = personRepository.findById(id)
        return if (person.isPresent) {
            ResponseEntity.ok(person.get().toResponse())
        } else {
            logger.warn("Person with id {} not found", id)
            ResponseEntity.notFound().build()
        }
    }

    override fun updatePerson(id: Long, request: UpdatePersonRequest): ResponseEntity<PersonResponse> {
        logger.info("Updating person with id: {}", id)
        return personRepository.findById(id).map {
            val updatedEntity = it.applyUpdate(request)
            val savedEntity = personRepository.save(updatedEntity)
            ResponseEntity.ok(savedEntity.toResponse())
        }.orElse(ResponseEntity.notFound().build())
    }

    override fun deletePerson(id: Long): ResponseEntity<Void> {
        logger.info("Deleting person with id: {}", id)
        val person = personRepository.findById(id)
        return if (person.isPresent) {
            personRepository.delete(person.get())
            ResponseEntity.noContent().build()
        } else {
            logger.warn("Person with id {} not found", id)
            ResponseEntity.notFound().build()
        }
    }

    override fun findByName(name: String): PersonResponse {
        logger.info("Finding person with name: {}", name)
        return personRepository.findByName(name).toResponse()
    }

    @Cacheable(value = ["personsWithPagination"], key = "#pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort")
    override fun getPersonsWithPagination(pageable: Pageable): Page<PersonResponse> {
        logger.info("Retrieving persons with pagination: {}", pageable)
        return personRepository.findAll(pageable).map { it.toResponse() }
    }

    override fun getPersonsWithSlicing(pageable: Pageable): Slice<PersonResponse> {
        logger.info("Retrieving persons with slicing: {}", pageable)
        return personRepository.findAll(pageable).map { it.toResponse() }
    }

    override fun findByAgeGreaterThanEqual(age: Int): List<PersonResponse> {
        logger.info("Finding persons with age >= {}", age)
        return personRepository.findByAgeGreaterThanEqual(age).map { it.toResponse() }
    }
}
