package kr.co.uplus.app.controller

import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ExtendWith(MockKExtension::class)
class TestControllerTest {

    private lateinit var mockMvc: MockMvc
    
    private lateinit var testController: TestController
    
    @BeforeEach
    fun setup() {
        testController = TestController()
        mockMvc = MockMvcBuilders.standaloneSetup(testController).build()
    }
    
    @Test
    fun `should return Hello World when helloWorld is called`() {
        // When & Then
        mockMvc.perform(get("/hello"))
            .andExpect(status().isOk)
            .andExpect(content().string("Hello World"))
        
        println("[DEBUG_LOG] Test completed successfully")
    }
}