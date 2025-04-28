package kr.co.uplus.app.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.parameters.RequestBody as RequestBodyMapping
import jakarta.validation.Valid
import kr.co.uplus.app.dto.request.CreatePersonRequest
import kr.co.uplus.app.dto.request.UpdatePersonRequest
import kr.co.uplus.app.dto.response.PersonResponse
import kr.co.uplus.app.service.PersonService
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
@RequestMapping("/api/persons")
class PersonController(
    private val personService: PersonService
) {

    // Create
    @PostMapping
    @Operation(summary = "Create new person")
    @RequestBodyMapping(
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
    fun createPerson(@Valid @RequestBody request: CreatePersonRequest): PersonResponse {
        return personService.createPerson(request)
    }

    // Read All
    @GetMapping
    fun getAllPersons(): List<PersonResponse> {
        return personService.getAllPersons()
    }

    // Read One
    @GetMapping("/{id}")
    fun getPersonById(@PathVariable id: Long): ResponseEntity<PersonResponse> {
        return personService.getPersonById(id)
    }

    // Update
    @PutMapping("/{id}")
    fun updatePerson(@PathVariable id: Long, @Valid @RequestBody request: UpdatePersonRequest): ResponseEntity<PersonResponse> {
        return personService.updatePerson(id, request)
    }

    // Delete
    @DeleteMapping("/{id}")
    fun deletePerson(@PathVariable id: Long): ResponseEntity<Void> {
        return personService.deletePerson(id)
    }

    // Pagination
    @GetMapping("/page")
    @Operation(summary = "Get persons with pagination")
    fun getPersonsWithPagination(@PageableDefault(size = 10) pageable: Pageable): Page<PersonResponse> {
        return personService.getPersonsWithPagination(pageable)
    }

    // Slicing
    @GetMapping("/slice")
    @Operation(summary = "Get persons with slicing")
    fun getPersonsWithSlicing(@PageableDefault(size = 10) pageable: Pageable): Slice<PersonResponse> {
        return personService.getPersonsWithSlicing(pageable)
    }

    // QueryDSL - Find persons with age >= specified value
    @GetMapping("/age/{minAge}")
    @Operation(summary = "Get persons with age greater than or equal to specified value")
    fun getPersonsByMinAge(@PathVariable minAge: Int): List<PersonResponse> {
        return personService.findByAgeGreaterThanEqual(minAge)
    }

    // QueryDSL - Find persons with age >= 10
    @GetMapping("/age-over-10")
    @Operation(summary = "Get persons with age greater than or equal to 10")
    fun getPersonsWithAgeOver10(): List<PersonResponse> {
        return personService.findByAgeGreaterThanEqual(10)
    }
}
