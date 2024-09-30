package com._week.hhplus_tdd_practice.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "lecture_enrollment_history")
data class LectureEnrollmentHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false)
    val lecture: Lecture,

    @Column(nullable = false)
    val userId: Long? = null,

    @Column(nullable = false)
    val creDateAt: LocalDateTime = LocalDateTime.now()
)