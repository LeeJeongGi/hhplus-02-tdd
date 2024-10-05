package com._week.hhplus_tdd_practice.domain

import com._week.hhplus_tdd_practice.domain.dto.LectureHistoryDto
import com._week.hhplus_tdd_practice.domain.dto.UserLectureDto
import com._week.hhplus_tdd_practice.infra.LectureRepository
import com._week.hhplus_tdd_practice.infra.LectureScheduleRepository
import com._week.hhplus_tdd_practice.infra.entity.Lecture
import com._week.hhplus_tdd_practice.infra.entity.LectureSchedule
import com._week.hhplus_tdd_practice.infra.entity.LectureType
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.test.Test

@SpringBootTest
class LectureEnrollmentServiceTest {

    @Autowired
    private lateinit var lectureEnrollmentService: LectureEnrollmentService

    @Autowired
    private lateinit var lectureRepository: LectureRepository

    @Autowired
    private lateinit var lectureScheduleRepository: LectureScheduleRepository

    @BeforeEach
    fun clear() {
        lectureRepository.deleteAll()
    }

    @Test
    @DisplayName("동일한 유저가 동일 특강에 신청할 경우 에러(false)가 발생 한다.")
    fun checkUserEnrollmentException() {
        // given
        val userId = 1L
        val saveLecture = saveTestLecture()
        lectureEnrollmentService.enroll(LectureHistoryDto.of(saveLecture, userId))

        // when
        val result = lectureEnrollmentService.checkUserEnrollment(UserLectureDto.of(saveLecture.id!!, userId))

        // then
        assertThat(result).isFalse()
    }

    @Test
    @DisplayName("특정 특강에 한번도 신청한 적이 없는 유저를 검증 했을 때 통과하는 테스트 ")
    fun checkUserEnrollmentTest() {
        // given
        val userId = 1L
        val saveLecture = saveTestLecture()

        // when & then
        assertDoesNotThrow {
            lectureEnrollmentService.checkUserEnrollment(UserLectureDto.of(saveLecture.id!!, userId))
        }

    }

    @Test
    @DisplayName("특강 신청 회원 수는 30명을 넘을 수 없기 때문에 31번째 신청 했을 때 에러 발생(false) 테스트")
    fun checkLectureCapacityException() {
        // given
        val userId = 1L
        val saveLecture = saveTestLecture()
        repeat(30) {
            lectureEnrollmentService.enroll(LectureHistoryDto.of(saveLecture, userId + it))
        }

        // when & then
        val result = lectureEnrollmentService.checkLectureCapacity(saveLecture.id!!)

        // then
        assertThat(result).isFalse()
    }

    @Test
    @DisplayName("특강 신청 회원 수가 30명이 넘지 않았을 때 정상적으로 검증 통과하는 테스트 ")
    fun checkLectureCapacity() {
        // given
        val userId = 1L
        val saveLecture = saveTestLecture()
        repeat(29) {
            lectureEnrollmentService.enroll(LectureHistoryDto.of(saveLecture, userId + it))
        }

        // when & then
        assertDoesNotThrow {
            lectureEnrollmentService.checkLectureCapacity(saveLecture.id!!)
        }
    }

    @Test
    @DisplayName("유저 아이디와 강의 객체를 받아서 신청 히스토리에 정상적으로 저장하는지 테스트 ")
    fun register() {
        // given
        val userId = 1L
        val saveLecture = saveTestLecture()
        val lectureHistoryDto = LectureHistoryDto(
            userId = userId,
            lecture = saveLecture,
        )

        // when
        val lectureEnrollmentHistory = lectureEnrollmentService.enroll(lectureHistoryDto)

        // then
        assertThat(lectureEnrollmentHistory).isNotNull
        assertThat(lectureEnrollmentHistory.lecture.id).isEqualTo(saveLecture.id)
        assertThat(lectureEnrollmentHistory.lecture.title).isEqualTo(saveLecture.title)
        assertThat(lectureEnrollmentHistory.lecture.presenter).isEqualTo(saveLecture.presenter)
    }

    @Test
    @DisplayName("특정 유저가 신청한 특강 목록을 가져오는 테스트")
    fun getUserHistoryTest() {
        // given
        val userId = 1L
        val lectures = listOf(
            Lecture(
                title = LectureType.Ai,
                presenter = "Lee",
                creDateAt = LocalDateTime.now(),
            ),
            Lecture(
                title = LectureType.FRONT_END,
                presenter = "Kim",
                creDateAt = LocalDateTime.now(),
            ),
            Lecture(
                title = LectureType.BACK_END,
                presenter = "Park",
                creDateAt = LocalDateTime.now(),
            ),
        )

        val saveLectures = lectureRepository.saveAll(lectures)
        saveLectures.forEach {
            val lectureSchedule = LectureSchedule(
                lecture = it,
                date = dateFormat("2024-09-30 13:00"),
                capacity = 30,
                creDateAt = LocalDateTime.now(),
            )
            lectureScheduleRepository.save(lectureSchedule)
        }

        repeat(3) {
            val lectureHistoryDto = LectureHistoryDto(
                userId = userId,
                lecture = lectures[it],
            )
            lectureEnrollmentService.enroll(lectureHistoryDto)
        }

        // when
        val result = lectureEnrollmentService.getUserOfLectureHistory(userId)

        // then
        assertThat(result).isNotNull()
        assertThat(result.size).isEqualTo(3)
        assertThat(result[0].lecture.schedules[0].capacity).isEqualTo(30)
        assertThat(result[1].lecture.schedules[0].capacity).isEqualTo(30)
        assertThat(result[2].lecture.schedules[0].capacity).isEqualTo(30)
    }

    private fun saveTestLecture(): Lecture {
        val pre_lecture = Lecture(
            title = LectureType.FRONT_END,
            presenter = "Lee",
        )

        val saveLecture = lectureRepository.save(pre_lecture)

        val lectureSchedule = LectureSchedule(
            lecture = saveLecture,
            date = dateFormat("2024-09-30 13:00"),
            capacity = 30,
            creDateAt = LocalDateTime.now(),
        )
        lectureScheduleRepository.save(lectureSchedule)

        return saveLecture
    }

    private fun dateFormat(dateString: String): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val date = LocalDateTime.parse(dateString, formatter)
        return date
    }
}