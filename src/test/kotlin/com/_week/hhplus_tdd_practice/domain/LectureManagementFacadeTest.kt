package com._week.hhplus_tdd_practice.domain

import com._week.hhplus_tdd_practice.domain.dto.LectureHistoryDto
import com._week.hhplus_tdd_practice.domain.dto.UserLectureDto
import com._week.hhplus_tdd_practice.infra.entity.Lecture
import com._week.hhplus_tdd_practice.infra.entity.LectureEnrollmentHistory
import com._week.hhplus_tdd_practice.infra.entity.LectureType
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class LectureManagementFacadeTest {

    @Mock
    private lateinit var lectureService: LectureService

    @Mock
    private lateinit var lectureEnrollmentService: LectureEnrollmentService

    @InjectMocks
    private lateinit var lectureManagementFacade: LectureManagementFacade

    @Test
    @DisplayName("유저가 특강을 성공적으로 신청할 수 있다.")
    fun testEnrollSuccess() {
        // given
        val userId = 1L
        val lecture = Lecture(
            id = 1L,
            title = LectureType.Ai,
            presenter = "Ms.Lee",
            capacity = 30,
            date = LocalDateTime.parse("2024-09-01 13:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        )

        val request = UserLectureDto(lecture.id!!, userId)

        doNothing().`when`(lectureEnrollmentService).checkUserEnrollment(request)
        `when`(lectureService.findLecture(request.lectureId)).thenReturn(lecture)
        doNothing().`when`(lectureEnrollmentService).checkLectureCapacity(lecture.id!!)
        `when`(lectureEnrollmentService.enroll(LectureHistoryDto.of(lecture, userId))).thenReturn(
            LectureEnrollmentHistory(
                id = 1L,
                lecture = lecture,
                userId = 1L,
                creDateAt = LocalDateTime.now()
            )
        )

        // when
        val result = lectureManagementFacade.enroll(request)

        // then
        assertNotNull(result)
        assertThat(result.lectureId).isEqualTo(lecture.id)
        assertThat(result.userId).isEqualTo(userId)
        assertThat(result.title).isEqualTo(LectureType.Ai)
        assertThat(result.presenter).isEqualTo(lecture.presenter)
    }
}