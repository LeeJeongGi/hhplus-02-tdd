package com._week.hhplus_tdd_practice.domain.dto

import com._week.hhplus_tdd_practice.api.lecture.request.LectureEnrollRequest

data class UserLectureDto(
    val lectureId: Long,
    val userId: Long,
) {
    companion object {
        fun of(lectureId: Long, userId: Long): UserLectureDto {
            return UserLectureDto(
                lectureId = lectureId,
                userId = userId,
            )
        }

        fun of(lectureEnrollRequest: LectureEnrollRequest): UserLectureDto {
            return UserLectureDto(
                lectureId = lectureEnrollRequest.lectureId,
                userId = lectureEnrollRequest.userId,
            )
        }
    }
}
