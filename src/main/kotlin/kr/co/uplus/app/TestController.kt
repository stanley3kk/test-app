package kr.co.uplus.app

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    @GetMapping("/hello")
    fun helloWorld(): String {
        return "Hello World"
    }
}
