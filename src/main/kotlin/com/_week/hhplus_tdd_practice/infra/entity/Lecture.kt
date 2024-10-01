package com._week.hhplus_tdd_practice.infra.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "lecture")
data class Lecture(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val title: LectureType,

    @Column(nullable = false)
    val presenter: String,

    @Column(nullable = false)
    val capacity: Int = 30,

    @Column(nullable = false)
    val date: LocalDateTime,

    @OneToMany(mappedBy = "lecture", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val registrationHistory: List<LectureEnrollmentHistory> = mutableListOf(),
)

enum class LectureType(val displayName: String) {
    Ai("Machine Learning"),
    BACK_END("Back End"),
    FRONT_END("Front End"),;
}