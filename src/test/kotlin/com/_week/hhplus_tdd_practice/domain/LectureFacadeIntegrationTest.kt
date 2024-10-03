package com._week.hhplus_tdd_practice.domain

import com._week.hhplus_tdd_practice.domain.dto.UserLectureDto
import com._week.hhplus_tdd_practice.infra.LectureApplierRepository
import com._week.hhplus_tdd_practice.infra.LectureEnrollmentRepository
import com._week.hhplus_tdd_practice.infra.LectureRepository
import com._week.hhplus_tdd_practice.infra.LectureScheduleRepository
import com._week.hhplus_tdd_practice.infra.entity.Lecture
import com._week.hhplus_tdd_practice.infra.entity.LectureApplier
import com._week.hhplus_tdd_practice.infra.entity.LectureSchedule
import com._week.hhplus_tdd_practice.infra.entity.LectureType
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.Test

@SpringBootTest
class LectureFacadeIntegrationTest {

    @Autowired
    private lateinit var lectureManagementFacade: LectureManagementFacade

    @Autowired
    private lateinit var lectureRepository: LectureRepository

    @Autowired
    private lateinit var lectureScheduleRepository: LectureScheduleRepository

    @Autowired
    private lateinit var lectureEnrollmentRepository: LectureEnrollmentRepository

    @Autowired
    private lateinit var lectureApplierRepository: LectureApplierRepository

    @BeforeEach
    fun setUp() {
        lectureScheduleRepository.deleteAll()
        lectureRepository.deleteAll()
    }
    
    @Test
    @DisplayName("동시에 동일한 특강에 대해 40명이 신청했을 때, 30명만 성공하는 것을 검증하는 통합 테스트")
    fun mustEnrollMaximum30When40AttemptToRegister_Simultaneously() {
        // given
        val lecture = Lecture(
            title = LectureType.BACK_END,
            presenter = "Lee",
            creDateAt = LocalDateTime.now(),
        )
        val saveLecture = lectureRepository.save(lecture)

        val lectureSchedule = LectureSchedule(
            lecture = saveLecture,
            capacity = 30,
            date = LocalDateTime.parse("2024-11-01 13:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            creDateAt = LocalDateTime.now(),
        )
        lectureScheduleRepository.save(lectureSchedule)

        val tt = LectureApplier(
            lectureId = saveLecture.id!!,
            currentEnrollmentCount = 0
        )
        lectureApplierRepository.save(tt)

        val executor = Executors.newFixedThreadPool(40)
        val lectureLatch = CountDownLatch(40)
        val cnt = AtomicInteger(0)

        // when && then
        try {
            repeat(40) {
                executor.submit {
                    try {
                        val userLectureDto = UserLectureDto(
                            lectureId = saveLecture.id!!,
                            userId = (it + 1).toLong()
                        )

                        val enroll = lectureManagementFacade.enroll(userLectureDto)
                        cnt.incrementAndGet()
                    } finally {
                        lectureLatch.countDown()
                    }
                }
            }
            lectureLatch.await()

            val result = lectureApplierRepository.getCurrentEnrollmentCountByLectureId(1L)

            assertThat(cnt.get()).isEqualTo(30)
            assertThat(result).isEqualTo(30)
        } finally {
            executor.shutdown()
        }

    }

    @Test
    @DisplayName("동일한 유저 정보로 같은 특강을 5번 신청했을 때, 1번만 성공하는 것을 검증하는 통합 테스트 작성")
    fun mustOnePassSameUser() {
        // given
        val lecture = Lecture(
            title = LectureType.BACK_END,
            presenter = "Lee",
            creDateAt = LocalDateTime.now(),
        )
        val saveLecture = lectureRepository.save(lecture)

        val lectureSchedule = LectureSchedule(
            lecture = saveLecture,
            capacity = 30,
            date = LocalDateTime.parse("2024-11-01 13:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            creDateAt = LocalDateTime.now(),
        )
        lectureScheduleRepository.save(lectureSchedule)

        val tt = LectureApplier(
            lectureId = saveLecture.id!!,
            currentEnrollmentCount = 0
        )
        lectureApplierRepository.save(tt)

        val executor = Executors.newFixedThreadPool(5)
        val lectureLatch = CountDownLatch(5)
        val cnt = AtomicInteger(0)

        // when && then
        try {
            repeat(5) {
                executor.submit {
                    try {
                        val userLectureDto = UserLectureDto(
                            lectureId = saveLecture.id!!,
                            userId = 1L
                        )

                        val enroll = lectureManagementFacade.enroll(userLectureDto)
                        cnt.incrementAndGet()
                    } finally {
                        lectureLatch.countDown()
                    }
                }
            }
            lectureLatch.await()
            val result = lectureEnrollmentRepository.findByUserIdAndLectureId(1L,1L)

            assertThat(cnt.get()).isEqualTo(1)
            assertThat(result!!.lecture.id).isEqualTo(1L)
            assertThat(result.userId).isEqualTo(1L)
        } finally {
            executor.shutdown()
        }
    }
}