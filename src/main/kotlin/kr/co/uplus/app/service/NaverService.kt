package kr.co.uplus.app.service

import kr.co.uplus.app.dto.response.NaverResponse

interface NaverService {
    fun getNaverHomepage(): NaverResponse
}