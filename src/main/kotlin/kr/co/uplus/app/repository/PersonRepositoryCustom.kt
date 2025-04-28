package kr.co.uplus.app.repository

import kr.co.uplus.app.domain.PersonEntity

/**
 * Custom repository interface for PersonEntity to support QueryDSL operations.
 */
interface PersonRepositoryCustom {
    /**
     * Find all persons with age greater than or equal to the specified value.
     *
     * @param age The minimum age to filter by
     * @return List of PersonEntity with age >= the specified value
     */
    fun findByAgeGreaterThanEqual(age: Int): List<PersonEntity>
}