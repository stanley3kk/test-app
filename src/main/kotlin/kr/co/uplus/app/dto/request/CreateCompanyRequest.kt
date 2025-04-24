package kr.co.uplus.app.dto.request

import jakarta.validation.constraints.Size
import jakarta.validation.constraints.Pattern

/**
 * DTO for creating a new company.
 */
data class CreateCompanyRequest(
    @field:Size(min = 2, max = 50, message = "Company name must be between 2 and 50 characters")
    val name: String,
    
    @field:Size(min = 5, max = 200, message = "Address must be between 5 and 200 characters")
    val address: String,
    
    @field:Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "Phone number must be in format: XXX-XXXX-XXXX")
    val phoneNumber: String
)