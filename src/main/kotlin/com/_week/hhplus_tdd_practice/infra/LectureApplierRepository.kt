package com._week.hhplus_tdd_practice.infra

import com._week.hhplus_tdd_practice.infra.entity.LectureApplier
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface LectureApplierRepository : JpaRepository<LectureApplier, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT la FROM LectureApplier la WHERE la.lectureId = :lectureId")
    fun findByLectureIdWithLock(@Param("lectureId") lectureId: Long): LectureApplier?

    @Query("SELECT la.currentEnrollmentCount FROM LectureApplier la WHERE la.lectureId = :lectureId")
    fun getCurrentEnrollmentCountByLectureId(@Param("lectureId") lectureId: Long): Int?
}