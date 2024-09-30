package com._week.hhplus_tdd_practice.domain.dto

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
    }
}
