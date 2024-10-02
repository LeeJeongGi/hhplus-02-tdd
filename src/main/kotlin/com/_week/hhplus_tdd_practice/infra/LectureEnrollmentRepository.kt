package com._week.hhplus_tdd_practice.infra

import com._week.hhplus_tdd_practice.infra.entity.LectureEnrollmentHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface LectureEnrollmentRepository: JpaRepository<LectureEnrollmentHistory, Long> {

    fun findByUserIdAndLectureId(userId: Long, lectureId: Long): LectureEnrollmentHistory?

    fun countByLectureId(lectureId: Long): Long

    @Query("SELECT le FROM LectureEnrollmentHistory le " +
            "JOIN FETCH le.lecture l " +
            "JOIN FETCH l.schedules ls " +
            "WHERE le.userId = :userId")
    fun findAllByUserId(userId: Long): List<LectureEnrollmentHistory>
}