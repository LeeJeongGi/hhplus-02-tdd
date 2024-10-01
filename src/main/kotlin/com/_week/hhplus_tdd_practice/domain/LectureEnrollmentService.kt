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

    fun checkUserEnrollment(userLectureDto: UserLectureDto) {
        val enrollment = lectureEnrollmentRepository.findByUserIdAndLectureId(userLectureDto.userId, userLectureDto.lectureId)

        if (enrollment != null) {
            throw IllegalArgumentException("동일한 유저는 동일 특강에 대해 한 번만 신청할 수 있습니다.")
        }
    }

    fun checkLectureCapacity(lectureId: Long) {
        val currentEnrollmentCount = lectureEnrollmentRepository.countByLectureId(lectureId)

        if (currentEnrollmentCount >= 30) {
            throw IllegalArgumentException("신청 인원이 30명을 초과할 수 없습니다.")
        }
    }
}