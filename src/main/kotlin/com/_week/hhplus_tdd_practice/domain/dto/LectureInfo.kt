package com._week.hhplus_tdd_practice.domain.dto

import com._week.hhplus_tdd_practice.infra.entity.Lecture
import com._week.hhplus_tdd_practice.infra.entity.LectureType

data class LectureInfo(
    val lectureId: Long,
    val title: LectureType,
    val presenter: String,
) {
    companion object {
        fun of(lecture: Lecture): LectureInfo {
            return LectureInfo(
                lectureId = lecture.id ?: 0L,
                title = lecture.title,
                presenter = lecture.presenter,
            )
        }
    }
}
