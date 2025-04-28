package kr.co.uplus.app.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.uplus.app.domain.PersonEntity
import kr.co.uplus.app.domain.QPersonEntity
import org.springframework.stereotype.Repository

/**
 * Implementation of PersonRepositoryCustom interface.
 * 
 * Note: This implementation currently uses JPQL because the QueryDSL Q-classes
 * haven't been generated yet. After building the project, this implementation
 * should be updated to use QueryDSL as follows:
 *
 * ```
 * override fun findByAgeGreaterThanEqual(age: Int): List<PersonEntity> {
 *     return queryFactory
 *         .selectFrom(QPersonEntity.personEntity)
 *         .where(QPersonEntity.personEntity.age.goe(age))
 *         .fetch()
 * }
 * ```
 */
@Repository
class PersonRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : PersonRepositoryCustom {

    /**
     * Find all persons with age greater than or equal to the specified value.
     * Currently using JPQL, will be updated to use QueryDSL once Q-classes are generated.
     *
     * @param age The minimum age to filter by
     * @return List of PersonEntity with age >= the specified value
     */
    override fun findByAgeGreaterThanEqual(age: Int): List<PersonEntity> {
         return queryFactory
             .selectFrom(QPersonEntity.personEntity)
             .where(QPersonEntity.personEntity.age.goe(age))
             .fetch()
    }
}
