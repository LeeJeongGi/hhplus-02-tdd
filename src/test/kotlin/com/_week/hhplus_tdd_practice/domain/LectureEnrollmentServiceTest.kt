package com._week.hhplus_tdd_practice.domain

import com._week.hhplus_tdd_practice.domain.dto.LectureHistoryDto
import com._week.hhplus_tdd_practice.domain.dto.UserLectureDto
import com._week.hhplus_tdd_practice.infra.entity.Lecture
import com._week.hhplus_tdd_practice.infra.entity.LectureType
import com._week.hhplus_tdd_practice.infra.LectureRepository
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
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

    @BeforeEach
    fun clear() {
        lectureRepository.deleteAll()
    }

    @Test
    @DisplayName("동일한 유저가 동일 특강에 신청할 경우 에러가 발생 한다.")
    fun checkUserEnrollmentException() {
        // given
        val userId = 1L
        val saveLecture = saveTestLecture()
        lectureEnrollmentService.enroll(LectureHistoryDto.of(saveLecture, userId))

        // when & then
        val message = assertThrows<IllegalArgumentException> {
            lectureEnrollmentService.checkUserEnrollment(UserLectureDto.of(saveLecture.id!!, userId))
        }.message

        assertThat(message).isEqualTo("동일한 유저는 동일 특강에 대해 한 번만 신청할 수 있습니다.")
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
    @DisplayName("특강 신청 회원 수는 30명을 넘을 수 없기 때문에 31번째 신청 했을 때 에러 발생 테스트")
    fun checkLectureCapacityException() {
        // given
        val userId = 1L
        val saveLecture = saveTestLecture()
        repeat(30) {
            lectureEnrollmentService.enroll(LectureHistoryDto.of(saveLecture, userId + it))
        }

        // when & then
        val message = assertThrows<IllegalArgumentException> {
            lectureEnrollmentService.checkLectureCapacity(saveLecture.id!!)
        }.message

        assertThat(message).isEqualTo("신청 인원이 30명을 초과할 수 없습니다.")
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
        assertThat(lectureEnrollmentHistory.lecture.capacity).isEqualTo(30L)

    }

    private fun saveTestLecture(): Lecture {
        val pre_lecture = Lecture(
            title = LectureType.FRONT_END,
            presenter = "Lee",
            capacity = 30,
            date = dateFormat("2024-09-30 13:00"),
        )

        return lectureRepository.save(pre_lecture)
    }

    private fun dateFormat(dateString: String): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val date = LocalDateTime.parse(dateString, formatter)
        return date
    }
}