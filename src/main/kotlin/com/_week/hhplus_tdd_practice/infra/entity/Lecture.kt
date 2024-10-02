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

    @OneToMany(mappedBy = "lecture", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val registrationHistory: List<LectureEnrollmentHistory> = mutableListOf(),

    @OneToMany(mappedBy = "lecture", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val schedules: List<LectureSchedule> = mutableListOf(),

    @Column(nullable = false)
    val creDateAt: LocalDateTime = LocalDateTime.now()
)

enum class LectureType(val displayName: String) {
    Ai("Machine Learning"),
    BACK_END("Back End"),
    FRONT_END("Front End"),;
}