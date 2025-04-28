package kr.co.uplus.app.service

import kr.co.uplus.app.client.NaverClient
import kr.co.uplus.app.dto.response.NaverResponse
import org.springframework.stereotype.Service

@Service
class NaverServiceImpl(
    private val naverClient: NaverClient
) : NaverService {
    
    override fun getNaverHomepage(): NaverResponse {
        val response = naverClient.getNaverHomepage()
        return NaverResponse(response.body ?: "No content available")
    }
}