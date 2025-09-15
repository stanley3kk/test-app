package kr.co.uplus.app.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kr.co.uplus.app.dto.request.CreateFaqRequest
import kr.co.uplus.app.dto.request.UpdateFaqRequest
import kr.co.uplus.app.dto.response.FaqResponse
import kr.co.uplus.app.service.FaqService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class FaqControllerTest {

    private lateinit var mockMvc: MockMvc
    
    @MockK
    private lateinit var faqService: FaqService
    
    private lateinit var faqController: FaqController
    
    private val objectMapper = ObjectMapper()
    
    @BeforeEach
    fun setup() {
        faqController = FaqController(faqService)
        mockMvc = MockMvcBuilders.standaloneSetup(faqController).build()
        
        // Configure ObjectMapper to handle LocalDateTime
        objectMapper.findAndRegisterModules()
    }
    
    @Test
    fun `should return FAQs when getFaqs is called with valid OS type`() {
        // Given
        val osType = "iOS"
        val faqList = listOf(
            FaqResponse(
                id = 1L,
                question = "Test Question 1",
                answer = "Test Answer 1",
                faqType = 1,
                faqCategory = "Test Category",
                isFavorite = false,
                imageUrl = null,
                appLink = null,
                startTime = LocalDateTime.now()
            ),
            FaqResponse(
                id = 2L,
                question = "Test Question 2",
                answer = "Test Answer 2",
                faqType = 2,
                faqCategory = "Test Category",
                isFavorite = true,
                imageUrl = null,
                appLink = null,
                startTime = LocalDateTime.now()
            )
        )
        
        val responseMap = mapOf("faqs" to faqList)
        
        every { faqService.getFaqByIdAndOsType(null, osType) } returns ResponseEntity.ok(responseMap)
        
        // When & Then
        mockMvc.perform(get("/faq?os=$osType"))
            .andExpect(status().isOk)
        
        // Verify
        verify(exactly = 1) { faqService.getFaqByIdAndOsType(null, osType) }
    }
    
    @Test
    fun `should return specific FAQ when getFaqs is called with valid ID and OS type`() {
        // Given
        val faqId = 1L
        val osType = "iOS"
        val faq = FaqResponse(
            id = faqId,
            question = "Test Question",
            answer = "Test Answer",
            faqType = 1,
            faqCategory = "Test Category",
            isFavorite = false,
            imageUrl = null,
            appLink = null,
            startTime = LocalDateTime.now()
        )
        
        val responseMap = mapOf("faq" to faq)
        
        every { faqService.getFaqByIdAndOsType(faqId, osType) } returns ResponseEntity.ok(responseMap)
        
        // When & Then
        mockMvc.perform(get("/faq?id=$faqId&os=$osType"))
            .andExpect(status().isOk)
        
        // Verify
        verify(exactly = 1) { faqService.getFaqByIdAndOsType(faqId, osType) }
    }
    
    @Test
    fun `should return 400 when getFaqs is called without OS type`() {
        // When & Then
        mockMvc.perform(get("/faq"))
            .andExpect(status().isBadRequest)
        
        // Verify - no service method should be called
        verify(exactly = 0) { faqService.getFaqByIdAndOsType(any(), any()) }
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
        
        val createdFaq = FaqResponse(
            id = 1L,
            question = "Test Question",
            answer = "Test Answer",
            faqType = 1,
            faqCategory = "Test Category",
            isFavorite = false,
            imageUrl = null,
            appLink = null,
            startTime = LocalDateTime.now()
        )
        
        every { faqService.createFaq(any()) } returns createdFaq
        
        // When & Then
        mockMvc.perform(
            post("/faq")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.question").value("Test Question"))
            .andExpect(jsonPath("$.answer").value("Test Answer"))
        
        // Verify
        verify(exactly = 1) { faqService.createFaq(request) }
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
        
        // When & Then
        mockMvc.perform(
            post("/faq")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
        
        // Verify - no service method should be called
        verify(exactly = 0) { faqService.createFaq(any()) }
    }
    
    @Test
    fun `should update FAQ when updateFaq is called with valid request`() {
        // Given
        val faqId = 1L
        val request = UpdateFaqRequest(
            question = "Updated Question",
            answer = "Updated Answer",
            faqType = 2,
            faqCategory = "Updated Category",
            isFavorite = true,
            osType = "iOS"
        )
        
        val updatedFaq = FaqResponse(
            id = faqId,
            question = "Updated Question",
            answer = "Updated Answer",
            faqType = 2,
            faqCategory = "Updated Category",
            isFavorite = true,
            imageUrl = null,
            appLink = null,
            startTime = LocalDateTime.now()
        )
        
        every { faqService.updateFaq(faqId, any()) } returns ResponseEntity.ok(updatedFaq)
        
        // When & Then
        mockMvc.perform(
            put("/faq/$faqId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(faqId))
            .andExpect(jsonPath("$.question").value("Updated Question"))
            .andExpect(jsonPath("$.answer").value("Updated Answer"))
        
        // Verify
        verify(exactly = 1) { faqService.updateFaq(faqId, request) }
    }
    
    @Test
    fun `should delete FAQ when deleteFaq is called with valid ID`() {
        // Given
        val faqId = 1L
        
        every { faqService.deleteFaq(faqId) } returns ResponseEntity.noContent().build()
        
        // When & Then
        mockMvc.perform(delete("/faq/$faqId"))
            .andExpect(status().isNoContent)
        
        // Verify
        verify(exactly = 1) { faqService.deleteFaq(faqId) }
    }
}