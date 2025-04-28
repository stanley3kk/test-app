package kr.co.uplus.app.controller

import io.swagger.v3.oas.annotations.Operation
import kr.co.uplus.app.dto.response.NaverResponse
import kr.co.uplus.app.service.NaverService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/naver")
class NaverController(
    private val naverService: NaverService
) {

    @GetMapping
    @Operation(summary = "Get Naver homepage content")
    fun getNaverHomepage(): NaverResponse {
        return naverService.getNaverHomepage()
    }
}