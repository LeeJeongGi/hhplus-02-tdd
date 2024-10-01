package com._week.hhplus_tdd_practice.domain.dto

import com._week.hhplus_tdd_practice.infra.entity.Lecture

data class LectureHistoryDto(
    val lecture: Lecture,
    val userId: Long,
) {
    companion object {
        fun of(lecture: Lecture, userId: Long): LectureHistoryDto {
            return LectureHistoryDto(
                lecture = lecture,
                userId = userId,
            )
        }
    }
}
