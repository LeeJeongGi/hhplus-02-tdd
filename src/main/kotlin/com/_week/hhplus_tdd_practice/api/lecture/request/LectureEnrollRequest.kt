package com._week.hhplus_tdd_practice.api.lecture.request

data class LectureEnrollRequest(
    val lectureId: Long,
    val userId: Long,
) {
    companion object {
        fun of(lectureId: Long, userId: Long): LectureEnrollRequest {
            return LectureEnrollRequest(
                lectureId = lectureId,
                userId = userId,
            )
        }
    }
}
