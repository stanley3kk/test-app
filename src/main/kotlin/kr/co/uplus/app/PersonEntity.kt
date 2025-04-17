package kr.co.uplus.app

import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table
data class PersonEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var age: Int
) : Serializable
