package com._week.hhplus_tdd_practice.api.lecture.response

import com._week.hhplus_tdd_practice.domain.dto.LectureInfo
import com._week.hhplus_tdd_practice.infra.entity.Lecture
import com._week.hhplus_tdd_practice.infra.entity.LectureType

data class LectureInfoResponse(
    val lectureId: Long,
    val title: LectureType,
    val presenter: String,
) {
    companion object {
        fun of(lectureInfo: LectureInfo): LectureInfoResponse {
            return LectureInfoResponse(
                lectureId = lectureInfo.lectureId ?: 0L,
                title = lectureInfo.title,
                presenter = lectureInfo.presenter,
            )
        }
    }
}
