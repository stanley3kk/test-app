package kr.co.uplus.app.repository

import kr.co.uplus.app.domain.PersonEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonRepository : JpaRepository<PersonEntity, Long>, PersonRepositoryCustom {
    fun findByName(name: String): PersonEntity
}
