package com._week.hhplus_tdd_practice.infra

import com._week.hhplus_tdd_practice.domain.entity.LectureEnrollmentHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LectureEnrollmentRepository: JpaRepository<LectureEnrollmentHistory, Long> {

    fun findByUserIdAndLectureId(userId: Long, lectureId: Long): LectureEnrollmentHistory?

    fun countByLectureId(lectureId: Long): Long
}