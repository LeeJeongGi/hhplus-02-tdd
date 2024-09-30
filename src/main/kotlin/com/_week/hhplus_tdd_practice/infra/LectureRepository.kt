package com._week.hhplus_tdd_practice.infra

import com._week.hhplus_tdd_practice.domain.entity.Lecture
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LectureRepository : JpaRepository<Lecture, Long> {

}