package com._week.hhplus_tdd_practice.domain

import com._week.hhplus_tdd_practice.domain.dto.LectureHistoryDto
import com._week.hhplus_tdd_practice.domain.dto.UserLectureDto
import com._week.hhplus_tdd_practice.infra.entity.LectureEnrollmentHistory
import com._week.hhplus_tdd_practice.infra.LectureEnrollmentRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class LectureEnrollmentService(
    private val lectureEnrollmentRepository: LectureEnrollmentRepository,
) {

    fun enroll(lectureHistoryDto: LectureHistoryDto): LectureEnrollmentHistory {
        val lectureEnrollmentHistory = LectureEnrollmentHistory(
            lecture = lectureHistoryDto.lecture,
            userId = lectureHistoryDto.userId,
            creDateAt = LocalDateTime.now(),
        )

        return lectureEnrollmentRepository.save(lectureEnrollmentHistory)
    }

    fun checkUserEnrollment(userLectureDto: UserLectureDto): Boolean {
        val enrollment = lectureEnrollmentRepository.findByUserIdAndLectureId(userLectureDto.userId, userLectureDto.lectureId)
        return enrollment == null
    }

    fun checkLectureCapacity(lectureId: Long): Boolean {
        val currentEnrollmentCount = lectureEnrollmentRepository.countByLectureId(lectureId)
        return currentEnrollmentCount < 30
    }
}