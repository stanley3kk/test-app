package kr.co.uplus.app

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonRepository : JpaRepository<PersonEntity, Long> {
    fun findByName(name: String): PersonEntity
}
