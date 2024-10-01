package com._week.hhplus_tdd_practice.api.lecture.response

import com._week.hhplus_tdd_practice.domain.dto.UserLectureHistoryInfo
import com._week.hhplus_tdd_practice.infra.entity.LectureType

data class UserLectureHistoryResponse(
    val userId: Long,
    val lectureId: Long,
    val title: LectureType,
    val presenter: String,
) {
    companion object {
        fun of(userLectureHistoryInfo: UserLectureHistoryInfo): UserLectureHistoryResponse {
            return UserLectureHistoryResponse(
                userId = userLectureHistoryInfo.userId,
                lectureId = userLectureHistoryInfo.lectureId,
                title = userLectureHistoryInfo.title,
                presenter = userLectureHistoryInfo.presenter,
            )
        }
    }
}
