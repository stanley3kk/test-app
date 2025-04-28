package kr.co.uplus.app.config

import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Configuration
import org.springframework.cache.jcache.JCacheCacheManager
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import javax.cache.Caching
import java.net.URI

/**
 * Configuration class for caching with EhCache.
 */
@Configuration
@EnableCaching
class CacheConfig {

    /**
     * Creates and configures the cache manager using EhCache.
     *
     * @return The configured cache manager
     */
    @Bean
    fun cacheManager(): CacheManager {
        val provider = Caching.getCachingProvider()
        val resourceUrl = this.javaClass.classLoader.getResource("ehcache.xml")
            ?: throw IllegalStateException("Could not find ehcache.xml on the classpath")
        val ehcacheManager = provider.getCacheManager(
            resourceUrl.toURI(),
            this.javaClass.classLoader
        )
        return JCacheCacheManager(ehcacheManager)
    }
}
