package kr.co.uplus.app.client

import kr.co.uplus.app.config.FeignConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
///sdkfjdslkjf
@FeignClient(
    name = "naver-client", 
    url = "https://www.naver.com",
    configuration = [FeignConfig::class]
)
interface NaverClient {

    @GetMapping("/")
    fun getNaverHomepage(): ResponseEntity<String>
}
