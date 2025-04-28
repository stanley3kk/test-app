package kr.co.uplus.app.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configuration class for QueryDSL.
 */
@Configuration
class QuerydslConfig {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    /**
     * Creates a JPAQueryFactory bean for use with QueryDSL.
     *
     * @return JPAQueryFactory instance
     */
    @Bean
    fun jpaQueryFactory(): JPAQueryFactory {
        return JPAQueryFactory(entityManager)
    }
}