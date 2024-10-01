package com._week.hhplus_tdd_practice.domain

import com._week.hhplus_tdd_practice.infra.entity.Lecture
import com._week.hhplus_tdd_practice.infra.LectureRepository
import org.springframework.stereotype.Service

@Service
class LectureService(
    val lectureRepository: LectureRepository
) {

    fun create(lecture: Lecture): Lecture {
        return lectureRepository.save(lecture)
    }

    fun findLecture(lectureId: Long): Lecture {
        val lecture = lectureRepository.findById(lectureId).orElseThrow {
            throw IllegalArgumentException("해당 ID의 특강이 존재하지 않습니다.")
        }

        return lecture
    }

}