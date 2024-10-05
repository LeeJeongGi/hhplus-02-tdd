package com._week.hhplus_tdd_practice.infra

import com._week.hhplus_tdd_practice.infra.entity.LectureSchedule
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LectureScheduleRepository : JpaRepository<LectureSchedule, Long> {

}