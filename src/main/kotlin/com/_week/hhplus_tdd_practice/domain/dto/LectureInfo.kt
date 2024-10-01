package com._week.hhplus_tdd_practice.domain.dto

import com._week.hhplus_tdd_practice.domain.entity.Lecture
import com._week.hhplus_tdd_practice.domain.entity.LectureType

data class LectureInfo(
    val userId: Long,
    val lectureId: Long,
    val title: LectureType,
    val presenter: String,
) {
    companion object {
        fun of(lecture: Lecture, userId: Long): LectureInfo {
            return LectureInfo(
                userId = userId,
                lectureId = lecture.id ?: 0L,
                title = lecture.title,
                presenter = lecture.presenter,
            )
        }
    }
}
