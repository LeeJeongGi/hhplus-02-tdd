package com._week.hhplus_tdd_practice.blog

import com._week.hhplus_tdd_practice.blog.entity.Todo
import com._week.hhplus_tdd_practice.blog.entity.User
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestDocumentPractice {

    @Test
    fun `새로운_할_일을_등록하면_저장되어야_한다`() {
        // given
        val user = User("정기")
        val todo = Todo("블로그작성하기", "2025-03-13")

        // when
        user.addTodo(todo)

        // then
        assertTrue(user.todos.contains(todo))
    }

    @Test
    fun `할_일을_완료하면_점수가_증가해야_한다`() {
        // given
        val user = User("정기")
        val todo = Todo("블로그작성하기", "2025-03-13")

        user.addTodo(todo)

        // when
        user.completeTodo(todo)

        // then
        assertEquals(10, user.getScore()) // 할 일을 완료하면 10점 증가한다고 가정
    }

    @Test
    fun `마감_기한이_지나면_점수가_차감되어야_한다`() {
        // given
        val user = User("정기")
        val overdueTodo = Todo("블로그작성하기", "2025-03-10")
        val validTodo = Todo("운동하기", "2025-03-15") // 미래 날짜

        user.addTodo(overdueTodo)
        user.addTodo(validTodo)

        // when
        user.checkOverdueTodos()

        // then
        assertEquals(-5, user.getScore()) // 마감 기한 초과 시 5점 차감

    }

    @Test
    fun `현재_점수를_정확하게_조회할_수_있어야_한다`() {
        // given
        val user = User("정기")
        val todo1 = Todo("블로그작성하기", "2025-03-15")
        val todo2 = Todo("운동하기", "2025-03-10") // 기한 초과 예정

        user.addTodo(todo1)
        user.addTodo(todo2)

        // when
        user.completeTodo(todo1) // 10점 증가
        user.checkOverdueTodos() // 5점 차감

        // then
        assertEquals(5, user.getScore()) // (10 - 5 = 5점 예상)
    }
}