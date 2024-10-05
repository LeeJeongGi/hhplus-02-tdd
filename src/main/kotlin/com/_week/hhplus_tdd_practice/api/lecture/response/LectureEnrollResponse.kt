package com._week.hhplus_tdd_practice.api.lecture.response

import com._week.hhplus_tdd_practice.domain.dto.LectureHistoryInfo
import com._week.hhplus_tdd_practice.infra.entity.LectureType

data class LectureEnrollResponse(
    val userId: Long,
    val lectureId: Long,
    val title: LectureType,
    val presenter: String,
) {
    companion object {
        fun of(lectureHistoryInfo: LectureHistoryInfo): LectureEnrollResponse {
            return LectureEnrollResponse(
                userId = lectureHistoryInfo.userId,
                lectureId = lectureHistoryInfo.lectureId ?: 0L,
                title = lectureHistoryInfo.title,
                presenter = lectureHistoryInfo.presenter,
            )
        }
    }
}
