package kr.co.uplus.app.exception

import org.springframework.data.mapping.PropertyReferenceException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

/**
 * Global exception handler for the application.
 * Handles validation errors and returns appropriate error responses.
 */
@ControllerAdvice
class GlobalExceptionHandler {

    /**
     * Handles validation errors from @Valid annotations.
     * Returns a 400 Bad Request response with details about the validation errors.
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, Any>> {
        val errors = HashMap<String, String>()
        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.defaultMessage ?: "Validation error"
            errors[fieldName] = errorMessage
        }

        val response = mapOf(
            "status" to HttpStatus.BAD_REQUEST.value(),
            "message" to "Validation failed",
            "errors" to errors
        )

        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    /**
     * Handles property reference exceptions that occur when sorting by non-existent properties.
     * Returns a 400 Bad Request response with details about the invalid property.
     */
    @ExceptionHandler(PropertyReferenceException::class)
    fun handlePropertyReferenceException(ex: PropertyReferenceException): ResponseEntity<Map<String, Any>> {
        val response = mapOf(
            "status" to HttpStatus.BAD_REQUEST.value(),
            "message" to "Invalid sort property",
            "error" to ex.message
        )

        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }
}
