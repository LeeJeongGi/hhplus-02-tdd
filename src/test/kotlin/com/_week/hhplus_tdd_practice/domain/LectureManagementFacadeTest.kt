package com._week.hhplus_tdd_practice.domain

import com._week.hhplus_tdd_practice.domain.dto.LectureHistoryDto
import com._week.hhplus_tdd_practice.domain.dto.UserLectureDto
import com._week.hhplus_tdd_practice.infra.entity.Lecture
import com._week.hhplus_tdd_practice.infra.entity.LectureEnrollmentHistory
import com._week.hhplus_tdd_practice.infra.entity.LectureType
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
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
    @DisplayName("동일한 유저가 동일 특강에 신청할 경우 에러가 발생 한다.")
    fun enrollExceptionTest1() {
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
        `when`(lectureEnrollmentService.checkUserEnrollment(request)).thenReturn(false)

        // when & then
        val message = assertThrows<IllegalArgumentException> {
            lectureManagementFacade.enroll(request)
        }.message

        assertThat(message).isEqualTo("동일한 유저는 동일 특강에 대해 한 번만 신청할 수 있습니다.")
    }

    @Test
    @DisplayName("특강 신청 회원 수는 30명을 넘을 수 없기 때문에 31번째 신청 했을 때 에러 발생 테스트")
    fun enrollExceptionTest2() {
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

        `when`(lectureEnrollmentService.checkUserEnrollment(request)).thenReturn(true)
        `when`(lectureService.findLecture(request.lectureId)).thenReturn(lecture)
        `when`(lectureEnrollmentService.checkLectureCapacity(lecture.id!!)).thenReturn(false)

        // when & then
        val message = assertThrows<IllegalArgumentException> {
            lectureManagementFacade.enroll(request)
        }.message

        assertThat(message).isEqualTo("신청 인원이 30명을 초과할 수 없습니다.")
    }

    @Test
    @DisplayName("유저가 특강을 성공적으로 신청할 수 있다.")
    fun enrollSuccess() {
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

        `when`(lectureEnrollmentService.checkUserEnrollment(request)).thenReturn(true)
        `when`(lectureService.findLecture(request.lectureId)).thenReturn(lecture)
        `when`(lectureEnrollmentService.checkLectureCapacity(lecture.id!!)).thenReturn(true)
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

    @Test
    @DisplayName("오늘 이후로 있는 특강 중 아직 정원(30명)이 미달된 특강 정보만 조회 테스트")
    fun getLecturesTest() {
        // given
        val lectures = listOf(
            Lecture(
                id = 1L,
                title = LectureType.Ai,
                presenter = "Lee",
                capacity = 30,
                date = LocalDateTime.parse("2024-10-02 13:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            ),
            Lecture(
                id = 2L,
                title = LectureType.FRONT_END,
                presenter = "Kim",
                capacity = 30,
                date = LocalDateTime.parse("2024-10-03 13:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            ),
            Lecture(
                id = 3L,
                title = LectureType.BACK_END,
                presenter = "Park",
                capacity = 30,
                date = LocalDateTime.parse("2024-10-04 13:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            ),
        )

        `when`(lectureService.getUpcomingLecture(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))).thenReturn(lectures)
        `when`(lectureEnrollmentService.checkLectureCapacity(lectures[0].id!!)).thenReturn(true)
        `when`(lectureEnrollmentService.checkLectureCapacity(lectures[1].id!!)).thenReturn(true)
        `when`(lectureEnrollmentService.checkLectureCapacity(lectures[2].id!!)).thenReturn(false)

        // when
        val result = lectureManagementFacade.getLectures()

        // then
        assertThat(result).isNotNull
        assertThat(result.size).isEqualTo(2)
    }

    @Test
    @DisplayName("신청 할 수 있는 특강이 한개도 없을 때 빈 배열 출력 테스트")
    fun getLecturesTest2() {
        // given
        `when`(lectureService.getUpcomingLecture(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))).thenReturn(listOf())

        // when
        val result = lectureManagementFacade.getLectures()

        // then
        assertThat(result).isNotNull
        assertThat(result.size).isEqualTo(0)
    }

    @Test
    @DisplayName("특정 userId 로 신청 완료된 특강 목록을 조회하는 테스트")
    fun getUserOfLectureHistoryTest() {
        // given
        val userLectureHistory = setTestHistory()
        `when`(lectureEnrollmentService.getUserOfLectureHistory(1L)).thenReturn(userLectureHistory)

        // when
        val result = lectureManagementFacade.getUserOfLectureHistory(1L)

        // then
        assertThat(result).isNotNull
        assertThat(result.size).isEqualTo(3)
    }

    private fun setTestHistory() = listOf(
        LectureEnrollmentHistory(
            id = 1L,
            userId = 1L,
            lecture = Lecture(
                id = 1L,
                title = LectureType.Ai,
                presenter = "Lee",
                capacity = 30,
                date = LocalDateTime.parse("2025-09-30 13:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            ),
            creDateAt = LocalDateTime.now()
        ),
        LectureEnrollmentHistory(
            id = 2L,
            userId = 1L,
            lecture = Lecture(
                id = 2L,
                title = LectureType.FRONT_END,
                presenter = "Kee",
                capacity = 30,
                date = LocalDateTime.parse("2025-10-30 13:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            ),
            creDateAt = LocalDateTime.now()
        ),
        LectureEnrollmentHistory(
            id = 3L,
            userId = 1L,
            lecture = Lecture(
                id = 3L,
                title = LectureType.BACK_END,
                presenter = "eee",
                capacity = 30,
                date = LocalDateTime.parse("2025-07-30 13:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            ),
            creDateAt = LocalDateTime.now()
        ),
    )

}