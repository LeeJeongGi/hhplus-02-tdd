package com._week.hhplus_tdd_practice.infra.entity

import jakarta.persistence.*

@Entity
class LectureApplier (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val lectureId: Long,

    @Column(nullable = false)
    var currentEnrollmentCount: Int = 0,
) {
    fun increaseEnrollmentCount() {
        this.currentEnrollmentCount += 1
    }
}