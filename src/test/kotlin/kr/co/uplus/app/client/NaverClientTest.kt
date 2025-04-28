package kr.co.uplus.app.client

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.slf4j.LoggerFactory

@SpringBootTest
class NaverClientTest {

    private val logger = LoggerFactory.getLogger(NaverClientTest::class.java)

    @Autowired
    private lateinit var naverClient: NaverClient

    @Test
    fun `should successfully get Naver homepage`() {
        // When
        val response = naverClient.getNaverHomepage()

        // Then
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body != null)
        
        logger.info("[DEBUG_LOG] Successfully retrieved Naver homepage with status: ${response.statusCode}")
    }
}