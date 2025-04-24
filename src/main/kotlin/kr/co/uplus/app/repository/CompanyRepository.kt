package kr.co.uplus.app.repository

import kr.co.uplus.app.domain.CompanyEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CompanyRepository : JpaRepository<CompanyEntity, Long> {
    fun findByName(name: String): CompanyEntity
}