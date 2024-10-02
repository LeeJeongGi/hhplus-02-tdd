package com._week.hhplus_tdd_practice.infra.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "lecture_schedule")
data class LectureSchedule(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val date: LocalDateTime,

    @Column(nullable = false)
    val capacity: Int = 30,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    val lecture: Lecture,

    @Column(nullable = false)
    val creDateAt: LocalDateTime = LocalDateTime.now()
)
