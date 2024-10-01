package com._week.hhplus_tdd_practice.domain

import com._week.hhplus_tdd_practice.infra.entity.Lecture
import com._week.hhplus_tdd_practice.infra.entity.LectureType
import org.assertj.core.api.Assertions.assertThat
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
}