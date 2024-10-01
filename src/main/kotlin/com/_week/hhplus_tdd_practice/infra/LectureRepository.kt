package com._week.hhplus_tdd_practice.infra

import com._week.hhplus_tdd_practice.infra.entity.Lecture
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface LectureRepository : JpaRepository<Lecture, Long> {
    fun findByDateAfter(currentDateTime: LocalDateTime): List<Lecture>
}