package kr.co.uplus.app.config

import feign.RetryableException
import feign.Retryer
import feign.codec.ErrorDecoder
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.IOException
import java.util.Date

/**
 * Configuration for Feign clients with retry mechanism
 */
@Configuration
class FeignConfig {
    private val logger = LoggerFactory.getLogger(FeignConfig::class.java)

    /**
     * Configures retry behavior for Feign clients
     * - period: 1000ms (initial backoff period)
     * - maxPeriod: 5000ms (maximum backoff period)
     * - maxAttempts: 3 (maximum number of retry attempts)
     */
    @Bean
    fun retryer(): Retryer {
        // Retry up to 3 times with a 1s initial backoff and 5s max backoff
        return Retryer.Default(1000, 5000, 3)
    }

    /**
     * Custom error decoder that determines which exceptions should trigger retries
     * - Retries on network errors (IOException)
     * - Retries on 5xx server errors
     * - Retries on 429 Too Many Requests
     */
    @Bean
    fun errorDecoder(): ErrorDecoder {
        return ErrorDecoder { methodKey, response ->
            val status = response.status()
            val exception = ErrorDecoder.Default().decode(methodKey, response)

            if (exception is IOException || status >= 500 || status == 429) {
                logger.warn("Request failed with status $status. Retrying... Method: $methodKey")
                // Using Date as retryAfter parameter to avoid constructor ambiguity
                RetryableException(
                    status,
                    response.reason(),
                    response.request().httpMethod(),
                    Date(), // Use current date as retryAfter
                    response.request()
                )
            } else {
                logger.error("Request failed with status $status. Not retrying. Method: $methodKey")
                exception
            }
        }
    }
}
