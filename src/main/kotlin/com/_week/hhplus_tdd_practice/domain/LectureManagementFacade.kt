package com._week.hhplus_tdd_practice.domain

import com._week.hhplus_tdd_practice.domain.dto.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class LectureManagementFacade(
    private val lectureService: LectureService,
    private val lectureEnrollmentService: LectureEnrollmentService,
    private val lectureApplierService: LectureApplierService
) {

    @Transactional(isolation = Isolation.READ_COMMITTED)
    fun enroll(request: UserLectureDto): LectureHistoryInfo {

        // 1. 특강 신청자가 30명 넘었는지 검증
        val lectureCapacity = lectureApplierService.getLectureCapacity(request.lectureId)
        if (lectureCapacity.currentEnrollmentCount >= 30) {
            throw IllegalArgumentException("신청 인원이 30명을 초과할 수 없습니다.")
        }

        // 2. 해당 특강에 신청 이력이 있는지 검증
        if (!lectureEnrollmentService.checkUserEnrollment(request)) {
            throw IllegalArgumentException("동일한 유저는 동일 특강에 대해 한 번만 신청할 수 있습니다.")
        }

        // 3. 유효한 강좌인지 검증
        val lecture = lectureService.findLecture(request.lectureId)

        // 4. 신청
        lectureEnrollmentService.enroll(LectureHistoryDto.of(lecture, request.userId))

        // 5. 현재 인원 수 증가
        lectureApplierService.incrementEnrollmentCount(request.lectureId)

        return LectureHistoryInfo.of(lecture, request.userId)
    }

    @Transactional(readOnly = true)
    fun getLectures(): List<LectureInfo> {
        val lectures = lectureService.getUpcomingLecture(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))

        return lectures.filter { lecture ->
            lectureEnrollmentService.checkLectureCapacity(lecture.id!!)
        }.map {
            LectureInfo.of(it)
        }
    }

    fun getUserOfLectureHistory(userId: Long): List<UserLectureHistoryInfo> {
        val userLectureHistory = lectureEnrollmentService.getUserOfLectureHistory(userId)
        return userLectureHistory.map {
            UserLectureHistoryInfo.of(it)
        }
    }
}