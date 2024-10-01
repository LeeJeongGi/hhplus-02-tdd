package com._week.hhplus_tdd_practice.domain

import com._week.hhplus_tdd_practice.infra.LectureRepository
import com._week.hhplus_tdd_practice.infra.entity.Lecture
import com._week.hhplus_tdd_practice.infra.entity.LectureType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import kotlin.test.Test

@SpringBootTest
class LectureServiceTest {

    @Autowired
    private lateinit var lectureService: LectureService

    @Autowired
    private lateinit var lectureRepository: LectureRepository

    @BeforeEach
    fun setUp() {
        lectureRepository.deleteAll()
    }

    @Test
    @DisplayName("존재하지 않는 특강에 대한 신청 시도는 실패한다.")
    fun registerException1() {
        // given
        val notExistLectureId = 1L

        // when & then
        val message = assertThrows<IllegalArgumentException> {
            lectureService.findLecture(notExistLectureId)
        }.message

        assertThat(message).isEqualTo("해당 ID의 특강이 존재하지 않습니다.")
    }

    @Test
    @DisplayName("특강 등록 성공 케이스 - 유저가 등록 할 특강을 등록하는 테스트")
    fun creat() {
        // given
        val lecture =  Lecture(
            title = LectureType.FRONT_END,
            presenter = "lee",
            capacity = 30,
            date = LocalDateTime.now()
        )

        // when
        val createLecture = lectureService.create(lecture)

        // then
        assertThat(createLecture.title).isEqualTo(lecture.title)
        assertThat(createLecture.presenter).isEqualTo(lecture.presenter)
        assertThat(createLecture.capacity).isEqualTo(lecture.capacity)
        assertThat(createLecture.date).isEqualTo(lecture.date)
    }

    @Test
    @DisplayName("5개 등록된 날짜 중(전날,지금,미래) 현재 기준으로 조회했을 때 정상적으로 미래 진행될 특강 리스트를 정상적으로 가져오는 테스트")
    fun getUpcomingLectureTest() {
        // Given
        testLectures()
        val now = LocalDateTime.now()

        // When
        val lectures = lectureService.getUpcomingLecture(now)

        // Then
        assertThat(lectures.size).isEqualTo(3)
    }

    @Test
    @DisplayName("진행될 특강이 존재하지 않는 경우 빈 배열이 반환되는지 확인 테스트")
    fun getUpcomingLectureTest2() {
        // Given
        val now = LocalDateTime.now()

        // When
        val lectures = lectureService.getUpcomingLecture(now)

        // Then
        assertThat(lectures.size).isEqualTo(0)
    }

    private fun testLectures() {
        repeat(5) { index ->
            val date = when (index) {
                0 -> LocalDateTime.now().minusDays(1)
                1 -> LocalDateTime.now()
                else -> LocalDateTime.now().plusDays(index - 1L)
            }

            val lecture = Lecture(
                title = LectureType.FRONT_END,
                presenter = "lee",
                capacity = 30,
                date = date
            )

            lectureService.create(lecture)
        }
    }
}