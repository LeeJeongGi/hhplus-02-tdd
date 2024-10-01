package com._week.hhplus_tdd_practice.api.lecture

import com._week.hhplus_tdd_practice.api.lecture.request.LectureEnrollRequest
import com._week.hhplus_tdd_practice.api.lecture.response.LectureEnrollResponse
import com._week.hhplus_tdd_practice.api.lecture.response.LectureInfoResponse
import com._week.hhplus_tdd_practice.api.lecture.response.UserLectureHistoryResponse
import com._week.hhplus_tdd_practice.domain.LectureManagementFacade
import com._week.hhplus_tdd_practice.domain.dto.UserLectureDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/lecture")
class LectureController(
    private val lectureManagementFacade: LectureManagementFacade
) {

    /**
     ***(핵심)** 특강 신청 **API**
     */
    @PostMapping
    fun enroll(@RequestBody lectureEnrollRequest: LectureEnrollRequest): ResponseEntity<LectureEnrollResponse> {
        val lectureHistoryInfo = lectureManagementFacade.enroll(UserLectureDto.of(lectureEnrollRequest))
        return ResponseEntity.ok(LectureEnrollResponse.of(lectureHistoryInfo))
    }

    /**
     * 특강 선택 API
     */
    @GetMapping("/open/list")
    fun getOpenLectures(): ResponseEntity<List<LectureInfoResponse>> {
        val lectures = lectureManagementFacade.getLectures()
        return ResponseEntity.ok(lectures.map { LectureInfoResponse.of(it) }.toList())
    }

    /**
     * 특강 신청 완료 목록 조회 API
     */
    @GetMapping("/list/{userId}")
    fun getUserHistory(@PathVariable userId: Long): ResponseEntity<List<UserLectureHistoryResponse>> {
        val history = lectureManagementFacade.getUserOfLectureHistory(userId)
        return ResponseEntity.ok(history.map { UserLectureHistoryResponse.of(it) }.toList())
    }
}