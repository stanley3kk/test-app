package kr.co.uplus.app

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.parameters.RequestBody as RequestBodyAnnotation


@RestController
@RequestMapping("/api/persons")
class PersonController(
    private val personRepository: PersonRepository
) {

    // Create
    @PostMapping
    @Operation(summary = "Create new person")
    @RequestBodyAnnotation(
        required = true,
        content = [Content(
            mediaType = "application/json",
            examples = [ExampleObject(
                name = "CreatePersonExample",
                summary = "A sample person creation request",
                value = """
                    {
                      "name": "홍길동",
                      "age": 25
                    }
                """
            )]
        )]
    )
    fun createPerson(@RequestBody personEntity: PersonEntity): PersonEntity {
        return personRepository.save(personEntity)
    }

    // Read All
    @GetMapping
    fun getAllPersons(): List<PersonEntity> {
        return personRepository.findAll()
    }

    // Read One
    @GetMapping("/{id}")
    fun getPersonById(@PathVariable id: Long): ResponseEntity<PersonEntity> {
        val person = personRepository.findById(id)
        return if (person.isPresent) {
            ResponseEntity.ok(person.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    // Update
    @PutMapping("/{id}")
    fun updatePerson(@PathVariable id: Long, @RequestBody updated: PersonEntity): ResponseEntity<PersonEntity> {
        return personRepository.findById(id).map {
            val newPerson = it.copy(name = updated.name, age = updated.age)
            ResponseEntity.ok(personRepository.save(newPerson))
        }.orElse(ResponseEntity.notFound().build())
    }

    // Delete
    @DeleteMapping("/{id}")
    fun deletePerson(@PathVariable id: Long): ResponseEntity<Void> {
        val person = personRepository.findById(id)
        return if (person.isPresent) {
            personRepository.delete(person.get())
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
