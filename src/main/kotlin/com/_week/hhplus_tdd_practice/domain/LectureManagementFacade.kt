package com._week.hhplus_tdd_practice.domain

import com._week.hhplus_tdd_practice.domain.dto.LectureHistoryDto
import com._week.hhplus_tdd_practice.domain.dto.LectureInfo
import com._week.hhplus_tdd_practice.domain.dto.UserLectureDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LectureManagementFacade(
    private val lectureService: LectureService,
    private val lectureEnrollmentService: LectureEnrollmentService,
) {
    @Transactional(readOnly = true)
    fun enroll(request: UserLectureDto): LectureInfo {
        // 1. 해당 특강에 신청 이력이 있는지 검증
        lectureEnrollmentService.checkUserEnrollment(request)

        // 2. 유효한 강좌인지 검증
        val lecture = lectureService.findLecture(request.lectureId)

        // 3. 특강 신청자가 30명 넘었는지 검증
        lectureEnrollmentService.checkLectureCapacity(request.lectureId)

        // 4. 신청
        lectureEnrollmentService.enroll(LectureHistoryDto.of(lecture, request.userId))
        return LectureInfo.of(lecture, request.userId)
    }
}