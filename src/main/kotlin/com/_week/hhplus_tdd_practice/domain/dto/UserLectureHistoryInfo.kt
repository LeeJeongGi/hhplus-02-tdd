package com._week.hhplus_tdd_practice.domain.dto

import com._week.hhplus_tdd_practice.infra.entity.LectureEnrollmentHistory
import com._week.hhplus_tdd_practice.infra.entity.LectureType

data class UserLectureHistoryInfo(
    val userId: Long,
    val lectureId: Long,
    val title: LectureType,
    val presenter: String,
) {
    companion object {
        fun of(lectureEnrollmentHistory: LectureEnrollmentHistory): UserLectureHistoryInfo {
            return UserLectureHistoryInfo(
                userId = lectureEnrollmentHistory.userId!!,
                lectureId = lectureEnrollmentHistory.lecture.id!!,
                title = lectureEnrollmentHistory.lecture.title,
                presenter = lectureEnrollmentHistory.lecture.presenter,
            )
        }
    }
}
