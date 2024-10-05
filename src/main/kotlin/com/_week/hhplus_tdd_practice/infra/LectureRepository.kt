package com._week.hhplus_tdd_practice.infra

import com._week.hhplus_tdd_practice.infra.entity.Lecture
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface LectureRepository : JpaRepository<Lecture, Long> {

    @Query("SELECT l FROM Lecture l JOIN FETCH l.schedules s WHERE s.date > :currentDateTime")
    fun findByDateAfter(currentDateTime: LocalDateTime): List<Lecture>
}