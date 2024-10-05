package com._week.hhplus_tdd_practice.domain

import com._week.hhplus_tdd_practice.infra.LectureApplierRepository
import com._week.hhplus_tdd_practice.infra.entity.LectureApplier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LectureApplierService(
    val lectureApplierRepository: LectureApplierRepository
) {

    @Transactional
    fun getLectureCapacity(lectureId: Long): LectureApplier {
        val lectureApplier =  lectureApplierRepository.findByLectureIdWithLock(lectureId)
            ?: throw IllegalArgumentException("해당 특강이 존재하지 않습니다.")

        return lectureApplier
    }

    @Transactional
    fun incrementEnrollmentCount(lectureId: Long): Long? {
        val lectureApplier =  lectureApplierRepository.findByLectureIdWithLock(lectureId)
            ?: throw IllegalArgumentException("해당 특강이 존재하지 않습니다.")

        lectureApplier.increaseEnrollmentCount()
        return lectureApplierRepository.save(lectureApplier).id
    }


}